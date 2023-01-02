package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.optima.gerai_pay.R;

public class VerifikasiAkunActivity extends AppCompatActivity {

    String kode_verifikasi, otp_id, nohp;
    String otp1, otp2, otp3, otp4, otp5, otp6;
    private EditText edt_otp1, edt_otp2, edt_otp3, edt_otp4, edt_otp5, edt_otp6;
    private Button btn_kirim_kode;
    private TextView txt_kirim_ulang_otp, txt_waktu_otp;
    int count   = 30;
    int sounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_akun);
    }
}