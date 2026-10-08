package com.smarthire.dto;

import com.smarthire.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    // Optional initial recruiter company name
    private String companyName;

    public RegisterRequest() {}

    public RegisterRequest(String fullName, String email, String password, Role role, String companyName) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.companyName = companyName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String fullName;
        private String email;
        private String password;
        private Role role;
        private String companyName;

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        public Builder role(Role role) {
            this.role = role;
            return this;
        }
        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }
        public RegisterRequest build() {
            RegisterRequest instance = new RegisterRequest();
            instance.fullName = this.fullName;
            instance.email = this.email;
            instance.password = this.password;
            instance.role = this.role;
            instance.companyName = this.companyName;
            return instance;
        }
    }

}
