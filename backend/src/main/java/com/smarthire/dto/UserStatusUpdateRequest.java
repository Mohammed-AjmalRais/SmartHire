package com.smarthire.dto;

import jakarta.validation.constraints.NotNull;

public class UserStatusUpdateRequest {
    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;

    public UserStatusUpdateRequest() {}

    public UserStatusUpdateRequest(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean enabled;

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserStatusUpdateRequest build() {
            return new UserStatusUpdateRequest(enabled);
        }
    }
}
