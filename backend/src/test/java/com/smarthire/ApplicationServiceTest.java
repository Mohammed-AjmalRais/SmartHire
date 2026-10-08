package com.smarthire;

import com.smarthire.dto.ApplyJobRequest;
import com.smarthire.dto.UpdateApplicationStatusRequest;
import com.smarthire.entity.*;
import com.smarthire.exception.DuplicateApplicationException;
import com.smarthire.exception.InvalidApplicationStatusException;
import com.smarthire.exception.JobExpiredException;
import com.smarthire.exception.UnauthorizedAccessException;
import com.smarthire.notification.NotificationService;
import com.smarthire.repository.ApplicationRepository;
import com.smarthire.repository.CandidateProfileRepository;
import com.smarthire.repository.InterviewRepository;
import com.smarthire.repository.ResumeRepository;
import com.smarthire.service.ApplicationService;
import com.smarthire.service.JobService;
import com.smarthire.service.RecruiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobService jobService;

    @Mock
    private CandidateProfileRepository candidateProfileRepository;

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private RecruiterService recruiterService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private InterviewRepository interviewRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private User candidateUser;
    private User recruiterUser;
    private Company company;
    private Job activeJob;
    private Job expiredJob;

    @BeforeEach
    void setUp() {
        candidateUser = User.builder()
                .id(1L)
                .email("candidate@test.com")
                .fullName("Test Candidate")
                .role(Role.ROLE_CANDIDATE)
                .build();

        recruiterUser = User.builder()
                .id(2L)
                .email("recruiter@test.com")
                .fullName("Test Recruiter")
                .role(Role.ROLE_RECRUITER)
                .build();

        company = Company.builder()
                .id(10L)
                .name("Acme Corp")
                .verified(true)
                .build();

        activeJob = Job.builder()
                .id(100L)
                .company(company)
                .recruiter(recruiterUser)
                .title("Java Developer")
                .status(JobStatus.PUBLISHED)
                .applicationDeadline(LocalDate.now().plusDays(10))
                .build();

        expiredJob = Job.builder()
                .id(101L)
                .company(company)
                .recruiter(recruiterUser)
                .title("Expired Job")
                .status(JobStatus.PUBLISHED)
                .applicationDeadline(LocalDate.now().minusDays(1))
                .build();
    }

    @Test
    void shouldNotApplyToExpiredJob() {
        when(jobService.getJobEntity(101L)).thenReturn(expiredJob);

        assertThrows(JobExpiredException.class, () -> {
            applicationService.applyForJob(101L, candidateUser, new ApplyJobRequest());
        });

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void shouldNotAllowDuplicateApplication() {
        when(jobService.getJobEntity(100L)).thenReturn(activeJob);
        when(applicationRepository.existsByCandidateIdAndJobId(candidateUser.getId(), 100L)).thenReturn(true);

        assertThrows(DuplicateApplicationException.class, () -> {
            applicationService.applyForJob(100L, candidateUser, new ApplyJobRequest());
        });

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void shouldNotAllowRecruiterToAccessAnotherCompanyApplication() {
        Application application = Application.builder()
                .id(500L)
                .candidate(candidateUser)
                .job(activeJob)
                .status(ApplicationStatus.APPLIED)
                .build();

        when(applicationRepository.findById(500L)).thenReturn(Optional.of(application));
        doThrow(new UnauthorizedAccessException("Not authorized"))
                .when(recruiterService).verifyRecruiterBelongsToCompany(recruiterUser, company);

        UpdateApplicationStatusRequest request = UpdateApplicationStatusRequest.builder()
                .status(ApplicationStatus.UNDER_REVIEW)
                .build();

        assertThrows(UnauthorizedAccessException.class, () -> {
            applicationService.updateStatus(500L, recruiterUser, request);
        });
    }

    @Test
    void shouldThrowWhenTransitioningToSelectedDirectlyFromApplied() {
        Application application = Application.builder()
                .id(500L)
                .candidate(candidateUser)
                .job(activeJob)
                .status(ApplicationStatus.APPLIED)
                .build();

        when(applicationRepository.findById(500L)).thenReturn(Optional.of(application));

        UpdateApplicationStatusRequest request = UpdateApplicationStatusRequest.builder()
                .status(ApplicationStatus.SELECTED)
                .build();

        assertThrows(InvalidApplicationStatusException.class, () -> {
            applicationService.updateStatus(500L, recruiterUser, request);
        });
    }
}
