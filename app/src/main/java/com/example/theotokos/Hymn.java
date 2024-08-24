package com.example.theotokos;

import android.os.AsyncTask;

import java.util.List;

public class Hymn{
    private String arabicContent;
    private String copticContent;
    private String copticArabicContent;
    private String title;

    public Hymn() {
        // Default constructor required for Firebase Firestore
    }

    public Hymn(String copticArabicContent, String arabicContent, String copticContent, String title) {
        this.arabicContent = arabicContent;
        this.copticContent = copticContent;
        this.copticArabicContent = copticArabicContent;
        this.title = title;
    }

    public String getArabicContent() {
        return arabicContent;
    }

    public void setArabicContent(String arabicContent) {
        this.arabicContent = arabicContent;
    }

    public String getCopticArabicContent() {
        return copticArabicContent;
    }

    public void setCopticArabicContent(String copticArabicContent) {
        this.copticArabicContent = copticArabicContent;
    }

    public String getCopticContent() {
        return copticContent;
    }

    public void setCopticContent(String copticContent) {
        this.copticContent = copticContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
