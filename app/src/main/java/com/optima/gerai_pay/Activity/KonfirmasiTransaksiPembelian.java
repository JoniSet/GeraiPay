package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KonfirmasiTransaksiPembelian extends AppCompatActivity {

    TextView title, nominal, phone_num, total;
    TextView saldo, trxid, produk, data, status, harga, date, nohp, saldo_final, txt_title;
    EditText edt_harga_jual;
    Button pay;
    ImageView backBtn;

    SessionManager sessionManager;
    String getToken;
    String getSaldo;
    String getId;
    String getName;
    String getTelp;
    String getEmail;
    String getReffCode;
    String getAgen;
    String getIdAgen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_transaksi_pembelian);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getToken        = user.get(SessionManager.TOKEN);
        getSaldo        = user.get(SessionManager.SALDO);
        getId           = user.get(SessionManager.ID);
        getName         = user.get(SessionManager.NAME);
        getTelp         = user.get(SessionManager.NOTELP);
        getEmail        = user.get(SessionManager.EMAIL);
        getReffCode     = user.get(SessionManager.REFFCODE);
        getAgen         = user.get(SessionManager.AGEN);
        getIdAgen       = user.get(SessionManager.ID_AGEN);

        Intent i=getIntent();

        String name     = i.getStringExtra("product_name");
        String price    = i.getStringExtra("price");
        String phone    = i.getStringExtra("phone");
        String inquiry  = i.getStringExtra("inquiry");

        phone_num       = findViewById(R.id.phonenum);
        title           = findViewById(R.id.title_nominal);
        nominal         = findViewById(R.id.nominal);
        saldo           = findViewById(R.id.saldo);
        trxid           = findViewById(R.id.trxid);
        produk          = findViewById(R.id.produk);
        data            = findViewById(R.id.data);
        status          = findViewById(R.id.status);
        harga           = findViewById(R.id.price);
        date            = findViewById(R.id.date);
        nohp            = findViewById(R.id.nohp);
        total           = findViewById(R.id.total);
        pay             = findViewById(R.id.pay);
        saldo_final     = findViewById(R.id.saldo_final);
        edt_harga_jual  = findViewById(R.id.edt_harga_jual);
        backBtn         = findViewById(R.id.backBtn);
        txt_title       = findViewById(R.id.txt_title);
        ProgressBar loading = findViewById(R.id.loading);
        title.setText(name);
        phone_num.setText(phone);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formattedString = formatter.format(Long.parseLong(price));
        String formattedSaldo  = formatter.format(Long.parseLong(getSaldo));

        total.setText(formattedString);
        nominal.setText(formattedString);
        edt_harga_jual.setText(formattedString);

        saldo.setText(formattedSaldo);

        pay.setOnClickListener(view -> {
            double h = Double.parseDouble(price);
            double ub = Double.parseDouble(getSaldo);
            if (ub < (h)) {
                FancyToast.makeText(KonfirmasiTransaksiPembelian.this, "Saldo Tidak Cukup, Silahkan Lakukan Top Up",
                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
            else if (edt_harga_jual.getText().toString().isEmpty()){
                FancyToast.makeText(KonfirmasiTransaksiPembelian.this, "Mohon isi harga jual anda!",
                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
            else if (Integer.parseInt(edt_harga_jual.getText().toString().replace(",", "")) < Integer.parseInt(price)){
                FancyToast.makeText(KonfirmasiTransaksiPembelian.this, "Harga yang anda masukkan terlalu rendah!",
                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
            else if (inquiry.equals("Token Listrik")) {
                requestPayPln();
            }
            else if (inquiry.equals("E-TOLL")){
                requestPay();
            }
            else if (inquiry.equals("GAME")){
                txt_title.setText("ID User");
                requestPay();
            }
            else if (inquiry.equals("")){
                requestPay();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setView();
    }

    private void setView() {
        edt_harga_jual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                total.setText(edt_harga_jual.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_harga_jual.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edt_harga_jual.setText(formattedString);
                    edt_harga_jual.setSelection(edt_harga_jual.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edt_harga_jual.addTextChangedListener(this);
            }
        });
    }

    private void requestPay(){
        final String code = getIntent().getStringExtra("code");
        final String phone = getIntent().getStringExtra("phone");

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();

        String url = Utils.BASE_URL +  "transaksi_pulsa";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    final JSONObject jsonObject;
                    try{
                        jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message").trim();
                        String responseCode = jsonObject.getString("response");

                        Log.d("Salahh", jsonObject.toString());

                        if (responseCode.equals("201")) {
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                            dialog.dismiss();
                        } else if (responseCode.equals("202")) {
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                            dialog.dismiss();
                        }else if (responseCode.equals("203")) {
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                            dialog.dismiss();
                        } else {
                            toastIcon("Transaksi Telah Masuk Dalam Antrian", R.drawable.ic_checklist, R.color.green_forest_primary);
                            Intent intent = new Intent(KonfirmasiTransaksiPembelian.this, TransaksiPembayaranSuccess.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        toastIcon("Transaksi Telah Masuk Dalam Antrian", R.drawable.ic_checklist, R.color.green_forest_primary);
                        Intent intent = new Intent(KonfirmasiTransaksiPembelian.this, TransaksiPembayaranSuccess.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                },
                error -> {
                    toastIcon("Transaksi Telah Masuk Dalam Antrian", R.drawable.ic_checklist, R.color.green_forest_primary);
                    Intent intent = new Intent(KonfirmasiTransaksiPembelian.this, TransaksiPembayaranSuccess.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("kode_produk", code);
                params.put("nomor", phone);
                params.put("harga_jual", edt_harga_jual.getText().toString().replace(",", ""));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void requestPayPln(){
        final String code = getIntent().getStringExtra("code");
        final String phone = getIntent().getStringExtra("phone");
        final String idPel = getIntent().getStringExtra("idPelanggan");

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();

        String url = Utils.BASE_URL + "transaksi_pulsa";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    final JSONObject jsonObject;
                    try{
                        jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message").trim();
                        String responseCode = jsonObject.getString("response");

                        switch (responseCode) {
                            case "201":
                            case "202":
                            case "203":
                                toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                                dialog.dismiss();
                                break;
                            default:
                                toastIcon("Transaksi Telah Masuk Dalam Antrian", R.drawable.ic_checklist, R.color.green_forest_primary);
                                Intent intent = new Intent(KonfirmasiTransaksiPembelian.this, TransaksiPembayaranSuccess.class);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                                break;
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        toastIcon("Transaksi Telah Masuk Dalam Antrian", R.drawable.ic_checklist, R.color.green_forest_primary);
                        Intent intent = new Intent(KonfirmasiTransaksiPembelian.this, TransaksiPembayaranSuccess.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                },
                error -> {
                    toastIcon("Transaksi Telah Masuk Dalam Antrian", R.drawable.ic_checklist, R.color.green_forest_primary);
                    Intent intent = new Intent(KonfirmasiTransaksiPembelian.this, TransaksiPembayaranSuccess.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("kode_produk", code);
                params.put("nomor", phone);
                params.put("no_meter_pln", idPel);
                params.put("edt_harga_jual", edt_harga_jual.getText().toString().replace(",", ""));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void toastIcon(final String message, final int image, final int color) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        @SuppressLint("InflateParams") View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(image);
        ((ImageView) custom_view.findViewById(R.id.icon)).setColorFilter(android.R.color.white);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(color));

        toast.setView(custom_view);
        toast.show();
    }

}