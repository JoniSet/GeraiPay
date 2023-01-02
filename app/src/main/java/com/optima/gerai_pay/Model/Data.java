package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("code")
	private String code;

	@SerializedName("jumlah_tagihan")
	private String jumlahTagihan;

	@SerializedName("admin")
	private String admin;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("tagihan_id")
	private String tagihanId;

	@SerializedName("type")
	private String type;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("periode")
	private String periode;

	@SerializedName("via")
	private String via;

	@SerializedName("nama")
	private String nama;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("phone")
	private String phone;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("id")
	private int id;

	@SerializedName("jumlah_bayar")
	private int jumlahBayar;

	@SerializedName("no_pelanggan")
	private String noPelanggan;

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setJumlahTagihan(String jumlahTagihan){
		this.jumlahTagihan = jumlahTagihan;
	}

	public String getJumlahTagihan(){
		return jumlahTagihan;
	}

	public void setAdmin(String admin){
		this.admin = admin;
	}

	public String getAdmin(){
		return admin;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setTagihanId(String tagihanId){
		this.tagihanId = tagihanId;
	}

	public String getTagihanId(){
		return tagihanId;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setPeriode(String periode){
		this.periode = periode;
	}

	public String getPeriode(){
		return periode;
	}

	public void setVia(String via){
		this.via = via;
	}

	public String getVia(){
		return via;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setJumlahBayar(int jumlahBayar){
		this.jumlahBayar = jumlahBayar;
	}

	public int getJumlahBayar(){
		return jumlahBayar;
	}

	public void setNoPelanggan(String noPelanggan){
		this.noPelanggan = noPelanggan;
	}

	public String getNoPelanggan(){
		return noPelanggan;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"code = '" + code + '\'' + 
			",jumlah_tagihan = '" + jumlahTagihan + '\'' + 
			",admin = '" + admin + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",tagihan_id = '" + tagihanId + '\'' + 
			",type = '" + type + '\'' + 
			",product_name = '" + productName + '\'' + 
			",periode = '" + periode + '\'' + 
			",via = '" + via + '\'' + 
			",nama = '" + nama + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",phone = '" + phone + '\'' + 
			",user_id = '" + userId + '\'' + 
			",id = '" + id + '\'' + 
			",jumlah_bayar = '" + jumlahBayar + '\'' + 
			",no_pelanggan = '" + noPelanggan + '\'' + 
			"}";
		}
}