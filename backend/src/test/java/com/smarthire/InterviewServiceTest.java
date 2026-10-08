package com.smarthire;

import com.smarthire.dto.ScheduleInterviewRequest;
import com.smarthire.entity.*;
import com.smarthire.exception.BadRequestException;
import com.smarthire.exception.InterviewConflictException;
import com.smarthire.exception.InvalidApplicationStatusException;
import com.smarthire.notification.NotificationService;
import com.smarthire.repository.ApplicationRepository;
import com.smarthire.repository.InterviewRepository;
import com.smarthire.service.ApplicationService;
import com.smarthire.service.InterviewService;
import com.smarthire.service.RecruiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewServiceTest {

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ApplicationService applicationService;

    @Mock
    private RecruiterService recruiterService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private InterviewService interviewService;

    private User recruiterUser;
    private User candidateUser;
    private Company company;
    private Job job;
    private Application application;

    @BeforeEach
    void setUp() {
        recruiterUser = User.builder().id(2L).email("recruiter@test.com").fullName("Test Recruiter").build();
        candidateUser = User.builder().id(1L).email("candidate@test.com").fullName("Test Candidate").build();
        company = Company.builder().id(10L).name("Acme Corp").verified(true).build();
        job = Job.builder().id(100L).company(company).build();

        application = Application.builder()
                .id(200L)
                .candidate(candidateUser)
                .job(job)
                .status(ApplicationStatus.SHORTLISTED)
                .build();
    }

    @Test
    void shouldNotScheduleOverlappingInterview() {
        when(applicationService.getApplicationEntity(200L)).thenReturn(application);

        LocalDate interviewDate = LocalDate.now().plusDays(2);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(11, 0);

        ScheduleInterviewRequest request = ScheduleInterviewRequest.builder()
                .applicationId(200L)
                .interviewType(InterviewType.TECHNICAL)
                .interviewDate(interviewDate)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        when(interviewRepository.existsConflictingInterview(
                recruiterUser,
                interviewDate,
                startTime,
                endTime,
                InterviewStatus.SCHEDULED,
                null
        )).thenReturn(true);

        assertThrows(InterviewConflictException.class, () -> {
            interviewService.scheduleInterview(recruiterUser, request);
        });

        verify(interviewRepository, never()).save(any());
    }

    @Test
    void shouldNotScheduleInterviewInPast() {
        when(applicationService.getApplicationEntity(200L)).thenReturn(application);

        ScheduleInterviewRequest request = ScheduleInterviewRequest.builder()
                .applicationId(200L)
                .interviewType(InterviewType.TECHNICAL)
                .interviewDate(LocalDate.now().minusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        assertThrows(BadRequestException.class, () -> {
            interviewService.scheduleInterview(recruiterUser, request);
        });
    }

    @Test
    void shouldNotScheduleInterviewForRejectedApplication() {
        application.setStatus(ApplicationStatus.REJECTED);
        when(applicationService.getApplicationEntity(200L)).thenReturn(application);

        ScheduleInterviewRequest request = ScheduleInterviewRequest.builder()
                .applicationId(200L)
                .interviewType(InterviewType.TECHNICAL)
                .interviewDate(LocalDate.now().plusDays(2))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        assertThrows(InvalidApplicationStatusException.class, () -> {
            interviewService.scheduleInterview(recruiterUser, request);
        });
    }
}
