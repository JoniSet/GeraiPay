package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.optima.gerai_pay.R;

public class TopupSucsessActivity extends AppCompatActivity {
    private TextView txt_tutup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_sucsess);

        txt_tutup = findViewById(R.id.txt_tutup);
        txt_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopupSucsessActivity.this, TopUpActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TopupSucsessActivity.this, TopUpActivity.class));
        finish();
    }
}