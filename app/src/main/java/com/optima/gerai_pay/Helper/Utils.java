package com.optima.gerai_pay.Helper;

import android.view.View;

import androidx.core.widget.NestedScrollView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Utils {

//    public static final String BASE_URL = "https://ppob.optimadigital.co.id/api_ppob/public/api/";
    public static final String BASE_URL = "https://api.geraipay.com/api/";
    public static final String AGENT_URL = "http://smartagent.dianarthaselaras.com/api/";
    public static final String WA_API_URL = "https://api.whatsapp.com/send?phone=";
    public static final String RAJAONGKIR_API = "https://x.rajaapi.com/";
    public static final String MISC_API = "http://apipos.dianarthaselaras.com/api/";

    public String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    public String formatIdr(Double number) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("IDR"));
        return format.format(number);
    }

    public static void nestedScrollTo(final NestedScrollView nested, final View targetView) {
        nested.post(() -> nested.scrollTo(500, targetView.getBottom()));
    }

}
