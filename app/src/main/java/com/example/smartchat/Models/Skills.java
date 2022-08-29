package com.example.smartchat.Models;

public class Skills {
    private String skill, id;
    private String textDateTime;

    public Skills() {
    }

    public Skills(String skill, String id) {
        this.skill = skill;
        this.id = id;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTextDateTime() {
        return textDateTime;
    }

    public void setTextDateTime(String textDateTime) {
        this.textDateTime = textDateTime;
    }
}
