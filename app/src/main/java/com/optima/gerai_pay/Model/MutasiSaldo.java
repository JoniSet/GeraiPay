package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class MutasiSaldo {

    @SerializedName("tgl")
    private String tgl;

    @SerializedName("nominal")
    private String nominal;

    @SerializedName("keterangan")
    private String keterangan;

    @SerializedName("status")
    private String status;

    public String getTgl(){
        return tgl;
    }

    public String getNominal() {
        return nominal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getStatus() {
        return status;
    }
}
