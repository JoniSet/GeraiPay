package com.optima.gerai_pay.Adapter;

public class OperatorTagihanAdapter {

    public String string;
    public String tag;

    public OperatorTagihanAdapter(String string, String tag) {
        this.string = string;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return string;
    }
}
