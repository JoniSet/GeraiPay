package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.optima.gerai_pay.Helper.Loading;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ValidationActivity extends AppCompatActivity {

    private EditText code1, code2, code3, code4, code5, code6;
    public int counter;
    String getOtpID, kodle_validasi, no_hp;
    Loading loading     = new Loading();
    Dialog dialog;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        sessionManager  = new SessionManager(ValidationActivity.this);

        dialog  = loading.dialog_helper(ValidationActivity.this);

        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);
        ImageButton backBtn     = findViewById(R.id.back);
        TextView countDownTimer = findViewById(R.id.countdownTimer);
        TextView retryBtn       = findViewById(R.id.retryBtn);
        Button submitBtn        = findViewById(R.id.submitBtn);

        getOtpID                = getIntent().getStringExtra("otpID");
        kodle_validasi          = getIntent().getStringExtra("kode_verifikasi");

        if (kodle_validasi.equals("2") || kodle_validasi.equals("3")){
            sendOtp();
        }

        new CountDownTimer(60000, 1000) {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                countDownTimer.setText("Kirim kode ulang dalam : " + millisUntilFinished / 1000);
                counter++;
            }

            @Override
            public void onFinish() {
                retryBtn.setVisibility(View.VISIBLE);
                countDownTimer.setVisibility(View.GONE);
            }

        } .start();

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ValidationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        submitBtn.setOnClickListener(v -> SendVerification());

        retryBtn.setOnClickListener(v -> {
            sendOtp();
            retryBtn.setVisibility(View.GONE);
            countDownTimer.setVisibility(View.VISIBLE);
        });

        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code1.getText().toString().length() > 0) {
                    code2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code2.getText().toString().length() > 0) {
                    code3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code3.getText().toString().length() > 0) {
                    code4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code4.getText().toString().length() > 0) {
                    code5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code5.getText().toString().length() > 0) {
                    code6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        code6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code6.getText().toString().length() > 0) {
                    submitBtn.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void SendVerification(){
        Dialog dialog           = new Dialog(ValidationActivity.this);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        TextView txt_title      = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Mengirim Kode");

        String URL_LOGIN = Utils.BASE_URL + "verifikasi";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");

                        dialog.dismiss();

                        if (responseCode.equals("200")) {
                            if (kodle_validasi.equals("1")) {
                                Intent intent = new Intent(ValidationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();toastIcon(message, R.drawable.ic_checklist, R.color.green_forest_primary);

                            }
                            else if (kodle_validasi.equals("2")) {
                                gantiNotelp();
                            }
                            else if (kodle_validasi.equals("3")) {
                                Intent intent = new Intent(ValidationActivity.this, GantiPasswordActivity.class);
                                intent.putExtra("nohp", getIntent().getStringExtra("nohp"));
                                intent.putExtra("password_lama", getIntent().getStringExtra("password"));
                                intent.putExtra("kode", "2");
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Intent intent = new Intent(ValidationActivity.this, GantiPasswordActivity.class);
                                intent.putExtra("nohp", getIntent().getStringExtra("nohp"));
                                intent.putExtra("kode", "1");
                                startActivity(intent);
                                finish();
                            }
                        } else if (responseCode.equals("201")) {
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        dialog.dismiss();
                        toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark);
                    }
                },
                error -> toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark))
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("otp", code1.getText().toString() + code2.getText().toString() + code3.getText().toString() + code4.getText().toString() + code5.getText().toString() + code6.getText().toString());
                params.put("otp_id", getOtpID);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendOtp(){
        String URL_LOGIN = Utils.BASE_URL + "otp";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");

                        if (responseCode.equals("200")) {
                            toastIcon(message, R.drawable.ic_checklist, android.R.color.holo_green_light);
                            getOtpID = jsonObject.getString("otp_id");

                        } else if (responseCode.equals("201")) {
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark);
                    }
                },
                error -> toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark))
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("notelp", getIntent().getStringExtra("noTelp"));
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void gantiNotelp(){
        dialog.show();
        AndroidNetworking.post(Utils.BASE_URL + "ganti_email")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .addBodyParameter("notelp", getIntent().getStringExtra("noTelpBaru"))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            dialog.dismiss();
                            String res_kode     = response.getString("response");
                            if (res_kode.equals("200")){
                                FancyToast.makeText(ValidationActivity.this, "Berhasil merubah Nomor Telephone",
                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                sessionManager.editNotelp(getIntent().getStringExtra("noTelpBaru"));
                                startActivity(new Intent(ValidationActivity.this, MainActivity.class));
                                finish();
                            }
                            else{
                                FancyToast.makeText(ValidationActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            FancyToast.makeText(ValidationActivity.this, "Gagal merubah info profil\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        FancyToast.makeText(ValidationActivity.this, "Gagal merubah info profil\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        finish();
                    }
                });
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