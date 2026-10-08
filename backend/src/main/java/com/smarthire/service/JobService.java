package com.smarthire.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smarthire.dto.CreateJobRequest;
import com.smarthire.dto.JobDTO;
import com.smarthire.dto.JobFilterCriteria;
import com.smarthire.dto.PageResponse;
import com.smarthire.entity.*;
import com.smarthire.exception.BadRequestException;
import com.smarthire.exception.JobNotFoundException;
import com.smarthire.exception.UnauthorizedAccessException;
import com.smarthire.repository.ApplicationRepository;
import com.smarthire.repository.JobRepository;
import com.smarthire.repository.SkillRepository;
import com.smarthire.specification.JobSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobService {
    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final RecruiterService recruiterService;
    private final ApplicationRepository applicationRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository, RecruiterService recruiterService, ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.recruiterService = recruiterService;
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public JobDTO createJob(User recruiterUser, CreateJobRequest request) {
        Company company = recruiterService.getRecruiterCompany(recruiterUser);

        // Enforce business rule: Only verified companies can publish jobs
        if (!company.isVerified()) {
            throw new BadRequestException("Your company (" + company.getName() + ") is pending administrative verification. You can only publish jobs once an admin approves your company.");
        }

        // Validate min/max salary and experience
        if (request.getMinSalary() != null && request.getMaxSalary() != null && request.getMinSalary() > request.getMaxSalary()) {
            throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
        }

        if (request.getMinExperience() != null && request.getMaxExperience() != null && request.getMinExperience() > request.getMaxExperience()) {
            throw new BadRequestException("Minimum experience cannot be greater than maximum experience");
        }

        Set<Skill> skills = new HashSet<>();
        if (request.getRequiredSkills() != null) {
            for (String skillName : request.getRequiredSkills()) {
                if (!skillName.isBlank()) {
                    Skill skill = skillRepository.findByNameIgnoreCase(skillName.trim())
                            .orElseGet(() -> skillRepository.save(Skill.builder().name(skillName.trim()).build()));
                    skills.add(skill);
                }
            }
        }

        Job job = Job.builder()
                .company(company)
                .recruiter(recruiterUser)
                .title(request.getTitle().trim())
                .description(request.getDescription().trim())
                .requiredSkills(skills)
                .minExperience(request.getMinExperience())
                .maxExperience(request.getMaxExperience())
                .minSalary(request.getMinSalary())
                .maxSalary(request.getMaxSalary())
                .currency(request.getCurrency() != null ? request.getCurrency() : "INR")
                .location(request.getLocation().trim())
                .employmentType(request.getEmploymentType())
                .workMode(request.getWorkMode())
                .openings(request.getOpenings())
                .applicationDeadline(request.getApplicationDeadline())
                .status(request.getStatus() != null ? request.getStatus() : JobStatus.PUBLISHED)
                .build();

        Job saved = jobRepository.save(job);
        log.info("Created job #{} '{}' for company '{}'", saved.getId(), saved.getTitle(), company.getName());
        return mapToDTO(saved);
    }

    @Transactional
    public JobDTO updateJob(Long jobId, User recruiterUser, CreateJobRequest request) {
        Job job = getJobEntity(jobId);
        recruiterService.verifyRecruiterBelongsToCompany(recruiterUser, job.getCompany());

        if (request.getMinSalary() != null && request.getMaxSalary() != null && request.getMinSalary() > request.getMaxSalary()) {
            throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
        }

        if (request.getMinExperience() != null && request.getMaxExperience() != null && request.getMinExperience() > request.getMaxExperience()) {
            throw new BadRequestException("Minimum experience cannot be greater than maximum experience");
        }

        job.setTitle(request.getTitle().trim());
        job.setDescription(request.getDescription().trim());
        job.setMinExperience(request.getMinExperience());
        job.setMaxExperience(request.getMaxExperience());
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());
        job.setLocation(request.getLocation().trim());
        job.setEmploymentType(request.getEmploymentType());
        job.setWorkMode(request.getWorkMode());
        job.setOpenings(request.getOpenings());
        job.setApplicationDeadline(request.getApplicationDeadline());
        if (request.getStatus() != null) {
            job.setStatus(request.getStatus());
        }

        if (request.getRequiredSkills() != null) {
            Set<Skill> skills = new HashSet<>();
            for (String skillName : request.getRequiredSkills()) {
                if (!skillName.isBlank()) {
                    Skill skill = skillRepository.findByNameIgnoreCase(skillName.trim())
                            .orElseGet(() -> skillRepository.save(Skill.builder().name(skillName.trim()).build()));
                    skills.add(skill);
                }
            }
            job.setRequiredSkills(skills);
        }

        Job updated = jobRepository.save(job);
        return mapToDTO(updated);
    }

    @Transactional
    public JobDTO closeJob(Long jobId, User recruiterUser) {
        Job job = getJobEntity(jobId);
        recruiterService.verifyRecruiterBelongsToCompany(recruiterUser, job.getCompany());

        job.setStatus(JobStatus.CLOSED);
        Job saved = jobRepository.save(job);
        log.info("Job #{} closed by recruiter {}", jobId, recruiterUser.getEmail());
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public JobDTO getJobById(Long jobId) {
        Job job = getJobEntity(jobId);
        return mapToDTO(job);
    }

    @Transactional(readOnly = true)
    public PageResponse<JobDTO> searchJobs(JobFilterCriteria criteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Job> jobPage = jobRepository.findAll(JobSpecification.filterBy(criteria), pageable);

        List<JobDTO> dtoList = jobPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return PageResponse.<JobDTO>builder()
                .content(dtoList)
                .pageNumber(jobPage.getNumber())
                .pageSize(jobPage.getSize())
                .totalElements(jobPage.getTotalElements())
                .totalPages(jobPage.getTotalPages())
                .last(jobPage.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public List<JobDTO> getCompanyJobs(User recruiterUser) {
        Company company = recruiterService.getRecruiterCompany(recruiterUser);
        return jobRepository.findByCompany(company).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Job getJobEntity(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job vacancy not found with ID: " + jobId));
    }

    public JobDTO mapToDTO(Job j) {
        Set<String> skills = j.getRequiredSkills() != null ?
                j.getRequiredSkills().stream().map(Skill::getName).collect(Collectors.toSet()) :
                Set.of();

        long applicationCount = applicationRepository.findByJob(j).size();

        return JobDTO.builder()
                .id(j.getId())
                .companyId(j.getCompany().getId())
                .companyName(j.getCompany().getName())
                .companyLogoUrl(j.getCompany().getLogoUrl())
                .companyIndustry(j.getCompany().getIndustry())
                .companyVerified(j.getCompany().isVerified())
                .recruiterId(j.getRecruiter().getId())
                .recruiterName(j.getRecruiter().getFullName())
                .title(j.getTitle())
                .description(j.getDescription())
                .requiredSkills(skills)
                .minExperience(j.getMinExperience())
                .maxExperience(j.getMaxExperience())
                .minSalary(j.getMinSalary())
                .maxSalary(j.getMaxSalary())
                .currency(j.getCurrency())
                .location(j.getLocation())
                .employmentType(j.getEmploymentType())
                .workMode(j.getWorkMode())
                .openings(j.getOpenings())
                .applicationDeadline(j.getApplicationDeadline())
                .status(j.getStatus())
                .totalApplicationsCount(applicationCount)
                .createdAt(j.getCreatedAt())
                .updatedAt(j.getUpdatedAt())
                .build();
    }
}
