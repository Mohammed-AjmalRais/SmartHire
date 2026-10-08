package com.smarthire.dto;

public class SkillDTO {
    private Long id;
    private String name;

    public SkillDTO() {}

    public SkillDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public SkillDTO build() {
            SkillDTO instance = new SkillDTO();
            instance.id = this.id;
            instance.name = this.name;
            return instance;
        }
    }

}
