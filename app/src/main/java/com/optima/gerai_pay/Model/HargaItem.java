package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class HargaItem{

	@SerializedName("nama_produk")
	private String namaProduk;

	@SerializedName("harga")
	private int harga;

	@SerializedName("kode_produk")
	private String kodeProduk;

	@SerializedName("status")
	private String status;

	@SerializedName("premium")
	private String premium;

	public String getNamaProduk(){
		return namaProduk;
	}

	public int getHarga(){
		return harga;
	}

	public String getKodeProduk(){
		return kodeProduk;
	}

	public String getStatus(){
		return status;
	}

	public String getPremium() {
		return premium;
	}
}