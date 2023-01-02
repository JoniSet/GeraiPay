package com.optima.gerai_pay.Model;

import com.google.gson.annotations.SerializedName;

public class ListKomisiItem{

	@SerializedName("transaksi_id")
	private String transaksiId;

	@SerializedName("komisi")
	private int komisi;

	@SerializedName("downline_name")
	private String downlineName;

	@SerializedName("level_downline")
	private int levelDownline;

	public String getTransaksiId(){
		return transaksiId;
	}

	public int getKomisi(){
		return komisi;
	}

	public String getDownlineName(){
		return downlineName;
	}

	public int getLevelDownline(){
		return levelDownline;
	}
}