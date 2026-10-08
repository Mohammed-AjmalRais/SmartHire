package com.smarthire.dto;

import jakarta.validation.constraints.NotNull;

public class CompanyVerificationRequest {
    @NotNull(message = "Verification status flag is required")
    private Boolean verified;

    public CompanyVerificationRequest() {}

    public CompanyVerificationRequest(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getVerified() {
        return verified;
    }

    public Boolean isVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean verified;

        public Builder verified(Boolean verified) {
            this.verified = verified;
            return this;
        }

        public CompanyVerificationRequest build() {
            return new CompanyVerificationRequest(verified);
        }
    }
}
