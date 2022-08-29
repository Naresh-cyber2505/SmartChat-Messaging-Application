package com.example.smartchat.Models;

public class Notes {

    private String noteTitle, noteSubTitle, inputNote, noteImage, noteId;
    private String textDateTime;
    private String textURL;
    private String color;

    public Notes() {
    }

    public Notes(String noteTitle, String noteSubTitle, String inputNote, String textDateTime, String noteId, String textURL, String noteImage) {
        this.noteTitle = noteTitle;
        this.noteSubTitle = noteSubTitle;
        this.inputNote = inputNote;
        this.textDateTime = textDateTime;
        this.noteId = noteId;
        this.textURL = textURL;
        this.noteImage = noteImage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextURL() {
        return textURL;
    }

    public void setTextURL(String textURL) {
        this.textURL = textURL;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(String noteImage) {
        this.noteImage = noteImage;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteSubTitle() {
        return noteSubTitle;
    }

    public void setNoteSubTitle(String noteSubTitle) {
        this.noteSubTitle = noteSubTitle;
    }

    public String getInputNote() {
        return inputNote;
    }

    public void setInputNote(String inputNote) {
        this.inputNote = inputNote;
    }

    public String getTextDateTime() {
        return textDateTime;
    }

    public void setTextDateTime(String textDateTime) {
        this.textDateTime = textDateTime;
    }
}
