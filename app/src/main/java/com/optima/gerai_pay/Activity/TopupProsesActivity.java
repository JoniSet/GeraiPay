package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.optima.gerai_pay.R;

public class TopupProsesActivity extends AppCompatActivity {
    private ImageView backBtn;
    private TextView txt_countdown, txt_belum_tf, txt_detail;
    private Button btn_tutup;

    int count   = 30;
    int counter = 0;

    TimerClass timerClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_proses);

        initView();
    }

    private void initView() {
        backBtn         = findViewById(R.id.backBtn);
        txt_countdown   = findViewById(R.id.txt_countdown);
        txt_belum_tf    = findViewById(R.id.txt_belum_tf);
        btn_tutup       = findViewById(R.id.btn_tutup);
        txt_detail      = findViewById(R.id.txt_detail);


        setView();
        setButton();
    }

    private void setButton() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_belum_tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopupProsesActivity.this, TopUpActivity.class));
                finish();
            }
        });
    }

    private void setView() {
        timerClass      = new TimerClass(60000 * 10, 1000);
        timerClass.start();
    }

    public class TimerClass extends CountDownTimer {

        TimerClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //Method ini berjalan saat waktu/timer berubah
        @Override
        public void onTick(long millisUntilFinished) {
            long detik  = millisUntilFinished / 1000;
            //Kenfigurasi Format Waktu yang digunakan
//            String waktuu = String.format("%02d:%02d",
//                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
//                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
//                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

            //Menampilkannya pada TexView
            txt_countdown.setText(String.format("%02d", detik / 60)
                    + ":" + String.format("%02d", detik % 60));

            if (txt_countdown.getText().toString().equals("00:01")){
                startActivity(new Intent(TopupProsesActivity.this, TopUpActivity.class));
                finish();
            }

        }

        @Override
        public void onFinish() {
            ///Berjalan saat waktu telah selesai atau berhenti

        }
    }
}