package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class TagihanList {

	@SerializedName("code")
	private String code;

	@SerializedName("biaya_admin")
	private String biayaAdmin;

	@SerializedName("pembayarankategori_id")
	private String pembayarankategoriId;

	@SerializedName("id")
	private int id;

	@SerializedName("pembayaranoperator_id")
	private String pembayaranoperatorId;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("status")
	private String status;

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setBiayaAdmin(String biayaAdmin){
		this.biayaAdmin = biayaAdmin;
	}

	public String getBiayaAdmin(){
		return biayaAdmin;
	}

	public void setPembayarankategoriId(String pembayarankategoriId){
		this.pembayarankategoriId = pembayarankategoriId;
	}

	public String getPembayarankategoriId(){
		return pembayarankategoriId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPembayaranoperatorId(String pembayaranoperatorId){
		this.pembayaranoperatorId = pembayaranoperatorId;
	}

	public String getPembayaranoperatorId(){
		return pembayaranoperatorId;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"code = '" + code + '\'' + 
			",biaya_admin = '" + biayaAdmin + '\'' + 
			",pembayarankategori_id = '" + pembayarankategoriId + '\'' + 
			",id = '" + id + '\'' + 
			",pembayaranoperator_id = '" + pembayaranoperatorId + '\'' + 
			",product_name = '" + productName + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}