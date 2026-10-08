package com.smarthire.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"candidate_id", "job_id"})
})
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(columnDefinition = "TEXT")
    private String recruiterNotes;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime appliedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Application() {}

    public Application(Long id, User candidate, Job job, Resume resume, String coverLetter, ApplicationStatus status, String recruiterNotes, String rejectionReason, LocalDateTime appliedAt, LocalDateTime updatedAt) {
        this.id = id;
        this.candidate = candidate;
        this.job = job;
        this.resume = resume;
        this.coverLetter = coverLetter;
        this.status = status;
        this.recruiterNotes = recruiterNotes;
        this.rejectionReason = rejectionReason;
        this.appliedAt = appliedAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCandidate() {
        return candidate;
    }

    public void setCandidate(User candidate) {
        this.candidate = candidate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getRecruiterNotes() {
        return recruiterNotes;
    }

    public void setRecruiterNotes(String recruiterNotes) {
        this.recruiterNotes = recruiterNotes;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
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
        private User candidate;
        private Job job;
        private Resume resume;
        private String coverLetter;
        private ApplicationStatus status;
        private String recruiterNotes;
        private String rejectionReason;
        private LocalDateTime appliedAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder candidate(User candidate) {
            this.candidate = candidate;
            return this;
        }
        public Builder job(Job job) {
            this.job = job;
            return this;
        }
        public Builder resume(Resume resume) {
            this.resume = resume;
            return this;
        }
        public Builder coverLetter(String coverLetter) {
            this.coverLetter = coverLetter;
            return this;
        }
        public Builder status(ApplicationStatus status) {
            this.status = status;
            return this;
        }
        public Builder recruiterNotes(String recruiterNotes) {
            this.recruiterNotes = recruiterNotes;
            return this;
        }
        public Builder rejectionReason(String rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }
        public Builder appliedAt(LocalDateTime appliedAt) {
            this.appliedAt = appliedAt;
            return this;
        }
        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public Application build() {
            Application instance = new Application();
            instance.id = this.id;
            instance.candidate = this.candidate;
            instance.job = this.job;
            instance.resume = this.resume;
            instance.coverLetter = this.coverLetter;
            instance.status = this.status;
            instance.recruiterNotes = this.recruiterNotes;
            instance.rejectionReason = this.rejectionReason;
            instance.appliedAt = this.appliedAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }

}
