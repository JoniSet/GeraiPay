package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class HistoryModel {

	@SerializedName("tgl")
	private String tgl;

	@SerializedName("transaksi_id")
	private String transaksiId;

	@SerializedName("nama_produk")
	private String namaProduk;

	@SerializedName("target")
	private String target;

	@SerializedName("harga")
	private String harga;

	@SerializedName("harga_jual")
	private String harga_jual;

	@SerializedName("status")
	private String status;

	public void setTgl(String tgl){
		this.tgl = tgl;
	}

	public String getTgl() {
		return tgl;
	}

	public void setTransaksiId(String transaksiId) {
		this.transaksiId = transaksiId;
	}

	public String getTransaksiId() {
		return transaksiId;
	}

	public void setNamaProduk(String namaProduk) {
		this.namaProduk = namaProduk;
	}

	public String getNamaProduk() {
		return namaProduk;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public void setHarga(String harga) {
		this.harga = harga;
	}

	public String getHarga() {
		return harga;
	}

	public void setStatus() {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public String getHarga_jual() {
		return harga_jual;
	}

	public void setHarga_jual(String harga_jual) {
		this.harga_jual = harga_jual;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}