package com.smarthire.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class EducationDTO {
    private Long id;
    @NotBlank(message = "Institution is required")
    private String institution;
    @NotBlank(message = "Degree is required")
    private String degree;
    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    private String grade;

    public EducationDTO() {}

    public EducationDTO(Long id, String institution, String degree, String fieldOfStudy, LocalDate startDate, LocalDate endDate, String grade) {
        this.id = id;
        this.institution = institution;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String institution;
        private String degree;
        private String fieldOfStudy;
        private LocalDate startDate;
        private LocalDate endDate;
        private String grade;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder institution(String institution) {
            this.institution = institution;
            return this;
        }
        public Builder degree(String degree) {
            this.degree = degree;
            return this;
        }
        public Builder fieldOfStudy(String fieldOfStudy) {
            this.fieldOfStudy = fieldOfStudy;
            return this;
        }
        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }
        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }
        public Builder grade(String grade) {
            this.grade = grade;
            return this;
        }
        public EducationDTO build() {
            EducationDTO instance = new EducationDTO();
            instance.id = this.id;
            instance.institution = this.institution;
            instance.degree = this.degree;
            instance.fieldOfStudy = this.fieldOfStudy;
            instance.startDate = this.startDate;
            instance.endDate = this.endDate;
            instance.grade = this.grade;
            return instance;
        }
    }

}
