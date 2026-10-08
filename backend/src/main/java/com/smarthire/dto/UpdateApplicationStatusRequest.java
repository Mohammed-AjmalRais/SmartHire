package com.smarthire.dto;

import com.smarthire.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
public class UpdateApplicationStatusRequest {

    @NotNull(message = "New application status is required")
    private ApplicationStatus status;

    private String recruiterNotes;

    private String rejectionReason;

    public UpdateApplicationStatusRequest() {}

    public UpdateApplicationStatusRequest(ApplicationStatus status, String recruiterNotes, String rejectionReason) {
        this.status = status;
        this.recruiterNotes = recruiterNotes;
        this.rejectionReason = rejectionReason;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ApplicationStatus status;
        private String recruiterNotes;
        private String rejectionReason;

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
        public UpdateApplicationStatusRequest build() {
            UpdateApplicationStatusRequest instance = new UpdateApplicationStatusRequest();
            instance.status = this.status;
            instance.recruiterNotes = this.recruiterNotes;
            instance.rejectionReason = this.rejectionReason;
            return instance;
        }
    }

}
