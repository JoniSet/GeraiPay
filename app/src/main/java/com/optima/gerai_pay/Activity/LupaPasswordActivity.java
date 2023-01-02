package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

public class LupaPasswordActivity extends AppCompatActivity {

    private EditText edt_email_lupa_pass;
    private Button btn_kirim_lupa_pass;
    private String otp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        initView();
    }

    private void initView() {
        edt_email_lupa_pass     = findViewById(R.id.edt_email_lupa_pass);
        btn_kirim_lupa_pass     = findViewById(R.id.btn_kirim_lupa_pass);

        btn_kirim_lupa_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_email_lupa_pass.getText().toString().isEmpty()){
                    FancyToast.makeText(LupaPasswordActivity.this, "Masukkan Email anda!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else {
                    lupaPass();
                }
            }
        });
    }

    private void lupaPass() {
        Dialog dialog           = new Dialog(LupaPasswordActivity.this);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        TextView txt_title      = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Mengirim Permintaan . . .");

        AndroidNetworking.post(Utils.BASE_URL + "forgot_password")
                .addBodyParameter("email", edt_email_lupa_pass.getText().toString())
                .setPriority(Priority.MEDIUM)
                .setTag("Forgot")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200")){
                                reqOTP(dialog, response.getString("nohp"));
                            }
                            else{
                                FancyToast.makeText(LupaPasswordActivity.this, response.getString("message"), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(LupaPasswordActivity.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        FancyToast.makeText(LupaPasswordActivity.this, anError.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        dialog.dismiss();
                    }
                });
    }

    private void reqOTP(Dialog dialog, String nohp){
        AndroidNetworking.post(Utils.BASE_URL + "otp")
                .addBodyParameter("notelp", nohp)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_kode     = response.getString("response");
                            if (res_kode.equals("200")){
                                otp_id          = response.getString("otp_id");
                                Intent intent   = new Intent(LupaPasswordActivity.this, ValidationActivity.class);
                                intent.putExtra("otpID", otp_id);
                                intent.putExtra("nohp", nohp);
                                intent.putExtra("kode_verifikasi", "4");
                                startActivity(intent);
                                dialog.dismiss();
                            }else if (res_kode.equals("201")){
                                FancyToast.makeText(
                                        LupaPasswordActivity.this,
                                        response.getString("message"),
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();
                                dialog.dismiss();
                            }else if (res_kode.equals("203")){
                                FancyToast.makeText(
                                        LupaPasswordActivity.this,
                                        response.getString("message"),
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();
                                dialog.dismiss();
                            }else {
                                FancyToast.makeText(
                                        LupaPasswordActivity.this,
                                        response.getString("message"),
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(LupaPasswordActivity.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        FancyToast.makeText(LupaPasswordActivity.this, anError.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        dialog.dismiss();
                    }
                });
    }
}