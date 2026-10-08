package com.smarthire.service;

import com.smarthire.dto.*;
import com.smarthire.entity.*;
import com.smarthire.exception.ResourceNotFoundException;
import com.smarthire.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CandidateService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final EducationRepository educationRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final ResumeRepository resumeRepository;

    public CandidateService(CandidateProfileRepository candidateProfileRepository, UserRepository userRepository, SkillRepository skillRepository, EducationRepository educationRepository, WorkExperienceRepository workExperienceRepository, ResumeRepository resumeRepository) {
        this.candidateProfileRepository = candidateProfileRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.educationRepository = educationRepository;
        this.workExperienceRepository = workExperienceRepository;
        this.resumeRepository = resumeRepository;
    }

    @Transactional(readOnly = true)
    public CandidateProfileDTO getProfileByUserId(Long userId) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found for user: " + userId));

        return mapToDTO(profile);
    }

    @Transactional
    public CandidateProfileDTO updateProfile(Long userId, UpdateCandidateProfileRequest request) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found for user: " + userId));

        User user = profile.getUser();
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
            userRepository.save(user);
        }

        profile.setPhone(request.getPhone());
        profile.setLocation(request.getLocation());
        profile.setProfessionalSummary(request.getProfessionalSummary());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setPortfolioUrl(request.getPortfolioUrl());
        profile.setYearsOfExperience(request.getYearsOfExperience());

        // Update skills
        if (request.getSkills() != null) {
            Set<Skill> skillEntities = new HashSet<>();
            for (String skillName : request.getSkills()) {
                if (!skillName.isBlank()) {
                    Skill skill = skillRepository.findByNameIgnoreCase(skillName.trim())
                            .orElseGet(() -> skillRepository.save(Skill.builder().name(skillName.trim()).build()));
                    skillEntities.add(skill);
                }
            }
            profile.setSkills(skillEntities);
        }

        // Update education
        if (request.getEducations() != null) {
            profile.getEducations().clear();
            for (EducationDTO eduDTO : request.getEducations()) {
                Education education = Education.builder()
                        .candidateProfile(profile)
                        .institution(eduDTO.getInstitution())
                        .degree(eduDTO.getDegree())
                        .fieldOfStudy(eduDTO.getFieldOfStudy())
                        .startDate(eduDTO.getStartDate())
                        .endDate(eduDTO.getEndDate())
                        .grade(eduDTO.getGrade())
                        .build();
                profile.getEducations().add(education);
            }
        }

        // Update work experience
        if (request.getWorkExperiences() != null) {
            profile.getWorkExperiences().clear();
            for (WorkExperienceDTO weDTO : request.getWorkExperiences()) {
                WorkExperience exp = WorkExperience.builder()
                        .candidateProfile(profile)
                        .companyName(weDTO.getCompanyName())
                        .jobTitle(weDTO.getJobTitle())
                        .location(weDTO.getLocation())
                        .startDate(weDTO.getStartDate())
                        .endDate(weDTO.getEndDate())
                        .isCurrent(weDTO.isCurrent())
                        .description(weDTO.getDescription())
                        .build();
                profile.getWorkExperiences().add(exp);
            }
        }

        CandidateProfile saved = candidateProfileRepository.save(profile);
        return mapToDTO(saved);
    }

    public CandidateProfile getCandidateProfileEntity(Long userId) {
        return candidateProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found for user: " + userId));
    }

    private CandidateProfileDTO mapToDTO(CandidateProfile profile) {
        Set<String> skills = profile.getSkills() != null ?
                profile.getSkills().stream().map(Skill::getName).collect(Collectors.toSet()) :
                Collections.emptySet();

        List<EducationDTO> educations = profile.getEducations() != null ?
                profile.getEducations().stream().map(e -> EducationDTO.builder()
                        .id(e.getId())
                        .institution(e.getInstitution())
                        .degree(e.getDegree())
                        .fieldOfStudy(e.getFieldOfStudy())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .grade(e.getGrade())
                        .build()).collect(Collectors.toList()) :
                Collections.emptyList();

        List<WorkExperienceDTO> workExperiences = profile.getWorkExperiences() != null ?
                profile.getWorkExperiences().stream().map(w -> WorkExperienceDTO.builder()
                        .id(w.getId())
                        .companyName(w.getCompanyName())
                        .jobTitle(w.getJobTitle())
                        .location(w.getLocation())
                        .startDate(w.getStartDate())
                        .endDate(w.getEndDate())
                        .isCurrent(w.isCurrent())
                        .description(w.getDescription())
                        .build()).collect(Collectors.toList()) :
                Collections.emptyList();

        List<ResumeDTO> resumes = profile.getResumes() != null ?
                profile.getResumes().stream().map(r -> ResumeDTO.builder()
                        .id(r.getId())
                        .originalFileName(r.getOriginalFileName())
                        .storedFileName(r.getStoredFileName())
                        .contentType(r.getContentType())
                        .fileSize(r.getFileSize())
                        .version(r.getVersion())
                        .isPrimary(r.isPrimary())
                        .uploadedAt(r.getUploadedAt())
                        .build()).collect(Collectors.toList()) :
                Collections.emptyList();

        ResumeDTO primaryResume = resumes.stream()
                .filter(ResumeDTO::isPrimary)
                .findFirst()
                .orElse(resumes.isEmpty() ? null : resumes.get(0));

        return CandidateProfileDTO.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .fullName(profile.getUser().getFullName())
                .email(profile.getUser().getEmail())
                .phone(profile.getPhone())
                .location(profile.getLocation())
                .professionalSummary(profile.getProfessionalSummary())
                .githubUrl(profile.getGithubUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .portfolioUrl(profile.getPortfolioUrl())
                .yearsOfExperience(profile.getYearsOfExperience())
                .skills(skills)
                .educations(educations)
                .workExperiences(workExperiences)
                .resumes(resumes)
                .primaryResume(primaryResume)
                .build();
    }
}
