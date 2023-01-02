package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.R;

public class TransaksiPembayaranSuccess extends AppCompatActivity {

    private Button backBtn;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_pembayaran_success);

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransaksiPembayaranSuccess.this, MainActivity.class));
                finish();
            }
        });
    }
}