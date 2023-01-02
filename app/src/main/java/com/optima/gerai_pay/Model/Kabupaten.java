package com.optima.gerai_pay.Model;

public class Kabupaten {
String kabaupatenid, kabputen, kodepos;

    public Kabupaten(String kabaupatenid, String kabputen, String kodepos) {
        this.kabaupatenid = kabaupatenid;
        this.kabputen = kabputen;
        this.kodepos = kodepos;
    }

    public String getKodepos() {
        return kodepos;
    }

    public String getKabaupatenid() {
        return kabaupatenid;
    }

    public String getKabputen() {
        return kabputen;
    }

    public void setKodepos(String kodepos) {
        this.kodepos = kodepos;
    }

    public void setKabaupatenid(String kabaupatenid) {
        this.kabaupatenid = kabaupatenid;
    }

    public void setKabputen(String kabputen) {
        this.kabputen = kabputen;
    }
}
