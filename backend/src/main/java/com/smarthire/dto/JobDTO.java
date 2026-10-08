package com.smarthire.dto;

import com.smarthire.entity.EmploymentType;
import com.smarthire.entity.JobStatus;
import com.smarthire.entity.WorkMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class JobDTO {
    private Long id;
    private Long companyId;
    private String companyName;
    private String companyLogoUrl;
    private String companyIndustry;
    private boolean companyVerified;
    private Long recruiterId;
    private String recruiterName;
    private String title;
    private String description;
    private Set<String> requiredSkills;
    private Integer minExperience;
    private Integer maxExperience;
    private Double minSalary;
    private Double maxSalary;
    private String currency;
    private String location;
    private EmploymentType employmentType;
    private WorkMode workMode;
    private Integer openings;
    private LocalDate applicationDeadline;
    private JobStatus status;
    private long totalApplicationsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobDTO() {}

    public JobDTO(Long id, Long companyId, String companyName, String companyLogoUrl, String companyIndustry, boolean companyVerified, Long recruiterId, String recruiterName, String title, String description, Set<String> requiredSkills, Integer minExperience, Integer maxExperience, Double minSalary, Double maxSalary, String currency, String location, EmploymentType employmentType, WorkMode workMode, Integer openings, LocalDate applicationDeadline, JobStatus status, long totalApplicationsCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyLogoUrl = companyLogoUrl;
        this.companyIndustry = companyIndustry;
        this.companyVerified = companyVerified;
        this.recruiterId = recruiterId;
        this.recruiterName = recruiterName;
        this.title = title;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.minExperience = minExperience;
        this.maxExperience = maxExperience;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.currency = currency;
        this.location = location;
        this.employmentType = employmentType;
        this.workMode = workMode;
        this.openings = openings;
        this.applicationDeadline = applicationDeadline;
        this.status = status;
        this.totalApplicationsCount = totalApplicationsCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public String getCompanyIndustry() {
        return companyIndustry;
    }

    public void setCompanyIndustry(String companyIndustry) {
        this.companyIndustry = companyIndustry;
    }

    public boolean isCompanyVerified() {
        return companyVerified;
    }

    public void setCompanyVerified(boolean companyVerified) {
        this.companyVerified = companyVerified;
    }

    public Long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Set<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public Integer getMinExperience() {
        return minExperience;
    }

    public void setMinExperience(Integer minExperience) {
        this.minExperience = minExperience;
    }

    public Integer getMaxExperience() {
        return maxExperience;
    }

    public void setMaxExperience(Integer maxExperience) {
        this.maxExperience = maxExperience;
    }

    public Double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public Double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public WorkMode getWorkMode() {
        return workMode;
    }

    public void setWorkMode(WorkMode workMode) {
        this.workMode = workMode;
    }

    public Integer getOpenings() {
        return openings;
    }

    public void setOpenings(Integer openings) {
        this.openings = openings;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public long getTotalApplicationsCount() {
        return totalApplicationsCount;
    }

    public void setTotalApplicationsCount(long totalApplicationsCount) {
        this.totalApplicationsCount = totalApplicationsCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long companyId;
        private String companyName;
        private String companyLogoUrl;
        private String companyIndustry;
        private boolean companyVerified;
        private Long recruiterId;
        private String recruiterName;
        private String title;
        private String description;
        private Set<String> requiredSkills;
        private Integer minExperience;
        private Integer maxExperience;
        private Double minSalary;
        private Double maxSalary;
        private String currency;
        private String location;
        private EmploymentType employmentType;
        private WorkMode workMode;
        private Integer openings;
        private LocalDate applicationDeadline;
        private JobStatus status;
        private long totalApplicationsCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder companyId(Long companyId) {
            this.companyId = companyId;
            return this;
        }
        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }
        public Builder companyLogoUrl(String companyLogoUrl) {
            this.companyLogoUrl = companyLogoUrl;
            return this;
        }
        public Builder companyIndustry(String companyIndustry) {
            this.companyIndustry = companyIndustry;
            return this;
        }
        public Builder companyVerified(boolean companyVerified) {
            this.companyVerified = companyVerified;
            return this;
        }
        public Builder recruiterId(Long recruiterId) {
            this.recruiterId = recruiterId;
            return this;
        }
        public Builder recruiterName(String recruiterName) {
            this.recruiterName = recruiterName;
            return this;
        }
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        public Builder requiredSkills(Set<String> requiredSkills) {
            this.requiredSkills = requiredSkills;
            return this;
        }
        public Builder minExperience(Integer minExperience) {
            this.minExperience = minExperience;
            return this;
        }
        public Builder maxExperience(Integer maxExperience) {
            this.maxExperience = maxExperience;
            return this;
        }
        public Builder minSalary(Double minSalary) {
            this.minSalary = minSalary;
            return this;
        }
        public Builder maxSalary(Double maxSalary) {
            this.maxSalary = maxSalary;
            return this;
        }
        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }
        public Builder location(String location) {
            this.location = location;
            return this;
        }
        public Builder employmentType(EmploymentType employmentType) {
            this.employmentType = employmentType;
            return this;
        }
        public Builder workMode(WorkMode workMode) {
            this.workMode = workMode;
            return this;
        }
        public Builder openings(Integer openings) {
            this.openings = openings;
            return this;
        }
        public Builder applicationDeadline(LocalDate applicationDeadline) {
            this.applicationDeadline = applicationDeadline;
            return this;
        }
        public Builder status(JobStatus status) {
            this.status = status;
            return this;
        }
        public Builder totalApplicationsCount(long totalApplicationsCount) {
            this.totalApplicationsCount = totalApplicationsCount;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public JobDTO build() {
            JobDTO instance = new JobDTO();
            instance.id = this.id;
            instance.companyId = this.companyId;
            instance.companyName = this.companyName;
            instance.companyLogoUrl = this.companyLogoUrl;
            instance.companyIndustry = this.companyIndustry;
            instance.companyVerified = this.companyVerified;
            instance.recruiterId = this.recruiterId;
            instance.recruiterName = this.recruiterName;
            instance.title = this.title;
            instance.description = this.description;
            instance.requiredSkills = this.requiredSkills;
            instance.minExperience = this.minExperience;
            instance.maxExperience = this.maxExperience;
            instance.minSalary = this.minSalary;
            instance.maxSalary = this.maxSalary;
            instance.currency = this.currency;
            instance.location = this.location;
            instance.employmentType = this.employmentType;
            instance.workMode = this.workMode;
            instance.openings = this.openings;
            instance.applicationDeadline = this.applicationDeadline;
            instance.status = this.status;
            instance.totalApplicationsCount = this.totalApplicationsCount;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }

}
