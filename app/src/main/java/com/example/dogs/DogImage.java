package com.example.dogs;

public class DogImage {
    private String image;
    private String status;

    public DogImage(String image, String status) {
        this.image = image;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "DogImage{" +
                "image='" + image + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
