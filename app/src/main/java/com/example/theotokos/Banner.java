package com.example.theotokos;

import androidx.annotation.Keep;

@Keep
public class Banner {
    private String imageURL, description;

    public Banner() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
