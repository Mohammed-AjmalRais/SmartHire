package com.smarthire.dto;

import jakarta.validation.constraints.NotBlank;
public class CreateCompanyRequest {

    @NotBlank(message = "Company name is required")
    private String name;

    private String description;

    private String website;

    private String industry;

    private String location;

    private String logoUrl;

    public CreateCompanyRequest() {}

    public CreateCompanyRequest(String name, String description, String website, String industry, String location, String logoUrl) {
        this.name = name;
        this.description = description;
        this.website = website;
        this.industry = industry;
        this.location = location;
        this.logoUrl = logoUrl;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private String website;
        private String industry;
        private String location;
        private String logoUrl;

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
        public CreateCompanyRequest build() {
            CreateCompanyRequest instance = new CreateCompanyRequest();
            instance.name = this.name;
            instance.description = this.description;
            instance.website = this.website;
            instance.industry = this.industry;
            instance.location = this.location;
            instance.logoUrl = this.logoUrl;
            return instance;
        }
    }

}
