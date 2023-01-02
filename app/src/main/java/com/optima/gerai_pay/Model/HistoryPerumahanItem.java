package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class HistoryPerumahanItem {

	@SerializedName("harga_resmi")
	private String hargaResmi;

	@SerializedName("kav")
	private String kav;

	@SerializedName("diskon")
	private String diskon;

	@SerializedName("harga_diskon")
	private String hargaDiskon;

	@SerializedName("tgl_booking")
	private String tglBooking;

	@SerializedName("ppn")
	private String ppn;

	@SerializedName("booking_fee")
	private String bookingFee;

	@SerializedName("nomor_blok")
	private String nomorBlok;

	@SerializedName("userid")
	private String userid;

	@SerializedName("nama_blok")
	private String namaBlok;

	@SerializedName("status_penjualan")
	private String statusPenjualan;

	@SerializedName("handphone")
	private String handphone;

	@SerializedName("nama")
	private String nama;

	@SerializedName("id")
	private String id;

	@SerializedName("metode_pembelian")
	private String metodePembelian;

	@SerializedName("email")
	private String email;

	@SerializedName("no_urut")
	private String noUrut;

	@SerializedName("blok")
	private String blok;

	@SerializedName("tipe_rumah")
	private String tipeRumah;

	@SerializedName("order_id")
	private String orderId;

	public String getHargaResmi(){
		return hargaResmi;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getBlok() {
		return blok;
	}

	public String getNoUrut() {
		return noUrut;
	}

	public String getKav(){
		return kav;
	}

	public String getBookingFee(){
		return bookingFee;
	}

	public String getNomorBlok(){
		return nomorBlok;
	}

	public String getUserid(){
		return userid;
	}

	public String getNamaBlok(){
		return namaBlok;
	}

	public String getStatusPenjualan(){
		return statusPenjualan;
	}

	public String getHandphone(){
		return handphone;
	}

	public String getNama(){
		return nama;
	}

	public String getId(){
		return id;
	}

	public String getMetodePembelian(){
		return metodePembelian;
	}

	public String getEmail(){
		return email;
	}

	public String getTglBooking(){
		return tglBooking;
	}

	public String getTipeRumah(){
		return tipeRumah;
	}

	public String getDiskon(){
		return diskon;
	}

	public String getHargaDiskon(){
		return hargaDiskon;
	}

	public String getPpn(){
		return ppn;
	}
}