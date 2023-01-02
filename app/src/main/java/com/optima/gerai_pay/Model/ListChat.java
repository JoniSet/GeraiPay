package com.optima.gerai_pay.Model;

public class ListChat {
    private String id, tujuan_user, dari_id, pesan, tgl, baca;

    public ListChat() {
    }

    public ListChat(String id, String tujuan_user, String dari_id, String pesan, String tgl, String baca) {
        this.id = id;
        this.tujuan_user = tujuan_user;
        this.dari_id = dari_id;
        this.pesan = pesan;
        this.tgl = tgl;
        this.baca = baca;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTujuan_user() {
        return tujuan_user;
    }

    public void setTujuan_user(String tujuan_user) {
        this.tujuan_user = tujuan_user;
    }

    public String getDari_id() {
        return dari_id;
    }

    public void setDari_id(String dari_id) {
        this.dari_id = dari_id;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getBaca() {
        return baca;
    }

    public void setBaca(String baca) {
        this.baca = baca;
    }
}
