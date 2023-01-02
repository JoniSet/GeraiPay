package com.optima.gerai_pay.Adapter;

public class OperatorPembelianAdapter {

    public String string;
    public String tag;

    public OperatorPembelianAdapter(String string, String tag) {
        this.string = string;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return string;
    }
}
