package com.smarthire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewer_id", nullable = false)
    private User interviewer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewType interviewType;

    @NotNull
    @Column(nullable = false)
    private LocalDate interviewDate;

    @NotNull
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(nullable = false)
    private LocalTime endTime;

    private String meetingLink;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status = InterviewStatus.SCHEDULED;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Interview() {}

    public Interview(Long id, Application application, User interviewer, InterviewType interviewType, LocalDate interviewDate, LocalTime startTime, LocalTime endTime, String meetingLink, String location, String notes, InterviewStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.application = application;
        this.interviewer = interviewer;
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

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public User getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(User interviewer) {
        this.interviewer = interviewer;
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
        private Application application;
        private User interviewer;
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
        public Builder application(Application application) {
            this.application = application;
            return this;
        }
        public Builder interviewer(User interviewer) {
            this.interviewer = interviewer;
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
        public Interview build() {
            Interview instance = new Interview();
            instance.id = this.id;
            instance.application = this.application;
            instance.interviewer = this.interviewer;
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
