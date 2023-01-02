package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class OperatorTagihan {

    @SerializedName("nama_produk")
    private String namaProduk;

    @SerializedName("kode_produk")
    private String kodeProduk;

    @SerializedName("status")
    private String status;

    public void setNamaProduk(String namaProduk){
        this.namaProduk = namaProduk;
    }

    public String getNamaProduk(){
        return namaProduk;
    }

    public void setKodeProduk(String kodeProduk){
        this.kodeProduk = kodeProduk;
    }

    public String getKodeProduk(){
        return kodeProduk;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String  getStatus(){
        return status;
    }
}
