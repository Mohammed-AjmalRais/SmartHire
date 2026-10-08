package com.smarthire.controller;

import com.smarthire.dto.ApiResponse;
import com.smarthire.dto.ResumeDTO;
import com.smarthire.entity.Resume;
import com.smarthire.entity.User;
import com.smarthire.service.ResumeService;
import com.smarthire.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@Tag(name = "Resumes", description = "Endpoints for resume management and authorized download")
public class ResumeController {

    private final ResumeService resumeService;
    private final SecurityUtils securityUtils;

    public ResumeController(ResumeService resumeService, SecurityUtils securityUtils) {
        this.resumeService = resumeService;
        this.securityUtils = securityUtils;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
    @Operation(summary = "Upload candidate resume (PDF/DOCX max 10MB)")
    public ResponseEntity<ApiResponse<ResumeDTO>> uploadResume(@RequestParam("file") MultipartFile file) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        ResumeDTO resumeDTO = resumeService.uploadResume(user, file);
        return new ResponseEntity<>(ApiResponse.success(resumeDTO, "Resume uploaded successfully"), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
    @Operation(summary = "Get candidate resume versions")
    public ResponseEntity<ApiResponse<List<ResumeDTO>>> getMyResumes() {
        User user = securityUtils.getCurrentAuthenticatedUser();
        List<ResumeDTO> resumes = resumeService.getCandidateResumes(user);
        return ResponseEntity.ok(ApiResponse.success(resumes));
    }

    @PutMapping("/{id}/primary")
    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
    @Operation(summary = "Mark a resume version as primary")
    public ResponseEntity<ApiResponse<ResumeDTO>> setPrimaryResume(@PathVariable Long id) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        ResumeDTO updated = resumeService.setPrimaryResume(id, user);
        return ResponseEntity.ok(ApiResponse.success(updated, "Primary resume updated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
    @Operation(summary = "Delete a resume version")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable Long id) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        resumeService.deleteResume(id, user);
        return ResponseEntity.ok(ApiResponse.success(null, "Resume deleted successfully"));
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Download resume file with business authorization checks")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long id) {
        User user = securityUtils.getCurrentAuthenticatedUser();
        Resource resource = resumeService.getResumeFileForDownload(id, user);
        Resume resume = resumeService.getResumeEntity(id);

        String contentType = resume.getContentType() != null ? resume.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getOriginalFileName() + "\"")
                .body(resource);
    }
}
