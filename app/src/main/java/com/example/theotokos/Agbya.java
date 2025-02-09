package com.example.theotokos;
import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.List;

@Keep
public class Agbya implements Serializable {

    private List<Integer> ageLevel; // Use Integer for numbers
    private String content;
    private String description;
    private int term; // Use int for whole numbers
    private String title;

    // Constructors (Good Practice)
    public Agbya() {} // Empty constructor for Firebase

    public Agbya(List<Integer> ageLevel, String content, String description, int term, String title) {
        this.ageLevel = ageLevel;
        this.content = content;
        this.description = description;
        this.term = term;
        this.title = title;
    }

    // Getters and Setters (Essential for Firebase)
    public List<Integer> getAgeLevel() {
        return ageLevel;
    }

    public void setAgeLevel(List<Integer> ageLevel) {
        this.ageLevel = ageLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}