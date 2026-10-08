package com.smarthire.dto;

public class ApplyJobRequest {
    private Long resumeId; // optional if candidate has primary resume
    private String coverLetter;

    public ApplyJobRequest() {}

    public ApplyJobRequest(Long resumeId, String coverLetter) {
        this.resumeId = resumeId;
        this.coverLetter = coverLetter;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long resumeId;
        private String coverLetter;

        public Builder resumeId(Long resumeId) {
            this.resumeId = resumeId;
            return this;
        }
        public Builder coverLetter(String coverLetter) {
            this.coverLetter = coverLetter;
            return this;
        }
        public ApplyJobRequest build() {
            ApplyJobRequest instance = new ApplyJobRequest();
            instance.resumeId = this.resumeId;
            instance.coverLetter = this.coverLetter;
            return instance;
        }
    }

}
