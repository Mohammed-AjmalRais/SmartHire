package com.smarthire.controller;

import com.smarthire.dto.*;
import com.smarthire.entity.User;
import com.smarthire.service.ApplicationService;
import com.smarthire.service.CandidateService;
import com.smarthire.service.InterviewService;
import com.smarthire.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Candidate", description = "Endpoints for job seekers to manage profile and applications")
public class CandidateController {

    private final CandidateService candidateService;
    private final ApplicationService applicationService;
    private final InterviewService interviewService;
    private final SecurityUtils securityUtils;

    public CandidateController(CandidateService candidateService, ApplicationService applicationService, InterviewService interviewService, SecurityUtils securityUtils) {
        this.candidateService = candidateService;
        this.applicationService = applicationService;
        this.interviewService = interviewService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/api/candidate/profile")
    @Operation(summary = "Get candidate professional profile")
    public ResponseEntity<ApiResponse<CandidateProfileDTO>> getProfile() {
        User user = securityUtils.getCurrentAuthenticatedUser();
        CandidateProfileDTO profile = candidateService.getProfileByUserId(user.getId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/api/candidate/profile")
    @Operation(summary = "Update candidate professional profile")
    public ResponseEntity<ApiResponse<CandidateProfileDTO>> updateProfile(
            @Valid @RequestBody UpdateCandidateProfileRequest request) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        CandidateProfileDTO updated = candidateService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Profile updated successfully"));
    }

    @PostMapping({"/api/candidate/jobs/{jobId}/apply", "/api/jobs/{jobId}/apply"})
    @Operation(summary = "Apply for a job vacancy")
    public ResponseEntity<ApiResponse<ApplicationDTO>> applyForJob(
            @PathVariable Long jobId,
            @RequestBody(required = false) ApplyJobRequest request) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        ApplicationDTO application = applicationService.applyForJob(jobId, user, request);
        return new ResponseEntity<>(ApiResponse.success(application, "Application submitted successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/api/candidate/applications")
    @Operation(summary = "Get all applications submitted by candidate")
    public ResponseEntity<ApiResponse<List<ApplicationDTO>>> getMyApplications() {
        User user = securityUtils.getCurrentAuthenticatedUser();
        List<ApplicationDTO> applications = applicationService.getCandidateApplications(user);
        return ResponseEntity.ok(ApiResponse.success(applications));
    }

    @GetMapping("/api/candidate/applications/{id}")
    @Operation(summary = "Get specific application status and interview details")
    public ResponseEntity<ApiResponse<ApplicationDTO>> getApplicationById(@PathVariable Long id) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        ApplicationDTO application = applicationService.getApplicationById(id, user);
        return ResponseEntity.ok(ApiResponse.success(application));
    }

    @PutMapping("/api/candidate/applications/{id}/withdraw")
    @Operation(summary = "Withdraw an active application")
    public ResponseEntity<ApiResponse<ApplicationDTO>> withdrawApplication(@PathVariable Long id) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        ApplicationDTO application = applicationService.withdrawApplication(id, user);
        return ResponseEntity.ok(ApiResponse.success(application, "Application withdrawn successfully"));
    }

    @GetMapping("/api/candidate/interviews")
    @Operation(summary = "Get all interviews scheduled for candidate")
    public ResponseEntity<ApiResponse<List<InterviewDTO>>> getMyInterviews() {
        User user = securityUtils.getCurrentAuthenticatedUser();
        List<InterviewDTO> interviews = interviewService.getCandidateInterviews(user);
        return ResponseEntity.ok(ApiResponse.success(interviews));
    }
}
