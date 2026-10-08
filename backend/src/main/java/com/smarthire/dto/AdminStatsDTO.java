package com.smarthire.dto;

public class AdminStatsDTO {
    private long totalUsers;
    private long totalCandidates;
    private long totalRecruiters;
    private long totalCompanies;
    private long pendingCompanies;
    private long verifiedCompanies;
    private long activeJobs;
    private long totalApplications;
    private long shortlistedApplications;
    private long scheduledInterviews;
    private long selectedCandidates;

    public AdminStatsDTO() {}

    public AdminStatsDTO(long totalUsers, long totalCandidates, long totalRecruiters, long totalCompanies, long pendingCompanies, long verifiedCompanies, long activeJobs, long totalApplications, long shortlistedApplications, long scheduledInterviews, long selectedCandidates) {
        this.totalUsers = totalUsers;
        this.totalCandidates = totalCandidates;
        this.totalRecruiters = totalRecruiters;
        this.totalCompanies = totalCompanies;
        this.pendingCompanies = pendingCompanies;
        this.verifiedCompanies = verifiedCompanies;
        this.activeJobs = activeJobs;
        this.totalApplications = totalApplications;
        this.shortlistedApplications = shortlistedApplications;
        this.scheduledInterviews = scheduledInterviews;
        this.selectedCandidates = selectedCandidates;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCandidates() {
        return totalCandidates;
    }

    public void setTotalCandidates(long totalCandidates) {
        this.totalCandidates = totalCandidates;
    }

    public long getTotalRecruiters() {
        return totalRecruiters;
    }

    public void setTotalRecruiters(long totalRecruiters) {
        this.totalRecruiters = totalRecruiters;
    }

    public long getTotalCompanies() {
        return totalCompanies;
    }

    public void setTotalCompanies(long totalCompanies) {
        this.totalCompanies = totalCompanies;
    }

    public long getPendingCompanies() {
        return pendingCompanies;
    }

    public void setPendingCompanies(long pendingCompanies) {
        this.pendingCompanies = pendingCompanies;
    }

    public long getVerifiedCompanies() {
        return verifiedCompanies;
    }

    public void setVerifiedCompanies(long verifiedCompanies) {
        this.verifiedCompanies = verifiedCompanies;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long totalUsers;
        private long totalCandidates;
        private long totalRecruiters;
        private long totalCompanies;
        private long pendingCompanies;
        private long verifiedCompanies;
        private long activeJobs;
        private long totalApplications;
        private long shortlistedApplications;
        private long scheduledInterviews;
        private long selectedCandidates;

        public Builder totalUsers(long totalUsers) {
            this.totalUsers = totalUsers;
            return this;
        }
        public Builder totalCandidates(long totalCandidates) {
            this.totalCandidates = totalCandidates;
            return this;
        }
        public Builder totalRecruiters(long totalRecruiters) {
            this.totalRecruiters = totalRecruiters;
            return this;
        }
        public Builder totalCompanies(long totalCompanies) {
            this.totalCompanies = totalCompanies;
            return this;
        }
        public Builder pendingCompanies(long pendingCompanies) {
            this.pendingCompanies = pendingCompanies;
            return this;
        }
        public Builder verifiedCompanies(long verifiedCompanies) {
            this.verifiedCompanies = verifiedCompanies;
            return this;
        }
        public Builder activeJobs(long activeJobs) {
            this.activeJobs = activeJobs;
            return this;
        }
        public Builder totalApplications(long totalApplications) {
            this.totalApplications = totalApplications;
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
        public AdminStatsDTO build() {
            AdminStatsDTO instance = new AdminStatsDTO();
            instance.totalUsers = this.totalUsers;
            instance.totalCandidates = this.totalCandidates;
            instance.totalRecruiters = this.totalRecruiters;
            instance.totalCompanies = this.totalCompanies;
            instance.pendingCompanies = this.pendingCompanies;
            instance.verifiedCompanies = this.verifiedCompanies;
            instance.activeJobs = this.activeJobs;
            instance.totalApplications = this.totalApplications;
            instance.shortlistedApplications = this.shortlistedApplications;
            instance.scheduledInterviews = this.scheduledInterviews;
            instance.selectedCandidates = this.selectedCandidates;
            return instance;
        }
    }

}
