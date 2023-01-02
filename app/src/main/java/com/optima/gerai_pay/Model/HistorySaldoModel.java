package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class HistorySaldoModel {

	@SerializedName("tgl")
	private String tgl;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("produk")
	private String produk;

	@SerializedName("total")
	private String total;

	@SerializedName("status")
	private String status;

	public void setTgl(String tgl){
		this.tgl = tgl;
	}

	public String getTgl(){
		return tgl;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setProduk(String produk) {
		this.produk = produk;
	}

	public String getProduk() {
		return produk;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotal() {
		return total;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}