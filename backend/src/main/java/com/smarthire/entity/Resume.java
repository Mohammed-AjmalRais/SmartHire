package com.smarthire.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_profile_id", nullable = false)
    private CandidateProfile candidateProfile;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false, unique = true)
    private String storedFileName;

    @Column(nullable = false)
    private String filePath;

    private String contentType;

    private Long fileSize;

    private Integer version = 1;

    private boolean isPrimary = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime uploadedAt;

    public Resume() {}

    public Resume(Long id, CandidateProfile candidateProfile, String originalFileName, String storedFileName, String filePath, String contentType, Long fileSize, Integer version, boolean isPrimary, LocalDateTime uploadedAt) {
        this.id = id;
        this.candidateProfile = candidateProfile;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.version = version;
        this.isPrimary = isPrimary;
        this.uploadedAt = uploadedAt;
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

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public boolean getIsPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private CandidateProfile candidateProfile;
        private String originalFileName;
        private String storedFileName;
        private String filePath;
        private String contentType;
        private Long fileSize;
        private Integer version;
        private boolean isPrimary;
        private LocalDateTime uploadedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder candidateProfile(CandidateProfile candidateProfile) {
            this.candidateProfile = candidateProfile;
            return this;
        }
        public Builder originalFileName(String originalFileName) {
            this.originalFileName = originalFileName;
            return this;
        }
        public Builder storedFileName(String storedFileName) {
            this.storedFileName = storedFileName;
            return this;
        }
        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }
        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }
        public Builder fileSize(Long fileSize) {
            this.fileSize = fileSize;
            return this;
        }
        public Builder version(Integer version) {
            this.version = version;
            return this;
        }
        public Builder isPrimary(boolean isPrimary) {
            this.isPrimary = isPrimary;
            return this;
        }
        public Builder uploadedAt(LocalDateTime uploadedAt) {
            this.uploadedAt = uploadedAt;
            return this;
        }
        public Resume build() {
            Resume instance = new Resume();
            instance.id = this.id;
            instance.candidateProfile = this.candidateProfile;
            instance.originalFileName = this.originalFileName;
            instance.storedFileName = this.storedFileName;
            instance.filePath = this.filePath;
            instance.contentType = this.contentType;
            instance.fileSize = this.fileSize;
            instance.version = this.version;
            instance.isPrimary = this.isPrimary;
            instance.uploadedAt = this.uploadedAt;
            return instance;
        }
    }

}
