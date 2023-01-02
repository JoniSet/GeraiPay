package com.optima.gerai_pay.Model;

public class ListDownline {
    String id, nama, notelp, jumlah_transfer;

    public ListDownline(String id, String nama, String notelp, String jumlah_transfer) {
        this.id = id;
        this.nama = nama;
        this.notelp = notelp;
        this.jumlah_transfer = jumlah_transfer;
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

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public String getJumlah_transfer() {
        return jumlah_transfer;
    }

    public void setJumlah_transfer(String jumlah_transfer) {
        this.jumlah_transfer = jumlah_transfer;
    }
}
