package com.smarthire.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smarthire.dto.ApplicationDTO;
import com.smarthire.dto.ApplyJobRequest;
import com.smarthire.dto.InterviewDTO;
import com.smarthire.dto.UpdateApplicationStatusRequest;
import com.smarthire.entity.*;
import com.smarthire.exception.*;
import com.smarthire.notification.NotificationService;
import com.smarthire.repository.ApplicationRepository;
import com.smarthire.repository.CandidateProfileRepository;
import com.smarthire.repository.InterviewRepository;
import com.smarthire.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    private final ApplicationRepository applicationRepository;
    private final JobService jobService;
    private final CandidateProfileRepository candidateProfileRepository;
    private final ResumeRepository resumeRepository;
    private final RecruiterService recruiterService;
    private final NotificationService notificationService;
    private final InterviewRepository interviewRepository;

    public ApplicationService(ApplicationRepository applicationRepository, JobService jobService, CandidateProfileRepository candidateProfileRepository, ResumeRepository resumeRepository, RecruiterService recruiterService, NotificationService notificationService, InterviewRepository interviewRepository) {
        this.applicationRepository = applicationRepository;
        this.jobService = jobService;
        this.candidateProfileRepository = candidateProfileRepository;
        this.resumeRepository = resumeRepository;
        this.recruiterService = recruiterService;
        this.notificationService = notificationService;
        this.interviewRepository = interviewRepository;
    }

    @Transactional
    public ApplicationDTO applyForJob(Long jobId, User candidateUser, ApplyJobRequest request) {
        Job job = jobService.getJobEntity(jobId);

        // 1. Business Rule: Job must not be expired or closed
        if (job.getStatus() == JobStatus.CLOSED || job.getApplicationDeadline().isBefore(LocalDate.now())) {
            throw new JobExpiredException("This job vacancy has closed or reached its application deadline on " + job.getApplicationDeadline());
        }

        // 2. Business Rule: Candidate cannot apply to the same job twice
        if (applicationRepository.existsByCandidateIdAndJobId(candidateUser.getId(), jobId)) {
            throw new DuplicateApplicationException("You have already submitted an application for this position (" + job.getTitle() + ")");
        }

        // 3. Resolve resume
        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateUser.getId())
                .orElseThrow(() -> new BadRequestException("Please create your candidate profile before applying"));

        Resume resume = null;
        if (request != null && request.getResumeId() != null) {
            resume = resumeRepository.findById(request.getResumeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Specified resume not found"));
            if (!resume.getCandidateProfile().getId().equals(profile.getId())) {
                throw new UnauthorizedAccessException("Cannot attach another user's resume");
            }
        } else {
            resume = resumeRepository.findByCandidateProfileAndIsPrimaryTrue(profile)
                    .orElseGet(() -> {
                        List<Resume> allResumes = resumeRepository.findByCandidateProfileOrderByUploadedAtDesc(profile);
                        return allResumes.isEmpty() ? null : allResumes.get(0);
                    });
        }

        Application application = Application.builder()
                .candidate(candidateUser)
                .job(job)
                .resume(resume)
                .coverLetter(request != null ? request.getCoverLetter() : null)
                .status(ApplicationStatus.APPLIED)
                .build();

        Application saved = applicationRepository.save(application);

        // Notify recruiter
        notificationService.sendNotification(
                job.getRecruiter(),
                "New Application Received",
                candidateUser.getFullName() + " applied for " + job.getTitle(),
                "/recruiter/applications/" + saved.getId()
        );

        // Notify candidate confirmation
        notificationService.sendNotification(
                candidateUser,
                "Application Submitted",
                "Your application for " + job.getTitle() + " at " + job.getCompany().getName() + " was successfully received.",
                "/candidate/applications/" + saved.getId()
        );

        log.info("Candidate {} applied for job #{}", candidateUser.getEmail(), job.getId());
        return mapToDTO(saved);
    }

    @Transactional
    public ApplicationDTO updateStatus(Long applicationId, User recruiterUser, UpdateApplicationStatusRequest request) {
        Application application = getApplicationEntity(applicationId);
        Company company = application.getJob().getCompany();

        // Business Rule: Recruiter must belong to the job's company
        recruiterService.verifyRecruiterBelongsToCompany(recruiterUser, company);

        ApplicationStatus currentStatus = application.getStatus();
        ApplicationStatus newStatus = request.getStatus();

        // Business Rule: State machine transition validations
        validateStatusTransition(currentStatus, newStatus);

        application.setStatus(newStatus);
        if (request.getRecruiterNotes() != null) {
            application.setRecruiterNotes(request.getRecruiterNotes());
        }
        if (request.getRejectionReason() != null) {
            application.setRejectionReason(request.getRejectionReason());
        }

        Application saved = applicationRepository.save(application);

        // Send notifications based on new status
        dispatchStatusNotification(saved);

        log.info("Application #{} moved from {} to {} by recruiter {}",
                applicationId, currentStatus, newStatus, recruiterUser.getEmail());

        return mapToDTO(saved);
    }

    @Transactional
    public ApplicationDTO withdrawApplication(Long applicationId, User candidateUser) {
        Application application = getApplicationEntity(applicationId);

        if (!application.getCandidate().getId().equals(candidateUser.getId())) {
            throw new UnauthorizedAccessException("Cannot withdraw another candidate's application");
        }

        if (application.getStatus() == ApplicationStatus.SELECTED || application.getStatus() == ApplicationStatus.REJECTED) {
            throw new InvalidApplicationStatusException("Cannot withdraw an application that has already reached final decision (" + application.getStatus() + ")");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        Application saved = applicationRepository.save(application);

        notificationService.sendNotification(
                application.getJob().getRecruiter(),
                "Application Withdrawn",
                candidateUser.getFullName() + " withdrew their application for " + application.getJob().getTitle(),
                "/recruiter/applications"
        );

        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ApplicationDTO> getCandidateApplications(User candidateUser) {
        return applicationRepository.findByCandidateOrderByAppliedAtDesc(candidateUser).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ApplicationDTO> getCompanyApplications(User recruiterUser) {
        Company company = recruiterService.getRecruiterCompany(recruiterUser);
        return applicationRepository.findByCompanyId(company.getId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ApplicationDTO> getJobApplications(Long jobId, User recruiterUser) {
        Job job = jobService.getJobEntity(jobId);
        recruiterService.verifyRecruiterBelongsToCompany(recruiterUser, job.getCompany());
        return applicationRepository.findByJob(job).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ApplicationDTO getApplicationById(Long applicationId, User currentUser) {
        Application application = getApplicationEntity(applicationId);

        if (currentUser.getRole() == Role.ROLE_CANDIDATE) {
            if (!application.getCandidate().getId().equals(currentUser.getId())) {
                throw new UnauthorizedAccessException("Cannot view another candidate's application");
            }
        } else if (currentUser.getRole() == Role.ROLE_RECRUITER) {
            recruiterService.verifyRecruiterBelongsToCompany(currentUser, application.getJob().getCompany());
        }

        return mapToDTO(application);
    }

    public Application getApplicationEntity(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found with ID: " + applicationId));
    }

    private void validateStatusTransition(ApplicationStatus from, ApplicationStatus to) {
        if (from == to) {
            return;
        }

        if (from == ApplicationStatus.REJECTED || from == ApplicationStatus.WITHDRAWN) {
            throw new InvalidApplicationStatusException("Cannot transition an application that is already in closed state: " + from);
        }

        // Allowed transitions:
        // APPLIED -> UNDER_REVIEW, REJECTED, WITHDRAWN
        // UNDER_REVIEW -> SHORTLISTED, REJECTED, WITHDRAWN
        // SHORTLISTED -> INTERVIEW_SCHEDULED, REJECTED, WITHDRAWN
        // INTERVIEW_SCHEDULED -> SELECTED, REJECTED
        // SELECTED -> final
        if (to == ApplicationStatus.SELECTED && from != ApplicationStatus.INTERVIEW_SCHEDULED && from != ApplicationStatus.SHORTLISTED) {
            throw new InvalidApplicationStatusException("Candidate must be shortlisted or have completed an interview before being selected. Current: " + from);
        }
    }

    private void dispatchStatusNotification(Application application) {
        User candidate = application.getCandidate();
        String jobTitle = application.getJob().getTitle();
        String companyName = application.getJob().getCompany().getName();

        switch (application.getStatus()) {
            case SHORTLISTED -> notificationService.sendNotification(
                    candidate,
                    "Application Shortlisted",
                    "Your application for " + jobTitle + " at " + companyName + " has been shortlisted!",
                    "/candidate/applications/" + application.getId()
            );
            case SELECTED -> notificationService.sendNotification(
                    candidate,
                    "Congratulations! Offer Extended",
                    "Congratulations! You have been selected for the " + jobTitle + " position at " + companyName + ".",
                    "/candidate/applications/" + application.getId()
            );
            case REJECTED -> {
                String reason = application.getRejectionReason() != null ? "\nReason: " + application.getRejectionReason() : "";
                notificationService.sendNotification(
                        candidate,
                        "Application Status Update",
                        "Thank you for your interest in " + jobTitle + " at " + companyName + ". Unfortunately, we will not be moving forward at this time." + reason,
                        "/candidate/applications/" + application.getId()
                );
            }
            default -> {}
        }
    }

    public ApplicationDTO mapToDTO(Application a) {
        List<InterviewDTO> interviews = interviewRepository.findByApplicationId(a.getId()).stream()
                .map(i -> InterviewDTO.builder()
                        .id(i.getId())
                        .applicationId(a.getId())
                        .jobId(a.getJob().getId())
                        .jobTitle(a.getJob().getTitle())
                        .companyName(a.getJob().getCompany().getName())
                        .candidateId(a.getCandidate().getId())
                        .candidateName(a.getCandidate().getFullName())
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
                        .build())
                .collect(Collectors.toList());

        return ApplicationDTO.builder()
                .id(a.getId())
                .candidateId(a.getCandidate().getId())
                .candidateName(a.getCandidate().getFullName())
                .candidateEmail(a.getCandidate().getEmail())
                .jobId(a.getJob().getId())
                .jobTitle(a.getJob().getTitle())
                .companyId(a.getJob().getCompany().getId())
                .companyName(a.getJob().getCompany().getName())
                .resumeId(a.getResume() != null ? a.getResume().getId() : null)
                .resumeFileName(a.getResume() != null ? a.getResume().getOriginalFileName() : null)
                .coverLetter(a.getCoverLetter())
                .status(a.getStatus())
                .recruiterNotes(a.getRecruiterNotes())
                .rejectionReason(a.getRejectionReason())
                .interviews(interviews)
                .appliedAt(a.getAppliedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
