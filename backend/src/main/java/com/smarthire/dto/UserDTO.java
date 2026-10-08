package com.smarthire.dto;

import com.smarthire.entity.Role;
import java.time.LocalDateTime;

public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private Role role;
    private boolean enabled;
    private Long companyId;
    private String companyName;
    private Boolean companyVerified;
    private LocalDateTime createdAt;

    public UserDTO() {}

    public UserDTO(Long id, String email, String fullName, Role role, boolean enabled, Long companyId, String companyName, Boolean companyVerified, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.enabled = enabled;
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyVerified = companyVerified;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public Boolean isCompanyVerified() {
        return companyVerified;
    }

    public void setCompanyVerified(Boolean companyVerified) {
        this.companyVerified = companyVerified;
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
        private String email;
        private String fullName;
        private Role role;
        private boolean enabled;
        private Long companyId;
        private String companyName;
        private Boolean companyVerified;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public Builder role(Role role) {
            this.role = role;
            return this;
        }
        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
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
        public Builder companyVerified(Boolean companyVerified) {
            this.companyVerified = companyVerified;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public UserDTO build() {
            UserDTO instance = new UserDTO();
            instance.id = this.id;
            instance.email = this.email;
            instance.fullName = this.fullName;
            instance.role = this.role;
            instance.enabled = this.enabled;
            instance.companyId = this.companyId;
            instance.companyName = this.companyName;
            instance.companyVerified = this.companyVerified;
            instance.createdAt = this.createdAt;
            return instance;
        }
    }

}
