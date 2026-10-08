package com.smarthire.dto;

import java.time.LocalDateTime;

public class CompanyDTO {
    private Long id;
    private String name;
    private String description;
    private String website;
    private String industry;
    private String location;
    private String logoUrl;
    private boolean verified;
    private long activeJobsCount;
    private LocalDateTime createdAt;

    public CompanyDTO() {}

    public CompanyDTO(Long id, String name, String description, String website, String industry, String location, String logoUrl, boolean verified, long activeJobsCount, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.industry = industry;
        this.location = location;
        this.logoUrl = logoUrl;
        this.verified = verified;
        this.activeJobsCount = activeJobsCount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public long getActiveJobsCount() {
        return activeJobsCount;
    }

    public void setActiveJobsCount(long activeJobsCount) {
        this.activeJobsCount = activeJobsCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private String website;
        private String industry;
        private String location;
        private String logoUrl;
        private boolean verified;
        private long activeJobsCount;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        public Builder website(String website) {
            this.website = website;
            return this;
        }
        public Builder industry(String industry) {
            this.industry = industry;
            return this;
        }
        public Builder location(String location) {
            this.location = location;
            return this;
        }
        public Builder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }
        public Builder verified(boolean verified) {
            this.verified = verified;
            return this;
        }
        public Builder activeJobsCount(long activeJobsCount) {
            this.activeJobsCount = activeJobsCount;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public CompanyDTO build() {
            CompanyDTO instance = new CompanyDTO();
            instance.id = this.id;
            instance.name = this.name;
            instance.description = this.description;
            instance.website = this.website;
            instance.industry = this.industry;
            instance.location = this.location;
            instance.logoUrl = this.logoUrl;
            instance.verified = this.verified;
            instance.activeJobsCount = this.activeJobsCount;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }

}
