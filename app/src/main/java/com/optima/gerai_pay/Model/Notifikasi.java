package com.optima.gerai_pay.Model;

public class Notifikasi {
    private String date;
    private String notif_title;
    private String notif_subtitle;

    public Notifikasi(String date, String notif_title, String notif_subtitle) {
        this.date = date;
        this.notif_title = notif_title;
        this.notif_subtitle = notif_subtitle;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotif_title() {
        return notif_title;
    }

    public void setNotif_title() {
        this.notif_title = notif_title;
    }

    public String getNotif_subtitle() {
        return notif_subtitle;
    }

    public void setNotif_subtitle() {
        this.notif_subtitle = notif_subtitle;
    }
}
