package com.smarthire.dto;

import java.time.LocalDateTime;

public class ResumeDTO {
    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String contentType;
    private Long fileSize;
    private Integer version;
    private boolean isPrimary;
    private LocalDateTime uploadedAt;

    public ResumeDTO() {}

    public ResumeDTO(Long id, String originalFileName, String storedFileName, String contentType, Long fileSize, Integer version, boolean isPrimary, LocalDateTime uploadedAt) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
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
        private String originalFileName;
        private String storedFileName;
        private String contentType;
        private Long fileSize;
        private Integer version;
        private boolean isPrimary;
        private LocalDateTime uploadedAt;

        public Builder id(Long id) {
            this.id = id;
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
        public ResumeDTO build() {
            ResumeDTO instance = new ResumeDTO();
            instance.id = this.id;
            instance.originalFileName = this.originalFileName;
            instance.storedFileName = this.storedFileName;
            instance.contentType = this.contentType;
            instance.fileSize = this.fileSize;
            instance.version = this.version;
            instance.isPrimary = this.isPrimary;
            instance.uploadedAt = this.uploadedAt;
            return instance;
        }
    }

}
