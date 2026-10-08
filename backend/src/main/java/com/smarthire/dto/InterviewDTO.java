package com.smarthire.dto;

import com.smarthire.entity.InterviewStatus;
import com.smarthire.entity.InterviewType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class InterviewDTO {
    private Long id;
    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private Long candidateId;
    private String candidateName;
    private Long interviewerId;
    private String interviewerName;
    private InterviewType interviewType;
    private LocalDate interviewDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String meetingLink;
    private String location;
    private String notes;
    private InterviewStatus status;
    private LocalDateTime createdAt;

    public InterviewDTO() {}

    public InterviewDTO(Long id, Long applicationId, Long jobId, String jobTitle, String companyName, Long candidateId, String candidateName, Long interviewerId, String interviewerName, InterviewType interviewType, LocalDate interviewDate, LocalTime startTime, LocalTime endTime, String meetingLink, String location, String notes, InterviewStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.interviewerId = interviewerId;
        this.interviewerName = interviewerName;
        this.interviewType = interviewType;
        this.interviewDate = interviewDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetingLink = meetingLink;
        this.location = location;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public Long getInterviewerId() {
        return interviewerId;
    }

    public void setInterviewerId(Long interviewerId) {
        this.interviewerId = interviewerId;
    }

    public String getInterviewerName() {
        return interviewerName;
    }

    public void setInterviewerName(String interviewerName) {
        this.interviewerName = interviewerName;
    }

    public InterviewType getInterviewType() {
        return interviewType;
    }

    public void setInterviewType(InterviewType interviewType) {
        this.interviewType = interviewType;
    }

    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDate interviewDate) {
        this.interviewDate = interviewDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public void setStatus(InterviewStatus status) {
        this.status = status;
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
        private Long applicationId;
        private Long jobId;
        private String jobTitle;
        private String companyName;
        private Long candidateId;
        private String candidateName;
        private Long interviewerId;
        private String interviewerName;
        private InterviewType interviewType;
        private LocalDate interviewDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String meetingLink;
        private String location;
        private String notes;
        private InterviewStatus status;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder applicationId(Long applicationId) {
            this.applicationId = applicationId;
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
        public Builder companyName(String companyName) {
            this.companyName = companyName;
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
        public Builder interviewerId(Long interviewerId) {
            this.interviewerId = interviewerId;
            return this;
        }
        public Builder interviewerName(String interviewerName) {
            this.interviewerName = interviewerName;
            return this;
        }
        public Builder interviewType(InterviewType interviewType) {
            this.interviewType = interviewType;
            return this;
        }
        public Builder interviewDate(LocalDate interviewDate) {
            this.interviewDate = interviewDate;
            return this;
        }
        public Builder startTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }
        public Builder endTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }
        public Builder meetingLink(String meetingLink) {
            this.meetingLink = meetingLink;
            return this;
        }
        public Builder location(String location) {
            this.location = location;
            return this;
        }
        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }
        public Builder status(InterviewStatus status) {
            this.status = status;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public InterviewDTO build() {
            InterviewDTO instance = new InterviewDTO();
            instance.id = this.id;
            instance.applicationId = this.applicationId;
            instance.jobId = this.jobId;
            instance.jobTitle = this.jobTitle;
            instance.companyName = this.companyName;
            instance.candidateId = this.candidateId;
            instance.candidateName = this.candidateName;
            instance.interviewerId = this.interviewerId;
            instance.interviewerName = this.interviewerName;
            instance.interviewType = this.interviewType;
            instance.interviewDate = this.interviewDate;
            instance.startTime = this.startTime;
            instance.endTime = this.endTime;
            instance.meetingLink = this.meetingLink;
            instance.location = this.location;
            instance.notes = this.notes;
            instance.status = this.status;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }

}
