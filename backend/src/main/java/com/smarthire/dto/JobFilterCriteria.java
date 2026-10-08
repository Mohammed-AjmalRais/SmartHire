package com.smarthire.dto;

import com.smarthire.entity.EmploymentType;
import com.smarthire.entity.JobStatus;
import com.smarthire.entity.WorkMode;
public class JobFilterCriteria {
    private String keyword;
    private String location;
    private Integer experience;
    private EmploymentType employmentType;
    private WorkMode workMode;
    private Double minSalary;
    private Double maxSalary;
    private String skill;
    private Long companyId;
    private JobStatus status;

    public JobFilterCriteria() {}

    public JobFilterCriteria(String keyword, String location, Integer experience, EmploymentType employmentType, WorkMode workMode, Double minSalary, Double maxSalary, String skill, Long companyId, JobStatus status) {
        this.keyword = keyword;
        this.location = location;
        this.experience = experience;
        this.employmentType = employmentType;
        this.workMode = workMode;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.skill = skill;
        this.companyId = companyId;
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
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

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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
        private String keyword;
        private String location;
        private Integer experience;
        private EmploymentType employmentType;
        private WorkMode workMode;
        private Double minSalary;
        private Double maxSalary;
        private String skill;
        private Long companyId;
        private JobStatus status;

        public Builder keyword(String keyword) {
            this.keyword = keyword;
            return this;
        }
        public Builder location(String location) {
            this.location = location;
            return this;
        }
        public Builder experience(Integer experience) {
            this.experience = experience;
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
        public Builder minSalary(Double minSalary) {
            this.minSalary = minSalary;
            return this;
        }
        public Builder maxSalary(Double maxSalary) {
            this.maxSalary = maxSalary;
            return this;
        }
        public Builder skill(String skill) {
            this.skill = skill;
            return this;
        }
        public Builder companyId(Long companyId) {
            this.companyId = companyId;
            return this;
        }
        public Builder status(JobStatus status) {
            this.status = status;
            return this;
        }
        public JobFilterCriteria build() {
            JobFilterCriteria instance = new JobFilterCriteria();
            instance.keyword = this.keyword;
            instance.location = this.location;
            instance.experience = this.experience;
            instance.employmentType = this.employmentType;
            instance.workMode = this.workMode;
            instance.minSalary = this.minSalary;
            instance.maxSalary = this.maxSalary;
            instance.skill = this.skill;
            instance.companyId = this.companyId;
            instance.status = this.status;
            return instance;
        }
    }

}
