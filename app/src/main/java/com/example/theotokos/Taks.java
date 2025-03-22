package com.example.theotokos;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.List;

@Keep
public class Taks implements Serializable, TitleContentHolder {
    private String title;
    private String content;
    private String homework;
    private int term;
    private List<Integer> ageLevel;

    public Taks() {
    }

    public Taks(String title, String content, String homework, List<Integer> ageLevel) {
        this.title = title;
        this.content = content;
        this.homework = homework;
        this.ageLevel = ageLevel;
    }
    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
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
