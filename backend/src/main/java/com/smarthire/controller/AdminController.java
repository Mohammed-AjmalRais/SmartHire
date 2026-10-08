package com.smarthire.controller;

import com.smarthire.dto.*;
import com.smarthire.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Admin", description = "Endpoints for platform administrators")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    @Operation(summary = "Get overall platform analytics and metrics")
    public ResponseEntity<ApiResponse<AdminStatsDTO>> getStats() {
        AdminStatsDTO stats = adminService.getPlatformStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/companies")
    @Operation(summary = "Get all registered companies (optional filter by verification status)")
    public ResponseEntity<ApiResponse<List<CompanyDTO>>> getCompanies(
            @RequestParam(required = false) Boolean verified) {
        List<CompanyDTO> companies = adminService.getAllCompanies(verified);
        return ResponseEntity.ok(ApiResponse.success(companies));
    }

    @PutMapping("/companies/{id}/verify")
    @Operation(summary = "Verify or reject a company registration")
    public ResponseEntity<ApiResponse<CompanyDTO>> verifyCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyVerificationRequest request) {
        CompanyDTO company = adminService.verifyCompany(id, request);
        String action = request.getVerified() ? "approved" : "rejected";
        return ResponseEntity.ok(ApiResponse.success(company, "Company successfully " + action));
    }

    @GetMapping("/users")
    @Operation(summary = "Get all registered users across platform")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getUsers() {
        List<UserDTO> users = adminService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PutMapping("/users/{id}/status")
    @Operation(summary = "Activate or deactivate a user account")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UserStatusUpdateRequest request) {
        UserDTO user = adminService.toggleUserStatus(id, request.getEnabled());
        String status = request.getEnabled() ? "activated" : "deactivated";
        return ResponseEntity.ok(ApiResponse.success(user, "User account successfully " + status));
    }
}
