package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.optima.gerai_pay.Helper.DropwdownListHelper;
import com.optima.gerai_pay.Helper.Loading;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailDownlineActivity extends AppCompatActivity {
    private ImageView backBtn, img_edit;
    private TextView txt_nama, txt_email, txt_notelp, txt_markup, txt_saldo;
    private RelativeLayout relative_markup;
    private LinearLayout relative_edt_markup;
    private Spinner spinner_markup;
    private Button btn_batal, btn_simpan;

    DropwdownListHelper dropwdownListHelper;

    Loading loading     = new Loading();
    Dialog dialog;

    String markup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_downline);

        initView();
    }

    private void initView() {
        dialog                  = loading.dialog_helper(DetailDownlineActivity.this);

        relative_edt_markup     = findViewById(R.id.relative_edt_markup);
        relative_markup         = findViewById(R.id.relative_markup);
        txt_markup              = findViewById(R.id.txt_markup);
        txt_notelp              = findViewById(R.id.txt_notelp);
        txt_email               = findViewById(R.id.txt_email);
        txt_nama                = findViewById(R.id.txt_nama);
        txt_saldo               = findViewById(R.id.txt_saldo);
        backBtn                 = findViewById(R.id.backBtn);
        img_edit                = findViewById(R.id.img_edit);
        spinner_markup          = findViewById(R.id.spinner_nikah);

        btn_batal               = findViewById(R.id.btn_batal);
        btn_simpan              = findViewById(R.id.btn_simpan);

        dropwdownListHelper     = new DropwdownListHelper();

        setButton();
        setView();
        setDownline();
    }

    private void setView() {
        List<String> listMarkup             = dropwdownListHelper.listMarkup();
        ArrayAdapter<String> markupAdapter  = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listMarkup);

        spinner_markup.setAdapter(markupAdapter);
        spinner_markup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                markup  = spinner_markup.getSelectedItem().toString().substring(4, spinner_markup.getSelectedItem().toString().length());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setButton() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relative_edt_markup.setVisibility(View.VISIBLE);
                relative_markup.setVisibility(View.GONE);
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relative_edt_markup.setVisibility(View.GONE);
                relative_markup.setVisibility(View.VISIBLE);
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDownline();
            }
        });
    }

    private void setDownline() {
        dialog.show();
        AndroidNetworking.post(Utils.BASE_URL + "detail_downline")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .addBodyParameter("id_downline", getIntent().getStringExtra("id"))
                .setPriority(Priority.MEDIUM)
                .setTag("detail_downline")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Hasil", response.toString());
                            String res_kode     = response.getString("response");
                            String data         = response.getString("data");
                            if (res_kode.equals("200")){
                                JSONObject obj  = new JSONObject(data);
                                txt_nama.setText(obj.getString("name"));
                                txt_email.setText(obj.getString("email"));
                                txt_notelp.setText(obj.getString("notelp"));
                                txt_markup.setText(obj.getString("markup"));

                                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                                formatter.applyPattern("#,###,###,###");
                                String formattedString = formatter.format(Long.parseLong(obj.getString("saldo")));
                                txt_saldo.setText("Rp " + formattedString);
                            }
                            else{
                                finish();
                                FancyToast.makeText(DetailDownlineActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(DetailDownlineActivity.this,"Gagal memuat downline\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                            finish();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        FancyToast.makeText(DetailDownlineActivity.this,"Gagal memuat downline\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        dialog.dismiss();
                        finish();
                    }
                });
    }

    private void UpdateDownline() {
        dialog.show();
        AndroidNetworking.post(Utils.BASE_URL + "edit_downline")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .addBodyParameter("id_downline", getIntent().getStringExtra("id"))
                .addBodyParameter("markup", markup)
                .setPriority(Priority.MEDIUM)
                .setTag("edit_downline")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Hasil", response.toString());
                            String res_kode     = response.getString("response");
                            if (res_kode.equals("200")){
                                txt_markup.setText(markup);
                                relative_edt_markup.setVisibility(View.GONE);
                                relative_markup.setVisibility(View.VISIBLE);
                                FancyToast.makeText(DetailDownlineActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            }
                            else{
                                FancyToast.makeText(DetailDownlineActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(DetailDownlineActivity.this,"Gagal memuat downline\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        FancyToast.makeText(DetailDownlineActivity.this,"Gagal memuat downline\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        dialog.dismiss();
                    }
                });
    }
}