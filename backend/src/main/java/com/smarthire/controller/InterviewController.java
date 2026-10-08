package com.smarthire.controller;

import com.smarthire.dto.ApiResponse;
import com.smarthire.dto.InterviewDTO;
import com.smarthire.dto.ScheduleInterviewRequest;
import com.smarthire.dto.UpdateInterviewRequest;
import com.smarthire.entity.Role;
import com.smarthire.entity.User;
import com.smarthire.service.InterviewService;
import com.smarthire.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@Tag(name = "Interviews", description = "Endpoints for scheduling and managing candidate interviews")
public class InterviewController {

    private final InterviewService interviewService;
    private final SecurityUtils securityUtils;

    public InterviewController(InterviewService interviewService, SecurityUtils securityUtils) {
        this.interviewService = interviewService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_RECRUITER')")
    @Operation(summary = "Schedule interview with automatic conflict detection")
    public ResponseEntity<ApiResponse<InterviewDTO>> scheduleInterview(
            @Valid @RequestBody ScheduleInterviewRequest request) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        InterviewDTO interview = interviewService.scheduleInterview(user, request);
        return new ResponseEntity<>(ApiResponse.success(interview, "Interview scheduled successfully"), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_RECRUITER')")
    @Operation(summary = "Update interview schedule or outcome")
    public ResponseEntity<ApiResponse<InterviewDTO>> updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInterviewRequest request) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        InterviewDTO interview = interviewService.updateInterview(id, user, request);
        return ResponseEntity.ok(ApiResponse.success(interview, "Interview updated successfully"));
    }

    @GetMapping
    @Operation(summary = "Get interviews for authenticated candidate or recruiter")
    public ResponseEntity<ApiResponse<List<InterviewDTO>>> getInterviews() {
        User user = securityUtils.getCurrentAuthenticatedUser();
        List<InterviewDTO> interviews;
        if (user.getRole() == Role.ROLE_CANDIDATE) {
            interviews = interviewService.getCandidateInterviews(user);
        } else if (user.getRole() == Role.ROLE_RECRUITER) {
            interviews = interviewService.getRecruiterInterviews(user);
        } else {
            interviews = List.of();
        }
        return ResponseEntity.ok(ApiResponse.success(interviews));
    }
}
