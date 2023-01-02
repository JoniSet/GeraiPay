package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class PekerjaanItem {

	@SerializedName("pekerjaan")
	private String pekerjaan;

	@SerializedName("id")
	private String id;

	public String getPekerjaan(){
		return pekerjaan;
	}

	public void setPekerjaan(String pekerjaan) {
		this.pekerjaan = pekerjaan;
	}

	public String getId(){
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}