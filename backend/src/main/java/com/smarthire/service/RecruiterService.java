package com.smarthire.service;

import com.smarthire.dto.CompanyDTO;
import com.smarthire.dto.CreateCompanyRequest;
import com.smarthire.dto.RecruiterStatsDTO;
import com.smarthire.entity.*;
import com.smarthire.exception.BadRequestException;
import com.smarthire.exception.ResourceNotFoundException;
import com.smarthire.exception.UnauthorizedAccessException;
import com.smarthire.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecruiterService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;

    public RecruiterService(RecruiterProfileRepository recruiterProfileRepository, CompanyRepository companyRepository, JobRepository jobRepository, ApplicationRepository applicationRepository, InterviewRepository interviewRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
    }

    @Transactional(readOnly = true)
    public RecruiterProfile getRecruiterProfile(User user) {
        return recruiterProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found for user: " + user.getId()));
    }

    @Transactional(readOnly = true)
    public Company getRecruiterCompany(User user) {
        RecruiterProfile profile = getRecruiterProfile(user);
        if (profile.getCompany() == null) {
            throw new BadRequestException("Recruiter has not set up or joined a company profile yet.");
        }
        return profile.getCompany();
    }

    @Transactional
    public CompanyDTO createOrUpdateCompany(User user, CreateCompanyRequest request) {
        RecruiterProfile profile = getRecruiterProfile(user);
        Company company = profile.getCompany();

        if (company == null) {
            // Check if company with this name exists
            company = companyRepository.findByNameIgnoreCase(request.getName().trim())
                    .orElseGet(() -> Company.builder()
                            .name(request.getName().trim())
                            .verified(false) // Needs admin verification
                            .build());
        }

        company.setName(request.getName().trim());
        company.setDescription(request.getDescription());
        company.setWebsite(request.getWebsite());
        company.setIndustry(request.getIndustry());
        company.setLocation(request.getLocation());
        company.setLogoUrl(request.getLogoUrl());

        Company savedCompany = companyRepository.save(company);
        profile.setCompany(savedCompany);
        recruiterProfileRepository.save(profile);

        return mapToCompanyDTO(savedCompany);
    }

    @Transactional(readOnly = true)
    public CompanyDTO getMyCompany(User user) {
        Company company = getRecruiterCompany(user);
        return mapToCompanyDTO(company);
    }

    @Transactional(readOnly = true)
    public RecruiterStatsDTO getRecruiterDashboardStats(User user) {
        Company company = getRecruiterCompany(user);

        long activeJobs = jobRepository.countByCompany(company);
        long totalApps = applicationRepository.countByJobCompany(company);
        long underReview = applicationRepository.countByJobCompanyAndStatus(company, ApplicationStatus.UNDER_REVIEW);
        long shortlisted = applicationRepository.countByJobCompanyAndStatus(company, ApplicationStatus.SHORTLISTED);
        long scheduled = interviewRepository.countByCompanyId(company.getId());
        long selected = applicationRepository.countByJobCompanyAndStatus(company, ApplicationStatus.SELECTED);
        long rejected = applicationRepository.countByJobCompanyAndStatus(company, ApplicationStatus.REJECTED);

        return RecruiterStatsDTO.builder()
                .activeJobs(activeJobs)
                .totalApplications(totalApps)
                .underReviewApplications(underReview)
                .shortlistedApplications(shortlisted)
                .scheduledInterviews(scheduled)
                .selectedCandidates(selected)
                .rejectedCandidates(rejected)
                .build();
    }

    public void verifyRecruiterBelongsToCompany(User recruiter, Company company) {
        RecruiterProfile profile = getRecruiterProfile(recruiter);
        if (profile.getCompany() == null || !profile.getCompany().getId().equals(company.getId())) {
            throw new UnauthorizedAccessException("Recruiter is not authorized to manage jobs or applications for this company.");
        }
    }

    public CompanyDTO mapToCompanyDTO(Company c) {
        long activeJobs = jobRepository.countByCompany(c);
        return CompanyDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .website(c.getWebsite())
                .industry(c.getIndustry())
                .location(c.getLocation())
                .logoUrl(c.getLogoUrl())
                .verified(c.isVerified())
                .activeJobsCount(activeJobs)
                .createdAt(c.getCreatedAt())
                .build();
    }
}
