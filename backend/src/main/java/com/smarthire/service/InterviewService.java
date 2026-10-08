package com.smarthire.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smarthire.dto.InterviewDTO;
import com.smarthire.dto.ScheduleInterviewRequest;
import com.smarthire.dto.UpdateInterviewRequest;
import com.smarthire.entity.*;
import com.smarthire.exception.BadRequestException;
import com.smarthire.exception.InterviewConflictException;
import com.smarthire.exception.InterviewNotFoundException;
import com.smarthire.exception.InvalidApplicationStatusException;
import com.smarthire.exception.UnauthorizedAccessException;
import com.smarthire.notification.NotificationService;
import com.smarthire.repository.ApplicationRepository;
import com.smarthire.repository.InterviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewService {
    private static final Logger log = LoggerFactory.getLogger(InterviewService.class);

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;
    private final RecruiterService recruiterService;
    private final NotificationService notificationService;

    public InterviewService(InterviewRepository interviewRepository, ApplicationRepository applicationRepository, ApplicationService applicationService, RecruiterService recruiterService, NotificationService notificationService) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.applicationService = applicationService;
        this.recruiterService = recruiterService;
        this.notificationService = notificationService;
    }

    @Transactional
    public InterviewDTO scheduleInterview(User recruiterUser, ScheduleInterviewRequest request) {
        Application application = applicationService.getApplicationEntity(request.getApplicationId());
        Company company = application.getJob().getCompany();

        // 1. Recruiter belongs to the company
        recruiterService.verifyRecruiterBelongsToCompany(recruiterUser, company);

        // 2. Business rule: Cannot schedule interview for rejected or withdrawn applications
        if (application.getStatus() == ApplicationStatus.REJECTED || application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new InvalidApplicationStatusException("Cannot schedule an interview for an application in status: " + application.getStatus());
        }

        // 3. Date / Time validation
        if (request.getInterviewDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Interview date cannot be in the past");
        }

        if (request.getInterviewDate().isEqual(LocalDate.now()) && request.getStartTime().isBefore(LocalTime.now())) {
            throw new BadRequestException("Interview start time cannot be in the past for today's date");
        }

        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new BadRequestException("Interview end time must be strictly after start time");
        }

        // 4. Business rule: Conflict detection - same interviewer cannot have overlapping interview
        boolean hasConflict = interviewRepository.existsConflictingInterview(
                recruiterUser,
                request.getInterviewDate(),
                request.getStartTime(),
                request.getEndTime(),
                InterviewStatus.SCHEDULED,
                null
        );

        if (hasConflict) {
            throw new InterviewConflictException("Scheduling conflict: Interviewer " + recruiterUser.getFullName() +
                    " already has another interview scheduled during " + request.getStartTime() + " - " + request.getEndTime() +
                    " on " + request.getInterviewDate());
        }

        Interview interview = Interview.builder()
                .application(application)
                .interviewer(recruiterUser)
                .interviewType(request.getInterviewType())
                .interviewDate(request.getInterviewDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .meetingLink(request.getMeetingLink())
                .location(request.getLocation())
                .notes(request.getNotes())
                .status(InterviewStatus.SCHEDULED)
                .build();

        Interview saved = interviewRepository.save(interview);

        // Update application status to INTERVIEW_SCHEDULED
        application.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applicationRepository.save(application);

        // Notify Candidate
        String formattedDate = request.getInterviewDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        String formattedTime = request.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
        notificationService.sendNotification(
                application.getCandidate(),
                "Interview Scheduled: " + request.getInterviewType(),
                "Your " + request.getInterviewType() + " interview for " + application.getJob().getTitle() +
                        " at " + company.getName() + " has been scheduled for " + formattedDate + " at " + formattedTime +
                        (request.getMeetingLink() != null ? ". Meeting link: " + request.getMeetingLink() : ""),
                "/candidate/interviews"
        );

        log.info("Interview #{} scheduled for application #{} by recruiter {}", saved.getId(), application.getId(), recruiterUser.getEmail());
        return mapToDTO(saved);
    }

    @Transactional
    public InterviewDTO updateInterview(Long interviewId, User recruiterUser, UpdateInterviewRequest request) {
        Interview interview = getInterviewEntity(interviewId);
        Company company = interview.getApplication().getJob().getCompany();
        recruiterService.verifyRecruiterBelongsToCompany(recruiterUser, company);

        if (request.getInterviewDate() != null) {
            if (request.getInterviewDate().isBefore(LocalDate.now())) {
                throw new BadRequestException("Interview date cannot be in the past");
            }
            interview.setInterviewDate(request.getInterviewDate());
        }

        if (request.getStartTime() != null) {
            interview.setStartTime(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            interview.setEndTime(request.getEndTime());
        }

        if (!interview.getEndTime().isAfter(interview.getStartTime())) {
            throw new BadRequestException("Interview end time must be after start time");
        }

        // Check conflicts if date/time modified
        if (request.getStatus() == null || request.getStatus() == InterviewStatus.SCHEDULED) {
            boolean hasConflict = interviewRepository.existsConflictingInterview(
                    interview.getInterviewer(),
                    interview.getInterviewDate(),
                    interview.getStartTime(),
                    interview.getEndTime(),
                    InterviewStatus.SCHEDULED,
                    interview.getId()
            );
            if (hasConflict) {
                throw new InterviewConflictException("Scheduling conflict: Interviewer has an overlapping interview scheduled for that slot.");
            }
        }

        if (request.getInterviewType() != null) interview.setInterviewType(request.getInterviewType());
        if (request.getMeetingLink() != null) interview.setMeetingLink(request.getMeetingLink());
        if (request.getLocation() != null) interview.setLocation(request.getLocation());
        if (request.getNotes() != null) interview.setNotes(request.getNotes());
        if (request.getStatus() != null) interview.setStatus(request.getStatus());

        Interview saved = interviewRepository.save(interview);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<InterviewDTO> getCandidateInterviews(User candidateUser) {
        return interviewRepository.findByCandidate(candidateUser).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterviewDTO> getRecruiterInterviews(User recruiterUser) {
        Company company = recruiterService.getRecruiterCompany(recruiterUser);
        return interviewRepository.findByCompanyId(company.getId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Interview getInterviewEntity(Long interviewId) {
        return interviewRepository.findById(interviewId)
                .orElseThrow(() -> new InterviewNotFoundException("Interview not found with ID: " + interviewId));
    }

    private InterviewDTO mapToDTO(Interview i) {
        return InterviewDTO.builder()
                .id(i.getId())
                .applicationId(i.getApplication().getId())
                .jobId(i.getApplication().getJob().getId())
                .jobTitle(i.getApplication().getJob().getTitle())
                .companyName(i.getApplication().getJob().getCompany().getName())
                .candidateId(i.getApplication().getCandidate().getId())
                .candidateName(i.getApplication().getCandidate().getFullName())
                .interviewerId(i.getInterviewer().getId())
                .interviewerName(i.getInterviewer().getFullName())
                .interviewType(i.getInterviewType())
                .interviewDate(i.getInterviewDate())
                .startTime(i.getStartTime())
                .endTime(i.getEndTime())
                .meetingLink(i.getMeetingLink())
                .location(i.getLocation())
                .notes(i.getNotes())
                .status(i.getStatus())
                .createdAt(i.getCreatedAt())
                .build();
    }
}
