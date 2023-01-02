package com.optima.gerai_pay.Model;

public class ListProduk {
    String id, nama_kategori, status, logo;

    public ListProduk(String id, String nama_kategori, String status, String logo) {
        this.id = id;
        this.nama_kategori = nama_kategori;
        this.status = status;
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
