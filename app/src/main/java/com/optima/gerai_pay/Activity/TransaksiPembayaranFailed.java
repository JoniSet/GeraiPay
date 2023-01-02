package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TransaksiPembayaranFailed extends AppCompatActivity {

    private TextView id_order, produk_code, produk_name, harga, order_data, order_status, order_date,
            target, mtrpln;

    private String getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_pembayaran_failed);

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        Intent i=getIntent();

        String orderId = i.getStringExtra("order_id");
        String code = i.getStringExtra("code");
        String name = i.getStringExtra("name");
        String price = i.getStringExtra("price");
        String data = i.getStringExtra("data");
        String status = i.getStringExtra("status");
        String date = i.getStringExtra("date");
        String hp = i.getStringExtra("hp");
        String pln = i.getStringExtra("pln");

        id_order = findViewById(R.id.id_order);
        produk_code = findViewById(R.id.produk_code);
        produk_name = findViewById(R.id.produk_name);
        harga = findViewById(R.id.harga);
        order_data = findViewById(R.id.order_data);
        order_status = findViewById(R.id.order_status);
        order_date = findViewById(R.id.order_date);
        target = findViewById(R.id.target);
        mtrpln = findViewById(R.id.mtrpln);
        Button backBtn = findViewById(R.id.backBtn);

        id_order.setText(orderId);
        produk_code.setText(code);
        produk_name.setText(name);
        harga.setText(price);
        order_data.setText(data);
        order_status.setText(status);
        order_date.setText(date);
        target.setText(hp);
        mtrpln.setText(pln);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(TransaksiPembayaranFailed.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        postData();
    }

    private void postData(){

        final String id = this.id_order.getText().toString().trim();
        final String produk = this.produk_code.getText().toString().trim();
        final String name = this.produk_name.getText().toString().trim();
        final String price = this.harga.getText().toString().trim();
        final String data = this.order_data.getText().toString().trim();
        final String status = this.order_status.getText().toString().trim();
        final String date = this.order_date.getText().toString().trim();
        final String hp = this.target.getText().toString().trim();
        final String pln = this.mtrpln.getText().toString().trim();

        String URL_REGISTER = "http://api.dianarthaselaras.com/history/addpembayaran";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(TransaksiPembayaranFailed.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }, 3000);

                    } catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(TransaksiPembayaranFailed.this, "Jaringan Bermasalah, Coba Lagi !", Toast.LENGTH_SHORT).show();

                    }
                },
                error -> Toast.makeText(TransaksiPembayaranFailed.this, "Jaringan Bermasalah, Coba Lagi !", Toast.LENGTH_SHORT).show())

        {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", getId);
                params.put("order_id", id);
                params.put("code", produk);
                params.put("produk", name);
                params.put("price", price);
                params.put("target", hp);
                params.put("meterpln", pln);
                params.put("data", data);
                params.put("status", "Failed");
                params.put("date", date);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}