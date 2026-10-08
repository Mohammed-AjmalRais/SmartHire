package com.smarthire.dto;

import com.smarthire.entity.EmploymentType;
import com.smarthire.entity.JobStatus;
import com.smarthire.entity.WorkMode;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public class CreateJobRequest {

    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 150, message = "Job title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 20, message = "Description must be at least 20 characters long")
    private String description;

    private Set<String> requiredSkills;

    @Min(value = 0, message = "Minimum experience cannot be negative")
    private Integer minExperience;

    @Min(value = 0, message = "Maximum experience cannot be negative")
    private Integer maxExperience;

    private Double minSalary;

    private Double maxSalary;

    private String currency = "INR";

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @NotNull(message = "Work mode is required")
    private WorkMode workMode;

    @NotNull(message = "Openings is required")
    @Min(value = 1, message = "Number of openings must be at least 1")
    private Integer openings;

    @NotNull(message = "Application deadline is required")
    @FutureOrPresent(message = "Application deadline must be today or in the future")
    private LocalDate applicationDeadline;

    private JobStatus status = JobStatus.PUBLISHED;

    public CreateJobRequest() {}

    public CreateJobRequest(String title, String description, Set<String> requiredSkills, Integer minExperience, Integer maxExperience, Double minSalary, Double maxSalary, String currency, String location, EmploymentType employmentType, WorkMode workMode, Integer openings, LocalDate applicationDeadline, JobStatus status) {
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
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
        public CreateJobRequest build() {
            CreateJobRequest instance = new CreateJobRequest();
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
            return instance;
        }
    }

}
