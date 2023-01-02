package com.optima.gerai_pay.Model;

public class Province {
    String province,province_id;

    public Province(String province, String province_id) {
        this.province = province;
        this.province_id = province_id;
    }

    public String getProvince() {
        return province;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }
}
