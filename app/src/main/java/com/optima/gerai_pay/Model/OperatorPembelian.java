
package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class OperatorPembelian {

    @SerializedName("nama_produk")
    private String namaProduk;

    @SerializedName("id_produk")
    private String idProduk;

    @SerializedName("id_kategori")
    private String idKategori;

    public void setNamaProduk(String namaProduk){
        this.namaProduk = namaProduk;
    }

    public String getNamaProduk(){
        return namaProduk;
    }

    public void setIdProduk(String idProduk){
        this.idProduk = idProduk;
    }

    public String getIdProduk(){
        return idProduk;
    }

    public void setIdKategori(String idKategori){
        this.idKategori = idKategori;
    }

    public String  getIdKategori(){
        return idKategori;
    }
}
