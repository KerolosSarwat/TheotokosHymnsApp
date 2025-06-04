package com.example.theotokos;

import androidx.annotation.Keep;

@Keep
public class Coptic {
    private String arabicWord, copticLetterImage, title;

    public Coptic() {
    }

    public String getArabicWord() {
        return arabicWord;
    }

    public void setArabicWord(String arabicWord) {
        this.arabicWord = arabicWord;
    }

    public String getCopticLetterImage() {
        return copticLetterImage;
    }

    public void setCopticLetterImage(String copticLetterImage) {
        this.copticLetterImage = copticLetterImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
