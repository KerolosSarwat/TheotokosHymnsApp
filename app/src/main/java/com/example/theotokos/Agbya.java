package com.example.theotokos;

import java.io.Serializable;
import java.util.List;

public class Agbya implements Serializable {
    private String title;
    private String content;
    private String description;
    private List<Integer> ageLevel;
    private int term;

    public Agbya() {

    }

    public Agbya(String title, String content, String description, List<Integer> ageLevel, int term) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.ageLevel = ageLevel;
        this.term = term;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<Integer> getAgeLevel() {
        return ageLevel;
    }

    public void setAgeLevel(List<Integer> ageLevel) {
        this.ageLevel = ageLevel;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }
}
