package com.smarthire.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smarthire.dto.AdminStatsDTO;
import com.smarthire.dto.CompanyDTO;
import com.smarthire.dto.CompanyVerificationRequest;
import com.smarthire.dto.UserDTO;
import com.smarthire.entity.*;
import com.smarthire.exception.ResourceNotFoundException;
import com.smarthire.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final RecruiterService recruiterService;
    private final AuthService authService;

    public AdminService(UserRepository userRepository, CompanyRepository companyRepository, JobRepository jobRepository, ApplicationRepository applicationRepository, InterviewRepository interviewRepository, RecruiterService recruiterService, AuthService authService) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.recruiterService = recruiterService;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public AdminStatsDTO getPlatformStatistics() {
        long totalUsers = userRepository.count();
        long totalCandidates = userRepository.countByRoleEquals(Role.ROLE_CANDIDATE);
        long totalRecruiters = userRepository.countByRoleEquals(Role.ROLE_RECRUITER);
        long totalCompanies = companyRepository.count();
        long pendingCompanies = companyRepository.countByVerified(false);
        long verifiedCompanies = companyRepository.countByVerified(true);
        long activeJobs = jobRepository.countByStatus(JobStatus.PUBLISHED);
        long totalApplications = applicationRepository.count();
        long shortlisted = applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED);
        long scheduledInterviews = interviewRepository.countByStatus(InterviewStatus.SCHEDULED);
        long selectedCandidates = applicationRepository.countByStatus(ApplicationStatus.SELECTED);

        return AdminStatsDTO.builder()
                .totalUsers(totalUsers)
                .totalCandidates(totalCandidates)
                .totalRecruiters(totalRecruiters)
                .totalCompanies(totalCompanies)
                .pendingCompanies(pendingCompanies)
                .verifiedCompanies(verifiedCompanies)
                .activeJobs(activeJobs)
                .totalApplications(totalApplications)
                .shortlistedApplications(shortlisted)
                .scheduledInterviews(scheduledInterviews)
                .selectedCandidates(selectedCandidates)
                .build();
    }

    @Transactional(readOnly = true)
    public List<CompanyDTO> getAllCompanies(Boolean verified) {
        List<Company> companies;
        if (verified != null) {
            companies = companyRepository.findByVerified(verified);
        } else {
            companies = companyRepository.findAll();
        }
        return companies.stream().map(recruiterService::mapToCompanyDTO).collect(Collectors.toList());
    }

    @Transactional
    public CompanyDTO verifyCompany(Long companyId, CompanyVerificationRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));

        company.setVerified(request.getVerified());
        Company saved = companyRepository.save(company);
        log.info("Company #{} '{}' verification status updated to {}", company.getId(), company.getName(), request.getVerified());
        return recruiterService.mapToCompanyDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(authService::mapToUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO toggleUserStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setEnabled(enabled);
        User saved = userRepository.save(user);
        log.info("User #{} '{}' active status set to {}", saved.getId(), saved.getEmail(), enabled);
        return authService.mapToUserDTO(saved);
    }
}
