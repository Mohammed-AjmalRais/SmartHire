package com.smarthire.controller;

import com.smarthire.dto.ApiResponse;
import com.smarthire.dto.JobDTO;
import com.smarthire.dto.JobFilterCriteria;
import com.smarthire.dto.PageResponse;
import com.smarthire.entity.EmploymentType;
import com.smarthire.entity.JobStatus;
import com.smarthire.entity.WorkMode;
import com.smarthire.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Jobs", description = "Endpoints for discovering and viewing job opportunities")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    @Operation(summary = "Search and filter job vacancies with pagination")
    public ResponseEntity<ApiResponse<PageResponse<JobDTO>>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer experience,
            @RequestParam(required = false) EmploymentType employmentType,
            @RequestParam(required = false) WorkMode workMode,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        JobFilterCriteria criteria = JobFilterCriteria.builder()
                .keyword(keyword)
                .location(location)
                .experience(experience)
                .employmentType(employmentType)
                .workMode(workMode)
                .minSalary(minSalary)
                .maxSalary(maxSalary)
                .skill(skill)
                .companyId(companyId)
                .status(JobStatus.PUBLISHED)
                .build();

        PageResponse<JobDTO> response = jobService.searchJobs(criteria, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get detailed information about a job vacancy")
    public ResponseEntity<ApiResponse<JobDTO>> getJobById(@PathVariable Long id) {
        JobDTO job = jobService.getJobById(id);
        return ResponseEntity.ok(ApiResponse.success(job));
    }
}
