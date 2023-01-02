package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class AlamatItem {

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private BigInteger id;

	public String getName(){
		return name;
	}

	public BigInteger getId(){
		return id;
	}
}