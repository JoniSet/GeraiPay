package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

public class GantiPasswordActivity extends AppCompatActivity {
    private EditText edt_pass_baru, edt_konfirm_pass_baru;
    private Button btn_kirim_pass_baru;
    private Boolean hide            = true;
    private Boolean hide1           = true;
    String nohp, otp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);

        initView();

    }

    private void initView() {
        edt_pass_baru           = findViewById(R.id.edt_pass_baru);
        edt_konfirm_pass_baru   = findViewById(R.id.edt_konfirm_pass_baru);
        btn_kirim_pass_baru     = findViewById(R.id.btn_kirim_pass_baru);

        Intent intent           = getIntent();
        nohp                    = intent.getStringExtra("nohp");
        otp_id                  = intent.getStringExtra("otp_id");

        settingButton();
    }

    private void settingButton() {
        btn_kirim_pass_baru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_pass_baru.getText().toString().isEmpty()){
                    FancyToast.makeText(GantiPasswordActivity.this, "Mohon masukkan password baru anda!", FancyToast.LENGTH_SHORT, FancyToast.ERROR
                            , false).show();
                }
                else if (!edt_konfirm_pass_baru.getText().toString().equals(edt_pass_baru.getText().toString())){
                    FancyToast.makeText(GantiPasswordActivity.this, "Konfirmasi Password tidak sama!", FancyToast.LENGTH_SHORT, FancyToast.ERROR
                            , false).show();
                }
                else{
                    if (getIntent().getStringExtra("kode").equals("1")){
                        gantiPassword();
                    }
                    else{
                        gantiPassBaru();
                    }
                }
            }
        });
    }

    private void gantiPassword() {
        Dialog dialog           = new Dialog(GantiPasswordActivity.this);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        TextView txt_title      = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Mengganti Password");

        AndroidNetworking.post(Utils.BASE_URL + "save_password")
                .addBodyParameter("notelp", nohp)
                .addBodyParameter("passwordbaru", edt_pass_baru.getText().toString())
                .setPriority(Priority.MEDIUM)
                .setTag("Ganti")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_kode     = response.getString("response");
                            if (res_kode.equals("200")){
                                FancyToast.makeText(
                                        GantiPasswordActivity.this,
                                        "password Berhasil Diganti!",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        false
                                ).show();

                                dialog.dismiss();
                                startActivity(new Intent(GantiPasswordActivity.this, LoginActivity.class));
                                finish();
                            }
                            else{
                                FancyToast.makeText(
                                        GantiPasswordActivity.this,
                                        "password GAGAL Diganti!",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();

                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(
                                    GantiPasswordActivity.this,
                                    "Ganti Password Gagal, Kesalahan Request!",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.ERROR,
                                    false
                            ).show();

                            Log.d("Ganti Pass", e.getMessage());

                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        FancyToast.makeText(
                                GantiPasswordActivity.this,
                                "Ganti Password Gagal, Periksa Koneksi Internet Anda!",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR,
                                false
                        ).show();

                        Log.d("Ganti", anError.toString());

                        dialog.dismiss();
                    }
                });
    }

    private void gantiPassBaru(){
        Dialog dialog           = new Dialog(GantiPasswordActivity.this);
        dialog.setContentView(R.layout.loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        TextView txt_title      = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Mengganti Password");

        AndroidNetworking.post(Utils.BASE_URL + "ganti_password")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .addBodyParameter("password_lama", getIntent().getStringExtra("password_lama"))
                .addBodyParameter("password_baru", edt_pass_baru.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            dialog.dismiss();
                            String res_kode     = response.getString("response");
                            if (res_kode.equals("200")){
                                FancyToast.makeText(GantiPasswordActivity.this, "Berhasil merubah password",
                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                startActivity(new Intent(GantiPasswordActivity.this, MainActivity.class));
                                finish();
                            }
                            else{
                                FancyToast.makeText(GantiPasswordActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            FancyToast.makeText(GantiPasswordActivity.this, "Gagal merubah info profil\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        FancyToast.makeText(GantiPasswordActivity.this, "Gagal merubah info profil\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        finish();
                    }
                });
    }
}