package com.optima.gerai_pay.Model;

public class Kecamatan {
    String kecamatanid, kecamatan;

    public Kecamatan(String kecamatanid, String kecamatan) {
        this.kecamatanid = kecamatanid;
        this.kecamatan = kecamatan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public String getKecamatanid() {
        return kecamatanid;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public void setKecamatanid(String kecamatanid) {
        this.kecamatanid = kecamatanid;
    }
}
