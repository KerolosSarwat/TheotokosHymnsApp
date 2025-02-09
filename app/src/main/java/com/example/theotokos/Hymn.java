package com.example.theotokos;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class Hymn{
    private String arabicContent;
    private String copticContent;
    private String copticArabicContent;
    private String title;
    private List<Integer> ageLevel;

    public Hymn() {
        // Default constructor required for Firebase Firestore
    }

    public Hymn(String copticArabicContent, String arabicContent, String copticContent, String title, List<Integer> ageLevel) {
        this.arabicContent = arabicContent;
        this.copticContent = copticContent;
        this.copticArabicContent = copticArabicContent;
        this.title = title;
        this.ageLevel = ageLevel;
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

    public List<Integer> getAgeLevel() {
        return ageLevel;
    }

    public void setAgeLevel(List<Integer> ageLevel) {
        this.ageLevel = ageLevel;
    }
}
