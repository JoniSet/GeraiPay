package com.optima.gerai_pay.Model;

public class ListLaporanKomisi {
    String tgl, name, komisi;

    public ListLaporanKomisi(String tgl, String name, String komisi) {
        this.tgl = tgl;
        this.name = name;
        this.komisi = komisi;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKomisi() {
        return komisi;
    }

    public void setKomisi(String komisi) {
        this.komisi = komisi;
    }
}
