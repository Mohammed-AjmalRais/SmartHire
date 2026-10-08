package com.smarthire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiter;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> requiredSkills = new HashSet<>();

    @Min(0)
    private Integer minExperience;

    @Min(0)
    private Integer maxExperience;

    private Double minSalary;

    private Double maxSalary;

    private String currency = "INR";

    @NotBlank
    private String location;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkMode workMode;

    @Min(1)
    private Integer openings = 1;

    @NotNull
    @Column(nullable = false)
    private LocalDate applicationDeadline;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.PUBLISHED;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Job() {}

    public Job(Long id, Company company, User recruiter, String title, String description, Set<Skill> requiredSkills, Integer minExperience, Integer maxExperience, Double minSalary, Double maxSalary, String currency, String location, EmploymentType employmentType, WorkMode workMode, Integer openings, LocalDate applicationDeadline, JobStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.company = company;
        this.recruiter = recruiter;
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(User recruiter) {
        this.recruiter = recruiter;
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

    public Set<Skill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Set<Skill> requiredSkills) {
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
        private Company company;
        private User recruiter;
        private String title;
        private String description;
        private Set<Skill> requiredSkills;
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
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder company(Company company) {
            this.company = company;
            return this;
        }
        public Builder recruiter(User recruiter) {
            this.recruiter = recruiter;
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
        public Builder requiredSkills(Set<Skill> requiredSkills) {
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
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public Job build() {
            Job instance = new Job();
            instance.id = this.id;
            instance.company = this.company;
            instance.recruiter = this.recruiter;
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
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }

}
