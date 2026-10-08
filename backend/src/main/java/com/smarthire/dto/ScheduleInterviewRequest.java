package com.smarthire.dto;

import com.smarthire.entity.InterviewType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleInterviewRequest {

    @NotNull(message = "Application ID is required")
    private Long applicationId;

    @NotNull(message = "Interview type is required")
    private InterviewType interviewType;

    @NotNull(message = "Interview date is required")
    @FutureOrPresent(message = "Interview date cannot be in the past")
    private LocalDate interviewDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    private String meetingLink;

    private String location;

    private String notes;

    public ScheduleInterviewRequest() {}

    public ScheduleInterviewRequest(Long applicationId, InterviewType interviewType, LocalDate interviewDate, LocalTime startTime, LocalTime endTime, String meetingLink, String location, String notes) {
        this.applicationId = applicationId;
        this.interviewType = interviewType;
        this.interviewDate = interviewDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetingLink = meetingLink;
        this.location = location;
        this.notes = notes;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long applicationId;
        private InterviewType interviewType;
        private LocalDate interviewDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String meetingLink;
        private String location;
        private String notes;

        public Builder applicationId(Long applicationId) {
            this.applicationId = applicationId;
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
        public ScheduleInterviewRequest build() {
            ScheduleInterviewRequest instance = new ScheduleInterviewRequest();
            instance.applicationId = this.applicationId;
            instance.interviewType = this.interviewType;
            instance.interviewDate = this.interviewDate;
            instance.startTime = this.startTime;
            instance.endTime = this.endTime;
            instance.meetingLink = this.meetingLink;
            instance.location = this.location;
            instance.notes = this.notes;
            return instance;
        }
    }

}
