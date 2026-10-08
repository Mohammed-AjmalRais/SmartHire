package com.smarthire.dto;

public class RecruiterStatsDTO {
    private long activeJobs;
    private long totalApplications;
    private long underReviewApplications;
    private long shortlistedApplications;
    private long scheduledInterviews;
    private long selectedCandidates;
    private long rejectedCandidates;

    public RecruiterStatsDTO() {}

    public RecruiterStatsDTO(long activeJobs, long totalApplications, long underReviewApplications, long shortlistedApplications, long scheduledInterviews, long selectedCandidates, long rejectedCandidates) {
        this.activeJobs = activeJobs;
        this.totalApplications = totalApplications;
        this.underReviewApplications = underReviewApplications;
        this.shortlistedApplications = shortlistedApplications;
        this.scheduledInterviews = scheduledInterviews;
        this.selectedCandidates = selectedCandidates;
        this.rejectedCandidates = rejectedCandidates;
    }

    public long getActiveJobs() {
        return activeJobs;
    }

    public void setActiveJobs(long activeJobs) {
        this.activeJobs = activeJobs;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getUnderReviewApplications() {
        return underReviewApplications;
    }

    public void setUnderReviewApplications(long underReviewApplications) {
        this.underReviewApplications = underReviewApplications;
    }

    public long getShortlistedApplications() {
        return shortlistedApplications;
    }

    public void setShortlistedApplications(long shortlistedApplications) {
        this.shortlistedApplications = shortlistedApplications;
    }

    public long getScheduledInterviews() {
        return scheduledInterviews;
    }

    public void setScheduledInterviews(long scheduledInterviews) {
        this.scheduledInterviews = scheduledInterviews;
    }

    public long getSelectedCandidates() {
        return selectedCandidates;
    }

    public void setSelectedCandidates(long selectedCandidates) {
        this.selectedCandidates = selectedCandidates;
    }

    public long getRejectedCandidates() {
        return rejectedCandidates;
    }

    public void setRejectedCandidates(long rejectedCandidates) {
        this.rejectedCandidates = rejectedCandidates;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long activeJobs;
        private long totalApplications;
        private long underReviewApplications;
        private long shortlistedApplications;
        private long scheduledInterviews;
        private long selectedCandidates;
        private long rejectedCandidates;

        public Builder activeJobs(long activeJobs) {
            this.activeJobs = activeJobs;
            return this;
        }
        public Builder totalApplications(long totalApplications) {
            this.totalApplications = totalApplications;
            return this;
        }
        public Builder underReviewApplications(long underReviewApplications) {
            this.underReviewApplications = underReviewApplications;
            return this;
        }
        public Builder shortlistedApplications(long shortlistedApplications) {
            this.shortlistedApplications = shortlistedApplications;
            return this;
        }
        public Builder scheduledInterviews(long scheduledInterviews) {
            this.scheduledInterviews = scheduledInterviews;
            return this;
        }
        public Builder selectedCandidates(long selectedCandidates) {
            this.selectedCandidates = selectedCandidates;
            return this;
        }
        public Builder rejectedCandidates(long rejectedCandidates) {
            this.rejectedCandidates = rejectedCandidates;
            return this;
        }
        public RecruiterStatsDTO build() {
            RecruiterStatsDTO instance = new RecruiterStatsDTO();
            instance.activeJobs = this.activeJobs;
            instance.totalApplications = this.totalApplications;
            instance.underReviewApplications = this.underReviewApplications;
            instance.shortlistedApplications = this.shortlistedApplications;
            instance.scheduledInterviews = this.scheduledInterviews;
            instance.selectedCandidates = this.selectedCandidates;
            instance.rejectedCandidates = this.rejectedCandidates;
            return instance;
        }
    }

}
