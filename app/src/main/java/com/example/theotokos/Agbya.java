package com.example.theotokos;

public class Agbya {
    private String title;
    private String content;
    private String description;


    public Agbya() {

    }

    public Agbya(String title, String content, String description) {
        this.title = title;
        this.content = content;
        this.description = description;
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
}
