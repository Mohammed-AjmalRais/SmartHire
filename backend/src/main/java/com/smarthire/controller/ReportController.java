package com.smarthire.controller;

import com.smarthire.entity.Application;
import com.smarthire.entity.CandidateProfile;
import com.smarthire.entity.Role;
import com.smarthire.entity.User;
import com.smarthire.exception.UnauthorizedAccessException;
import com.smarthire.service.ApplicationService;
import com.smarthire.service.CandidateService;
import com.smarthire.service.PdfReportService;
import com.smarthire.service.RecruiterService;
import com.smarthire.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Endpoints for generating downloadable PDF recruitment reports")
public class ReportController {

    private final PdfReportService pdfReportService;
    private final ApplicationService applicationService;
    private final CandidateService candidateService;
    private final RecruiterService recruiterService;
    private final SecurityUtils securityUtils;

    public ReportController(PdfReportService pdfReportService, ApplicationService applicationService, CandidateService candidateService, RecruiterService recruiterService, SecurityUtils securityUtils) {
        this.pdfReportService = pdfReportService;
        this.applicationService = applicationService;
        this.candidateService = candidateService;
        this.recruiterService = recruiterService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/application/{id}/pdf")
    @Operation(summary = "Generate and download application summary PDF")
    public ResponseEntity<byte[]> getApplicationSummaryPdf(@PathVariable Long id) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        Application application = applicationService.getApplicationEntity(id);

        // Authorization check
        if (user.getRole() == Role.ROLE_CANDIDATE) {
            if (!application.getCandidate().getId().equals(user.getId())) {
                throw new UnauthorizedAccessException("Not authorized to download summary for this application");
            }
        } else if (user.getRole() == Role.ROLE_RECRUITER) {
            recruiterService.verifyRecruiterBelongsToCompany(user, application.getJob().getCompany());
        }

        byte[] pdf = pdfReportService.generateApplicationSummaryPdf(application);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"application_" + id + "_summary.pdf\"")
                .body(pdf);
    }

    @GetMapping("/candidate/{userId}/pdf")
    @Operation(summary = "Generate and download candidate profile summary PDF")
    public ResponseEntity<byte[]> getCandidateProfilePdf(@PathVariable Long userId) {
        User user = securityUtils.getCurrentAuthenticatedUser();

        if (user.getRole() == Role.ROLE_CANDIDATE && !user.getId().equals(userId)) {
            throw new UnauthorizedAccessException("Not authorized to download another candidate's profile PDF");
        }

        CandidateProfile profile = candidateService.getCandidateProfileEntity(userId);
        byte[] pdf = pdfReportService.generateCandidateProfilePdf(profile);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"candidate_" + userId + "_profile.pdf\"")
                .body(pdf);
    }
}
