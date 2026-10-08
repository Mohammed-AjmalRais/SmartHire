package com.smarthire.dto;

import java.util.List;
import java.util.Set;

public class CandidateProfileDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String location;
    private String professionalSummary;
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;
    private Integer yearsOfExperience;
    private Set<String> skills;
    private List<EducationDTO> educations;
    private List<WorkExperienceDTO> workExperiences;
    private List<ResumeDTO> resumes;
    private ResumeDTO primaryResume;

    public CandidateProfileDTO() {}

    public CandidateProfileDTO(Long id, Long userId, String fullName, String email, String phone, String location, String professionalSummary, String githubUrl, String linkedinUrl, String portfolioUrl, Integer yearsOfExperience, Set<String> skills, List<EducationDTO> educations, List<WorkExperienceDTO> workExperiences, List<ResumeDTO> resumes, ResumeDTO primaryResume) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.professionalSummary = professionalSummary;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
        this.portfolioUrl = portfolioUrl;
        this.yearsOfExperience = yearsOfExperience;
        this.skills = skills;
        this.educations = educations;
        this.workExperiences = workExperiences;
        this.resumes = resumes;
        this.primaryResume = primaryResume;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfessionalSummary() {
        return professionalSummary;
    }

    public void setProfessionalSummary(String professionalSummary) {
        this.professionalSummary = professionalSummary;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills;
    }

    public List<EducationDTO> getEducations() {
        return educations;
    }

    public void setEducations(List<EducationDTO> educations) {
        this.educations = educations;
    }

    public List<WorkExperienceDTO> getWorkExperiences() {
        return workExperiences;
    }

    public void setWorkExperiences(List<WorkExperienceDTO> workExperiences) {
        this.workExperiences = workExperiences;
    }

    public List<ResumeDTO> getResumes() {
        return resumes;
    }

    public void setResumes(List<ResumeDTO> resumes) {
        this.resumes = resumes;
    }

    public ResumeDTO getPrimaryResume() {
        return primaryResume;
    }

    public void setPrimaryResume(ResumeDTO primaryResume) {
        this.primaryResume = primaryResume;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long userId;
        private String fullName;
        private String email;
        private String phone;
        private String location;
        private String professionalSummary;
        private String githubUrl;
        private String linkedinUrl;
        private String portfolioUrl;
        private Integer yearsOfExperience;
        private Set<String> skills;
        private List<EducationDTO> educations;
        private List<WorkExperienceDTO> workExperiences;
        private List<ResumeDTO> resumes;
        private ResumeDTO primaryResume;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public Builder location(String location) {
            this.location = location;
            return this;
        }
        public Builder professionalSummary(String professionalSummary) {
            this.professionalSummary = professionalSummary;
            return this;
        }
        public Builder githubUrl(String githubUrl) {
            this.githubUrl = githubUrl;
            return this;
        }
        public Builder linkedinUrl(String linkedinUrl) {
            this.linkedinUrl = linkedinUrl;
            return this;
        }
        public Builder portfolioUrl(String portfolioUrl) {
            this.portfolioUrl = portfolioUrl;
            return this;
        }
        public Builder yearsOfExperience(Integer yearsOfExperience) {
            this.yearsOfExperience = yearsOfExperience;
            return this;
        }
        public Builder skills(Set<String> skills) {
            this.skills = skills;
            return this;
        }
        public Builder educations(List<EducationDTO> educations) {
            this.educations = educations;
            return this;
        }
        public Builder workExperiences(List<WorkExperienceDTO> workExperiences) {
            this.workExperiences = workExperiences;
            return this;
        }
        public Builder resumes(List<ResumeDTO> resumes) {
            this.resumes = resumes;
            return this;
        }
        public Builder primaryResume(ResumeDTO primaryResume) {
            this.primaryResume = primaryResume;
            return this;
        }
        public CandidateProfileDTO build() {
            CandidateProfileDTO instance = new CandidateProfileDTO();
            instance.id = this.id;
            instance.userId = this.userId;
            instance.fullName = this.fullName;
            instance.email = this.email;
            instance.phone = this.phone;
            instance.location = this.location;
            instance.professionalSummary = this.professionalSummary;
            instance.githubUrl = this.githubUrl;
            instance.linkedinUrl = this.linkedinUrl;
            instance.portfolioUrl = this.portfolioUrl;
            instance.yearsOfExperience = this.yearsOfExperience;
            instance.skills = this.skills;
            instance.educations = this.educations;
            instance.workExperiences = this.workExperiences;
            instance.resumes = this.resumes;
            instance.primaryResume = this.primaryResume;
            return instance;
        }
    }

}
