package com.smarthire.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smarthire.dto.ResumeDTO;
import com.smarthire.entity.*;
import com.smarthire.exception.ResourceNotFoundException;
import com.smarthire.exception.UnauthorizedAccessException;
import com.smarthire.file.FileStorageService;
import com.smarthire.repository.ApplicationRepository;
import com.smarthire.repository.CandidateProfileRepository;
import com.smarthire.repository.RecruiterProfileRepository;
import com.smarthire.repository.ResumeRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumeService {
    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    private final ResumeRepository resumeRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final ApplicationRepository applicationRepository;
    private final FileStorageService fileStorageService;

    public ResumeService(ResumeRepository resumeRepository, CandidateProfileRepository candidateProfileRepository, RecruiterProfileRepository recruiterProfileRepository, ApplicationRepository applicationRepository, FileStorageService fileStorageService) {
        this.resumeRepository = resumeRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.applicationRepository = applicationRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public ResumeDTO uploadResume(User candidateUser, MultipartFile file) {
        CandidateProfile candidateProfile = candidateProfileRepository.findByUserId(candidateUser.getId())
                .orElseGet(() -> candidateProfileRepository.save(CandidateProfile.builder().user(candidateUser).build()));

        List<Resume> existingResumes = resumeRepository.findByCandidateProfileOrderByUploadedAtDesc(candidateProfile);
        int nextVersion = existingResumes.size() + 1;

        // If this is the first resume or user has none, mark primary
        boolean isPrimary = existingResumes.isEmpty();

        String storedFileName = fileStorageService.storeFile(file);

        Resume resume = Resume.builder()
                .candidateProfile(candidateProfile)
                .originalFileName(file.getOriginalFilename())
                .storedFileName(storedFileName)
                .filePath(storedFileName)
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .version(nextVersion)
                .isPrimary(isPrimary)
                .build();

        Resume saved = resumeRepository.save(resume);
        log.info("Saved resume #{} (v{}) for candidate {}", saved.getId(), saved.getVersion(), candidateUser.getEmail());

        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ResumeDTO> getCandidateResumes(User candidateUser) {
        CandidateProfile candidateProfile = candidateProfileRepository.findByUserId(candidateUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found"));

        return resumeRepository.findByCandidateProfileOrderByUploadedAtDesc(candidateProfile).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResumeDTO setPrimaryResume(Long resumeId, User candidateUser) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found: " + resumeId));

        if (!resume.getCandidateProfile().getUser().getId().equals(candidateUser.getId())) {
            throw new UnauthorizedAccessException("Cannot modify another candidate's resume");
        }

        List<Resume> resumes = resumeRepository.findByCandidateProfileOrderByUploadedAtDesc(resume.getCandidateProfile());
        for (Resume r : resumes) {
            r.setPrimary(r.getId().equals(resumeId));
        }
        resumeRepository.saveAll(resumes);

        resume.setPrimary(true);
        return mapToDTO(resume);
    }

    @Transactional
    public void deleteResume(Long resumeId, User candidateUser) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found: " + resumeId));

        if (!resume.getCandidateProfile().getUser().getId().equals(candidateUser.getId())) {
            throw new UnauthorizedAccessException("Cannot delete another candidate's resume");
        }

        fileStorageService.deleteFile(resume.getStoredFileName());
        resumeRepository.delete(resume);
    }

    @Transactional(readOnly = true)
    public Resource getResumeFileForDownload(Long resumeId, User currentUser) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID: " + resumeId));

        Long candidateUserId = resume.getCandidateProfile().getUser().getId();

        // 1. Candidate viewing their own resume
        if (currentUser.getRole() == Role.ROLE_CANDIDATE) {
            if (!currentUser.getId().equals(candidateUserId)) {
                throw new UnauthorizedAccessException("You are not authorized to view another candidate's resume.");
            }
        }
        // 2. Recruiter viewing resume - verify authorization
        else if (currentUser.getRole() == Role.ROLE_RECRUITER) {
            RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(currentUser.getId())
                    .orElseThrow(() -> new UnauthorizedAccessException("Recruiter profile not found"));

            Company company = recruiterProfile.getCompany();
            if (company == null) {
                throw new UnauthorizedAccessException("Recruiter is not associated with any company.");
            }

            // Verify whether candidate has applied to any job of this company
            boolean hasApplied = applicationRepository.existsByCandidateIdAndJobCompanyId(candidateUserId, company.getId());
            if (!hasApplied) {
                log.warn("Unauthorized attempt: Recruiter {} attempted to download resume of candidate {} who has not applied to company {}",
                        currentUser.getEmail(), candidateUserId, company.getName());
                throw new UnauthorizedAccessException("Recruiters are only authorized to access resumes of candidates who have applied to vacancies at their company.");
            }
        }
        // 3. Admin has unrestricted audit permission

        return fileStorageService.loadFileAsResource(resume.getStoredFileName());
    }

    @Transactional(readOnly = true)
    public Resume getResumeEntity(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found: " + resumeId));
    }

    public ResumeDTO mapToDTO(Resume r) {
        return ResumeDTO.builder()
                .id(r.getId())
                .originalFileName(r.getOriginalFileName())
                .storedFileName(r.getStoredFileName())
                .contentType(r.getContentType())
                .fileSize(r.getFileSize())
                .version(r.getVersion())
                .isPrimary(r.isPrimary())
                .uploadedAt(r.getUploadedAt())
                .build();
    }
}
