package com.smarthire.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class WorkExperienceDTO {
    private Long id;
    @NotBlank(message = "Company name is required")
    private String companyName;
    @NotBlank(message = "Job title is required")
    private String jobTitle;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrent;
    private String description;

    public WorkExperienceDTO() {}

    public WorkExperienceDTO(Long id, String companyName, String jobTitle, String location, LocalDate startDate, LocalDate endDate, boolean isCurrent, String description) {
        this.id = id;
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
        public WorkExperienceDTO build() {
            WorkExperienceDTO instance = new WorkExperienceDTO();
            instance.id = this.id;
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
