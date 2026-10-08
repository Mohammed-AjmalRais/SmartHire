package com.smarthire.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "recruiter_profiles")
public class RecruiterProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    private String positionTitle;

    private String phone;

    public RecruiterProfile() {}

    public RecruiterProfile(Long id, User user, Company company, String positionTitle, String phone) {
        this.id = id;
        this.user = user;
        this.company = company;
        this.positionTitle = positionTitle;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private User user;
        private Company company;
        private String positionTitle;
        private String phone;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder user(User user) {
            this.user = user;
            return this;
        }
        public Builder company(Company company) {
            this.company = company;
            return this;
        }
        public Builder positionTitle(String positionTitle) {
            this.positionTitle = positionTitle;
            return this;
        }
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        public RecruiterProfile build() {
            RecruiterProfile instance = new RecruiterProfile();
            instance.id = this.id;
            instance.user = this.user;
            instance.company = this.company;
            instance.positionTitle = this.positionTitle;
            instance.phone = this.phone;
            return instance;
        }
    }

}
