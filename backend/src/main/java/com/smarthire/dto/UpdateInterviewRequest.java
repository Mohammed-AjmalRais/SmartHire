package com.smarthire.dto;

import com.smarthire.entity.InterviewStatus;
import com.smarthire.entity.InterviewType;
import java.time.LocalDate;
import java.time.LocalTime;

public class UpdateInterviewRequest {
    private InterviewType interviewType;
    private LocalDate interviewDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String meetingLink;
    private String location;
    private String notes;
    private InterviewStatus status;

    public UpdateInterviewRequest() {}

    public UpdateInterviewRequest(InterviewType interviewType, LocalDate interviewDate, LocalTime startTime, LocalTime endTime, String meetingLink, String location, String notes, InterviewStatus status) {
        this.interviewType = interviewType;
        this.interviewDate = interviewDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetingLink = meetingLink;
        this.location = location;
        this.notes = notes;
        this.status = status;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private InterviewType interviewType;
        private LocalDate interviewDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String meetingLink;
        private String location;
        private String notes;
        private InterviewStatus status;

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
        public UpdateInterviewRequest build() {
            UpdateInterviewRequest instance = new UpdateInterviewRequest();
            instance.interviewType = this.interviewType;
            instance.interviewDate = this.interviewDate;
            instance.startTime = this.startTime;
            instance.endTime = this.endTime;
            instance.meetingLink = this.meetingLink;
            instance.location = this.location;
            instance.notes = this.notes;
            instance.status = this.status;
            return instance;
        }
    }

}
