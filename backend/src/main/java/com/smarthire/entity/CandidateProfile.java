package com.smarthire.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "candidate_profiles")
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String phone;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String professionalSummary;

    private String githubUrl;

    private String linkedinUrl;

    private String portfolioUrl;

    private Integer yearsOfExperience;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "candidate_skills",
            joinColumns = @JoinColumn(name = "candidate_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkExperience> workExperiences = new ArrayList<>();

    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>();

    public CandidateProfile() {}

    public CandidateProfile(Long id, User user, String phone, String location, String professionalSummary, String githubUrl, String linkedinUrl, String portfolioUrl, Integer yearsOfExperience, Set<Skill> skills, List<Education> educations, List<WorkExperience> workExperiences, List<Resume> resumes) {
        this.id = id;
        this.user = user;
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
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public List<WorkExperience> getWorkExperiences() {
        return workExperiences;
    }

    public void setWorkExperiences(List<WorkExperience> workExperiences) {
        this.workExperiences = workExperiences;
    }

    public List<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(List<Resume> resumes) {
        this.resumes = resumes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private User user;
        private String phone;
        private String location;
        private String professionalSummary;
        private String githubUrl;
        private String linkedinUrl;
        private String portfolioUrl;
        private Integer yearsOfExperience;
        private Set<Skill> skills;
        private List<Education> educations;
        private List<WorkExperience> workExperiences;
        private List<Resume> resumes;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder user(User user) {
            this.user = user;
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
        public Builder skills(Set<Skill> skills) {
            this.skills = skills;
            return this;
        }
        public Builder educations(List<Education> educations) {
            this.educations = educations;
            return this;
        }
        public Builder workExperiences(List<WorkExperience> workExperiences) {
            this.workExperiences = workExperiences;
            return this;
        }
        public Builder resumes(List<Resume> resumes) {
            this.resumes = resumes;
            return this;
        }
        public CandidateProfile build() {
            CandidateProfile instance = new CandidateProfile();
            instance.id = this.id;
            instance.user = this.user;
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
            return instance;
        }
    }

}
