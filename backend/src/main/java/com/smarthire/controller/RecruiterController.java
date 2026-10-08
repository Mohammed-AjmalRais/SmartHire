package com.smarthire.controller;

import com.smarthire.dto.*;
import com.smarthire.entity.User;
import com.smarthire.service.ApplicationService;
import com.smarthire.service.JobService;
import com.smarthire.service.RecruiterService;
import com.smarthire.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
@Tag(name = "Recruiter", description = "Endpoints for recruiters to manage company, jobs, and candidates")
public class RecruiterController {

    private final RecruiterService recruiterService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final SecurityUtils securityUtils;

    public RecruiterController(RecruiterService recruiterService, JobService jobService, ApplicationService applicationService, SecurityUtils securityUtils) {
        this.recruiterService = recruiterService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/company")
    @Operation(summary = "Get recruiter's company profile")
    public ResponseEntity<ApiResponse<CompanyDTO>> getCompany() {
        User user = securityUtils.getCurrentAuthenticatedUser();
        CompanyDTO company = recruiterService.getMyCompany(user);
        return ResponseEntity.ok(ApiResponse.success(company));
    }

    @PostMapping("/company")
    @Operation(summary = "Create or update recruiter's company profile")
    public ResponseEntity<ApiResponse<CompanyDTO>> createOrUpdateCompany(
            @Valid @RequestBody CreateCompanyRequest request) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        CompanyDTO company = recruiterService.createOrUpdateCompany(user, request);
        return ResponseEntity.ok(ApiResponse.success(company, "Company profile updated successfully"));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get recruiter dashboard metrics")
    public ResponseEntity<ApiResponse<RecruiterStatsDTO>> getRecruiterStats() {
        User user = securityUtils.getCurrentAuthenticatedUser();
        RecruiterStatsDTO stats = recruiterService.getRecruiterDashboardStats(user);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/jobs")
    @Operation(summary = "Get all jobs posted for recruiter's company")
    public ResponseEntity<ApiResponse<List<JobDTO>>> getCompanyJobs() {
        User user = securityUtils.getCurrentAuthenticatedUser();
        List<JobDTO> jobs = jobService.getCompanyJobs(user);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @PostMapping("/jobs")
    @Operation(summary = "Publish a new job vacancy (requires verified company)")
    public ResponseEntity<ApiResponse<JobDTO>> createJob(@Valid @RequestBody CreateJobRequest request) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        JobDTO job = jobService.createJob(user, request);
        return new ResponseEntity<>(ApiResponse.success(job, "Job vacancy published successfully"), HttpStatus.CREATED);
    }

    @PutMapping("/jobs/{id}")
    @Operation(summary = "Update an existing job vacancy")
    public ResponseEntity<ApiResponse<JobDTO>> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody CreateJobRequest request) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        JobDTO job = jobService.updateJob(id, user, request);
        return ResponseEntity.ok(ApiResponse.success(job, "Job updated successfully"));
    }

    @PutMapping("/jobs/{id}/close")
    @Operation(summary = "Close a job vacancy")
    public ResponseEntity<ApiResponse<JobDTO>> closeJob(@PathVariable Long id) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        JobDTO job = jobService.closeJob(id, user);
        return ResponseEntity.ok(ApiResponse.success(job, "Job closed successfully"));
    }

    @GetMapping("/applications")
    @Operation(summary = "Get all applications submitted to recruiter's company")
    public ResponseEntity<ApiResponse<List<ApplicationDTO>>> getCompanyApplications() {
        User user = securityUtils.getCurrentAuthenticatedUser();
        List<ApplicationDTO> applications = applicationService.getCompanyApplications(user);
        return ResponseEntity.ok(ApiResponse.success(applications));
    }

    @GetMapping("/jobs/{jobId}/applications")
    @Operation(summary = "Get all applications for a specific job vacancy")
    public ResponseEntity<ApiResponse<List<ApplicationDTO>>> getJobApplications(@PathVariable Long jobId) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        List<ApplicationDTO> applications = applicationService.getJobApplications(jobId, user);
        return ResponseEntity.ok(ApiResponse.success(applications));
    }

    @PutMapping("/applications/{id}/status")
    @Operation(summary = "Advance or update recruitment status for an application")
    public ResponseEntity<ApiResponse<ApplicationDTO>> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationStatusRequest request) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        ApplicationDTO application = applicationService.updateStatus(id, user, request);
        return ResponseEntity.ok(ApiResponse.success(application, "Application status transitioned to " + request.getStatus()));
    }
}
