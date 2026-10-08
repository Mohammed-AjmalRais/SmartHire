package com.smarthire.dto;

import com.smarthire.entity.ApplicationStatus;
import java.time.LocalDateTime;
import java.util.List;

public class ApplicationDTO {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private String candidateEmail;
    private String candidatePhone;
    private Long jobId;
    private String jobTitle;
    private Long companyId;
    private String companyName;
    private Long resumeId;
    private String resumeFileName;
    private String coverLetter;
    private ApplicationStatus status;
    private String recruiterNotes;
    private String rejectionReason;
    private List<InterviewDTO> interviews;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    public ApplicationDTO() {}

    public ApplicationDTO(Long id, Long candidateId, String candidateName, String candidateEmail, String candidatePhone, Long jobId, String jobTitle, Long companyId, String companyName, Long resumeId, String resumeFileName, String coverLetter, ApplicationStatus status, String recruiterNotes, String rejectionReason, List<InterviewDTO> interviews, LocalDateTime appliedAt, LocalDateTime updatedAt) {
        this.id = id;
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.candidatePhone = candidatePhone;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyId = companyId;
        this.companyName = companyName;
        this.resumeId = resumeId;
        this.resumeFileName = resumeFileName;
        this.coverLetter = coverLetter;
        this.status = status;
        this.recruiterNotes = recruiterNotes;
        this.rejectionReason = rejectionReason;
        this.interviews = interviews;
        this.appliedAt = appliedAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getCandidatePhone() {
        return candidatePhone;
    }

    public void setCandidatePhone(String candidatePhone) {
        this.candidatePhone = candidatePhone;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
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

    public List<InterviewDTO> getInterviews() {
        return interviews;
    }

    public void setInterviews(List<InterviewDTO> interviews) {
        this.interviews = interviews;
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
        private Long candidateId;
        private String candidateName;
        private String candidateEmail;
        private String candidatePhone;
        private Long jobId;
        private String jobTitle;
        private Long companyId;
        private String companyName;
        private Long resumeId;
        private String resumeFileName;
        private String coverLetter;
        private ApplicationStatus status;
        private String recruiterNotes;
        private String rejectionReason;
        private List<InterviewDTO> interviews;
        private LocalDateTime appliedAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder candidateId(Long candidateId) {
            this.candidateId = candidateId;
            return this;
        }
        public Builder candidateName(String candidateName) {
            this.candidateName = candidateName;
            return this;
        }
        public Builder candidateEmail(String candidateEmail) {
            this.candidateEmail = candidateEmail;
            return this;
        }
        public Builder candidatePhone(String candidatePhone) {
            this.candidatePhone = candidatePhone;
            return this;
        }
        public Builder jobId(Long jobId) {
            this.jobId = jobId;
            return this;
        }
        public Builder jobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }
        public Builder companyId(Long companyId) {
            this.companyId = companyId;
            return this;
        }
        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }
        public Builder resumeId(Long resumeId) {
            this.resumeId = resumeId;
            return this;
        }
        public Builder resumeFileName(String resumeFileName) {
            this.resumeFileName = resumeFileName;
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
        public Builder interviews(List<InterviewDTO> interviews) {
            this.interviews = interviews;
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
        public ApplicationDTO build() {
            ApplicationDTO instance = new ApplicationDTO();
            instance.id = this.id;
            instance.candidateId = this.candidateId;
            instance.candidateName = this.candidateName;
            instance.candidateEmail = this.candidateEmail;
            instance.candidatePhone = this.candidatePhone;
            instance.jobId = this.jobId;
            instance.jobTitle = this.jobTitle;
            instance.companyId = this.companyId;
            instance.companyName = this.companyName;
            instance.resumeId = this.resumeId;
            instance.resumeFileName = this.resumeFileName;
            instance.coverLetter = this.coverLetter;
            instance.status = this.status;
            instance.recruiterNotes = this.recruiterNotes;
            instance.rejectionReason = this.rejectionReason;
            instance.interviews = this.interviews;
            instance.appliedAt = this.appliedAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }

}
