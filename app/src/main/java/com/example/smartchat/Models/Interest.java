package com.example.smartchat.Models;

public class Interest {
    private String interest, id;
    private String textDateTime;

    public Interest() {
    }

    public Interest(String interest, String id) {
        this.interest = interest;
        this.id = id;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
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
