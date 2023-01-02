package com.optima.gerai_pay.Model;

public class ListNotif {
    String id, kategori, title, body, tanggal;

    public ListNotif() {
    }

    public ListNotif(String id, String kategori, String title, String body, String tanggal) {
        this.id = id;
        this.kategori = kategori;
        this.title = title;
        this.body = body;
        this.tanggal = tanggal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
