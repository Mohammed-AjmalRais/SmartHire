package com.smarthire.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "work_experiences")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_profile_id", nullable = false)
    private CandidateProfile candidateProfile;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobTitle;

    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean isCurrent = false;

    @Column(columnDefinition = "TEXT")
    private String description;

    public WorkExperience() {}

    public WorkExperience(Long id, CandidateProfile candidateProfile, String companyName, String jobTitle, String location, LocalDate startDate, LocalDate endDate, boolean isCurrent, String description) {
        this.id = id;
        this.candidateProfile = candidateProfile;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrent = isCurrent;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CandidateProfile getCandidateProfile() {
        return candidateProfile;
    }

    public void setCandidateProfile(CandidateProfile candidateProfile) {
        this.candidateProfile = candidateProfile;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public boolean getIsCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private CandidateProfile candidateProfile;
        private String companyName;
        private String jobTitle;
        private String location;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean isCurrent;
        private String description;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder candidateProfile(CandidateProfile candidateProfile) {
            this.candidateProfile = candidateProfile;
            return this;
        }
        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }
        public Builder jobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }
        public Builder location(String location) {
            this.location = location;
            return this;
        }
        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }
        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }
        public Builder isCurrent(boolean isCurrent) {
            this.isCurrent = isCurrent;
            return this;
        }
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        public WorkExperience build() {
            WorkExperience instance = new WorkExperience();
            instance.id = this.id;
            instance.candidateProfile = this.candidateProfile;
            instance.companyName = this.companyName;
            instance.jobTitle = this.jobTitle;
            instance.location = this.location;
            instance.startDate = this.startDate;
            instance.endDate = this.endDate;
            instance.isCurrent = this.isCurrent;
            instance.description = this.description;
            return instance;
        }
    }

}
