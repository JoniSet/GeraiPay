package com.optima.gerai_pay.Model;

public class DataTraining {
    String id, title;
    int gambar;

    public DataTraining(String id, String title, int gambar) {
        this.id = id;
        this.title = title;
        this.gambar = gambar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGambar() {
        return gambar;
    }

    public void setGambar(int gambar) {
        this.gambar = gambar;
    }
}
