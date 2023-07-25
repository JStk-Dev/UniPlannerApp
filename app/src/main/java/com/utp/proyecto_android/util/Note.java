package com.utp.proyecto_android.util;

public class Note {

    private String idNote;
    private String titleNote;
    private String contentNote;

    public Note() {
    }

    public Note(String titleNote, String contentNote) {
        this.titleNote = titleNote;
        this.contentNote = contentNote;
    }

    public String getIdNote() {
        return idNote;
    }

    public void setIdNote(String idNote) {
        this.idNote = idNote;
    }

    public String getTitleNote() {
        return titleNote;
    }

    public void setTitleNote(String titleNote) {
        this.titleNote = titleNote;
    }

    public String getContentNote() {
        return contentNote;
    }

    public void setContentNote(String contentNote) {
        this.contentNote = contentNote;
    }
}
