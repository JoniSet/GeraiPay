package com.optima.gerai_pay.Helper;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tanggal {
    public String getTanggal(){
        DateFormat dateFormat       = new SimpleDateFormat("yyyy-MM-dd");
        Date date                   = new Date();

        return dateFormat.format(date);
    }

    public String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}
