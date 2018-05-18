package com.example.jeromelaquay.freesia;

public class Photo {

    private String image;
    private String message;

    public Photo() {
    }

    public Photo(String image, String message) {
        this.image = image;
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
