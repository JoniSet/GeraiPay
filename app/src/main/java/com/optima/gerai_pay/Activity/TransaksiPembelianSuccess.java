package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.R;

import java.util.HashMap;

public class TransaksiPembelianSuccess extends AppCompatActivity {

    private Button backBtn;
    TextView trxid, produk, data, status, harga, date, nohp;
    private SessionManager sessionManager;
    private String getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_pembelian_success);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);

        backBtn = findViewById(R.id.backBtn);
        trxid = findViewById(R.id.trxid);
        produk = findViewById(R.id.produk);
        data = findViewById(R.id.data);
        status = findViewById(R.id.status);
        harga = findViewById(R.id.price);
        date = findViewById(R.id.date);
        nohp = findViewById(R.id.nohp);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(TransaksiPembelianSuccess.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}