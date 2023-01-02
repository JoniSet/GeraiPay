package com.optima.gerai_pay.Model;

public class ListImageSlider {
    String id, nama;

    int gambar;

    public ListImageSlider(String id, String nama, int gambar) {
        this.id = id;
        this.nama = nama;
        this.gambar = gambar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getGambar() {
        return gambar;
    }

    public void setGambar(int gambar) {
        this.gambar = gambar;
    }
}
