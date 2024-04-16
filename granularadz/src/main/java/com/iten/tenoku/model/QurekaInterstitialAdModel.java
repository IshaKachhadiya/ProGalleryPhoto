package com.iten.tenoku.model;

public class QurekaInterstitialAdModel {

    String title;
    String description;
    String button;

    public QurekaInterstitialAdModel(String title, String description, String button) {
        this.title = title;
        this.description = description;
        this.button = button;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getButton() {
        return button;
    }
}
