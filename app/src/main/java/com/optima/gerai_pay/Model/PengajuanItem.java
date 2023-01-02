package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class PengajuanItem {

	@SerializedName("provinsi")
	private String provinsi;

	@SerializedName("pendidikan")
	private String pendidikan;

	@SerializedName("foto_npwp")
	private String fotoNpwp;

	@SerializedName("kode_pos")
	private String kodePos;

	@SerializedName("foto_ktp")
	private String fotoKtp;

	@SerializedName("kabupaten")
	private String kabupaten;

	@SerializedName("nomor_nik")
	private String nomorNik;

	@SerializedName("kelurahan")
	private String kelurahan;

	@SerializedName("alamat")
	private String alamat;

	@SerializedName("nama_pelanggan")
	private String namaPelanggan;

	@SerializedName("handphone")
	private String handphone;

	@SerializedName("tempat_lahir")
	private String tempatLahir;

	@SerializedName("pekerjaan")
	private String pekerjaan;

	@SerializedName("id_agent")
	private String idAgent;

	@SerializedName("id_pelanggan")
	private String idPelanggan;

	@SerializedName("status_pengajuan")
	private String statusPengajuan;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("id")
	private String id;

	@SerializedName("nama_ibukandung")
	private String namaIbukandung;

	@SerializedName("tanggal_lahir")
	private String tanggalLahir;

	@SerializedName("email")
	private String email;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("product_price")
	private String productPrice;

	@SerializedName("tanggal_pengajuan")
	private String tanggalPengajuan;

	@SerializedName("product_img")
	private String productImg;

	@SerializedName("status_pernikahan")
	private String statusPernikahan;

	public String getProvinsi(){
		return provinsi;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public String getTanggalPengajuan() {
		return tanggalPengajuan;
	}

	public String getProductImg() {
		return productImg;
	}

	public String getPendidikan(){
		return pendidikan;
	}

	public String getFotoNpwp(){
		return fotoNpwp;
	}

	public String getKodePos(){
		return kodePos;
	}

	public String getFotoKtp(){
		return fotoKtp;
	}

	public String getKabupaten(){
		return kabupaten;
	}

	public String getNomorNik(){
		return nomorNik;
	}

	public String getKelurahan(){
		return kelurahan;
	}

	public String getAlamat(){
		return alamat;
	}

	public String getNamaPelanggan(){
		return namaPelanggan;
	}

	public String getHandphone(){
		return handphone;
	}

	public String getTempatLahir(){
		return tempatLahir;
	}

	public String getPekerjaan(){
		return pekerjaan;
	}

	public String getIdAgent(){
		return idAgent;
	}

	public String getIdPelanggan() {
		return idPelanggan;
	}

	public String getStatusPengajuan(){
		return statusPengajuan;
	}

	public String getKecamatan(){
		return kecamatan;
	}

	public String getId(){
		return id;
	}

	public String getNamaIbukandung(){
		return namaIbukandung;
	}

	public String getTanggalLahir(){
		return tanggalLahir;
	}

	public String getEmail(){
		return email;
	}

	public String getStatusPernikahan(){
		return statusPernikahan;
	}
}