package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KonfirmasiTransaksiPembayaran extends AppCompatActivity {

    private TextView totalBayar;
    private ProgressBar loading;

    private EditText edt_harga_jual;

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
    String IDTransaksi;
    String getJumlahBayar;
    String IDPelanggan;
    String namaProduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_transaksi_pembayaran);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getToken = user.get(SessionManager.TOKEN);
        getSaldo = user.get(SessionManager.SALDO);
        getId = user.get(SessionManager.ID);
        getName = user.get(SessionManager.NAME);
        getTelp = user.get(SessionManager.NOTELP);
        getEmail = user.get(SessionManager.EMAIL);
        getReffCode = user.get(SessionManager.REFFCODE);
        getAgen = user.get(SessionManager.AGEN);
        getIdAgen = user.get(SessionManager.ID_AGEN);

        TextView idPelanggan = findViewById(R.id.idPelanggan);
        TextView titleProduk = findViewById(R.id.titleProduk);
        TextView jumlahBayar = findViewById(R.id.jumlahBayar);
        TextView saldo = findViewById(R.id.saldo);
        Button payButton = findViewById(R.id.payBtn);
        totalBayar      = findViewById(R.id.totalBayar);
        edt_harga_jual  = findViewById(R.id.edt_harga_jual);

        Intent i=getIntent();

        namaProduk = i.getStringExtra("namaProduk");
        IDPelanggan = i.getStringExtra("IDPelanggan");
        IDTransaksi = i.getStringExtra("IDTransaksi");
        getJumlahBayar = i.getStringExtra("jumlahBayar");

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formattedBayar  = formatter.format(Long.parseLong(String.valueOf(getJumlahBayar)));
        String formattedSaldo  = formatter.format(Long.parseLong(String.valueOf(getSaldo)));

        jumlahBayar.setText("Rp. " + formattedBayar);
        totalBayar.setText("Rp. " + formattedBayar);
        saldo.setText("Rp. " + formattedSaldo);
        idPelanggan.setText(IDPelanggan);
        titleProduk.setText(namaProduk);
        edt_harga_jual.setText(formattedBayar);

        payButton.setOnClickListener(view -> {
            double h = Double.parseDouble(getJumlahBayar);
            double ub = Double.parseDouble(getSaldo);
            if (ub < (h)) {
                toastIcon("Saldo Tidak Cukup, Silahkan Lakukan Top Up !!", R.drawable.ic_close, android.R.color.holo_red_dark);
            }
            else if (edt_harga_jual.getText().toString().isEmpty()){
                FancyToast.makeText(KonfirmasiTransaksiPembayaran.this, "Mohon isi harga jual anda!",
                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
            else if (Integer.parseInt(edt_harga_jual.getText().toString().replace(",", "")) < Integer.parseInt(getJumlahBayar)){
                FancyToast.makeText(KonfirmasiTransaksiPembayaran.this, "Harga yang anda masukkan terlalu rendah!",
                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }else {
                requestPay();
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
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Utils.BASE_URL + "transaksi_tagihan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message").trim();
                        String responceCode = jsonObject.getString("response").trim();

                        if (responceCode.equals("200") && message.equals("Pembayaran Berhasil")) {
                            dialog.dismiss();
                            toastIcon("Transaksi Telah Masuk Dalam Antrian", R.drawable.ic_checklist, R.color.green_forest_primary);
                            sessionManager.createSession(getId, getToken, getName, getTelp, getEmail, jsonObject.getString("saldo_akhir"), getReffCode, getAgen, getIdAgen, MainActivity.provinsi, MainActivity.kabupaten, MainActivity.kecamatan);
                            Intent intent = new Intent(KonfirmasiTransaksiPembayaran.this, TransaksiPembayaranSuccess.class);
                            startActivity(intent);
                            finish();

                        } else if (responceCode.equals("203") && message.equals("Pembayaran Gagal")) {
                            dialog.dismiss();
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                        }
                        else {
                            dialog.dismiss();
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                        }

                    } catch (JSONException e){
                        toastIcon("Transaksi Telah Masuk Dalam Antrian", R.drawable.ic_checklist, R.color.green_forest_primary);
                        Intent intent = new Intent(KonfirmasiTransaksiPembayaran.this, TransaksiPembayaranSuccess.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                },
                error -> {
                    toastIcon("Transaksi Telah Masuk Dalam Antrian", R.drawable.ic_checklist, R.color.green_forest_primary);
                    Intent intent = new Intent(KonfirmasiTransaksiPembayaran.this, TransaksiPembayaranSuccess.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("tagihan_id", IDTransaksi);
                params.put("jumlah_bayar", getJumlahBayar);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}