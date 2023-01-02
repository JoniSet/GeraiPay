package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.android.material.textfield.TextInputEditText;
import com.optima.gerai_pay.Adapter.AdapterSpinnerKabupaten;
import com.optima.gerai_pay.Adapter.AdapterSpinnerKecamatan;
import com.optima.gerai_pay.Adapter.AdapterSpinnerProvinsi;
import com.optima.gerai_pay.Helper.Loading;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Model.Kabupaten;
import com.optima.gerai_pay.Model.Kecamatan;
import com.optima.gerai_pay.Model.Province;
import com.optima.gerai_pay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
import static com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etName, etPhone, etEmail, etPassword, etRefferal, etConfirmPassword, edt_alamat;
    View view_alamat ;
    boolean alamat = false;
    private LinearLayout linear_alamat;
    private Spinner spinner_provinsi, spinner_kabupaten, spinner_kecamatan;

    Loading loading     = new Loading();
    Dialog dialog;

    private ArrayList<Province> list_provinsi   = new ArrayList<>();
    private ArrayList<Kabupaten> list_kabupaten = new ArrayList<>();
    private ArrayList<Kecamatan> list_kecamatan = new ArrayList<>();

    Province prov_item;
    Kabupaten kab_item;
    Kecamatan kec_item;

    String id_provinsi, id_kab, id_kec, nama_provinsi = "", nama_kab = "", nama_kec = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dialog      = loading.dialog_helper(RegisterActivity.this);

        etName      = findViewById(R.id.etName);
        etPhone     = findViewById(R.id.etPhone);
        etEmail     = findViewById(R.id.etEmail);
        etPassword  = findViewById(R.id.etPassword);
        etRefferal  = findViewById(R.id.etRefferal);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        view_alamat = findViewById(R.id.view_alamat);
        linear_alamat = findViewById(R.id.linear_alamat);
        Button signUp = findViewById(R.id.signUp);
        Button login = findViewById(R.id.logIn);
        TextView tvConfirmPassword = findViewById(R.id.tvConfirmPassword);

        spinner_kecamatan = findViewById(R.id.spinner_kecamatan);
        spinner_kabupaten = findViewById(R.id.spinner_kabupaten);
        spinner_provinsi = findViewById(R.id.spinner_provinsi);

        login.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        signUp.setEnabled(!etName.getText().toString().equals("") ||
                !etPhone.getText().toString().equals("") ||
                !etEmail.getText().toString().equals("") ||
                !etPassword.getText().toString().equals("") ||
                !etConfirmPassword.getText().toString().equals(""));

        signUp.setOnClickListener(view -> Register());

        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Objects.requireNonNull(etConfirmPassword.getText()).toString().equals(Objects.requireNonNull(etPassword.getText()).toString())) {
                    tvConfirmPassword.setVisibility(View.VISIBLE);
                    tvConfirmPassword.setText("Password Sesuai");
                    tvConfirmPassword.setTextColor(getResources().getColor(R.color.blue));
                    signUp.setEnabled(true);
                } else if (etPassword.getText().toString().equals("") && etConfirmPassword.getText().toString().equals("")) {
                    tvConfirmPassword.setVisibility(View.GONE);
                    signUp.setEnabled(false);
                } else if (etConfirmPassword.getText().toString().equals("")) {
                    tvConfirmPassword.setVisibility(View.GONE);
                    signUp.setEnabled(false);
                } else {
                    tvConfirmPassword.setVisibility(View.VISIBLE);
                    tvConfirmPassword.setText("Password Tidak Sesuai !");
                    etConfirmPassword.setError("Password Tidak Sesuai !");
                    tvConfirmPassword.setTextColor(getResources().getColor(R.color.red));
                    signUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        view_alamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alamat == false){
                    linear_alamat.setVisibility(View.VISIBLE);
                    alamat = true;
                    if (nama_provinsi.isEmpty()) {
                        provinsi();
                    }
                }
                else{
                    linear_alamat.setVisibility(View.GONE);
                    alamat = false;
                }
            }
        });



        spinner_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                prov_item       = (Province) parent.getItemAtPosition(position);
                id_provinsi     = prov_item.getProvince_id();
                nama_provinsi   = prov_item.getProvince();

                dialog.show();
                kabupaten(id_provinsi);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_kabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                kab_item       = (Kabupaten) parent.getItemAtPosition(position);
                id_kab          = kab_item.getKabaupatenid();
                nama_kab        = kab_item.getKabputen();

                dialog.show();
                kecamatan(id_kab);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_kecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                kec_item       = (Kecamatan) parent.getItemAtPosition(position);
                id_kec         = kec_item.getKecamatanid();
                nama_kec        = kec_item.getKecamatan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Register(){

        final String nama = Objects.requireNonNull(this.etName.getText()).toString().trim();
        final String email = Objects.requireNonNull(this.etEmail.getText()).toString().trim();
        final String handphone = Objects.requireNonNull(this.etPhone.getText()).toString().trim();
        final String password = Objects.requireNonNull(this.etPassword.getText()).toString().trim();

        if (nama.matches("")){
            toastIcon("Anda belum mengisi nama !", R.drawable.ic_close, android.R.color.holo_red_dark);
            return;
        }
        if (email.matches("")){
            toastIcon("Anda belum mengisi email !", R.drawable.ic_close, android.R.color.holo_red_dark);
            return;
        }

        if (handphone.matches("")){
            toastIcon("Anda belum mengisi Nomor Handphone !", R.drawable.ic_close, android.R.color.holo_red_dark);
            return;
        }

        if (password.matches("")){
            toastIcon("Anda belum mengisi passowrd !", R.drawable.ic_close, android.R.color.holo_red_dark);
            return;
        }
        if (nama_provinsi.isEmpty()){
            toastIcon("Anda belum mengisi alamat !", R.drawable.ic_close, android.R.color.holo_red_dark);
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        String URL_REGISTER = Utils.BASE_URL + "register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");

                        if (responseCode.equals("201")){
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        } else if (responseCode.equals("202")) {
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                            dialog.dismiss();

                        }  else {
                            toastIcon(message, R.drawable.ic_checklist, R.color.green_forest_primary);
                            String otp_id = jsonObject.getString("otp_id");
                            String notelp = jsonObject.getString("notelp");
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    Intent intent = new Intent(RegisterActivity.this, ValidationActivity.class);
                                    intent.putExtra("otpID", otp_id);
                                    intent.putExtra("noTelp", notelp);
                                    intent.putExtra("kode_verifikasi", "1");
                                    startActivity(intent);
                                    finish();
                                }
                            }, 3000);
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Registrasi Gagal!" + e.toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                },
                error -> {
                    Toast.makeText(RegisterActivity.this, "Registrasi Gagal!" + error.toString(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
        {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", etName.getText().toString());
                params.put("email",etEmail.getText().toString());
                params.put("notelp", etPhone.getText().toString());
                params.put("password", etPassword.getText().toString());
                params.put("kode_reff", etRefferal.getText().toString());
                params.put("provinsi", nama_provinsi);
                params.put("kabupaten", nama_kab);
                params.put("kecamatan", nama_kec);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

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

    private void provinsi() {
        dialog.show();
        list_provinsi.clear();
        spinner_provinsi.setAdapter(null);
        AndroidNetworking.get("https://api.rajaongkir.com/starter/province")
                .addHeaders("key", "32b2e7fcd3e3bc30c1e006c928617c0e")
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj_raja_ongkir  = new JSONObject(response.getString("rajaongkir"));
                            JSONObject obj_status       = new JSONObject(obj_raja_ongkir.getString("status"));

                            String code                 = obj_status.getString("code");

                            if (code.equals("200")){
                                JSONArray array         = obj_raja_ongkir.getJSONArray("results");

                                for (int i = 0; i < array.length(); i++){
                                    JSONObject object   = array.getJSONObject(i);
                                    String id           = object.getString("province_id");
                                    String nama_prov    = object.getString("province");

                                    Province data       = new Province(nama_prov, id);
                                    list_provinsi.add(data);
                                }

                                AdapterSpinnerProvinsi adapterSpinnerProvinsi = new AdapterSpinnerProvinsi(RegisterActivity.this, list_provinsi);
                                spinner_provinsi.setAdapter(adapterSpinnerProvinsi);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("Salah", e.getMessage());
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(RegisterActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Salah", anError.getMessage());
                        dialog.dismiss();
                    }
                });
    }

    private void kabupaten(String id_provinsi) {
        list_kabupaten.clear();
        spinner_kabupaten.setAdapter(null);
        AndroidNetworking.get("https://pro.rajaongkir.com/api/city?province=" + id_provinsi)
                .addHeaders("key", "32b2e7fcd3e3bc30c1e006c928617c0e")
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj_raja_ongkir  = new JSONObject(response.getString("rajaongkir"));
                            JSONObject obj_status       = new JSONObject(obj_raja_ongkir.getString("status"));

                            String code                 = obj_status.getString("code");

                            if (code.equals("200")){
                                JSONArray array         = obj_raja_ongkir.getJSONArray("results");

                                for (int i = 0; i < array.length(); i++){
                                    JSONObject object   = array.getJSONObject(i);
                                    String id           = object.getString("city_id");
                                    String nama_kab     = object.getString("type") + " " + object.getString("city_name");
                                    String kode_pos     = object.getString("postal_code");

                                    Kabupaten data      = new Kabupaten(id, nama_kab, kode_pos);
                                    list_kabupaten.add(data);
                                }

                                AdapterSpinnerKabupaten adapterSpinnerKabupaten = new AdapterSpinnerKabupaten(RegisterActivity.this, list_kabupaten);
                                spinner_kabupaten.setAdapter(adapterSpinnerKabupaten);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("Salah", e.getMessage());
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(RegisterActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Salah", anError.getMessage());
                        dialog.dismiss();
                    }
                });
    }

    private void kecamatan(String id_kab) {
        list_kecamatan.clear();
        spinner_kecamatan.setAdapter(null);
        AndroidNetworking.get("https://pro.rajaongkir.com/api/subdistrict?city=" + id_kab)
                .addHeaders("key", "32b2e7fcd3e3bc30c1e006c928617c0e")
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj_raja_ongkir  = new JSONObject(response.getString("rajaongkir"));
                            JSONObject obj_status       = new JSONObject(obj_raja_ongkir.getString("status"));

                            String code                 = obj_status.getString("code");

                            if (code.equals("200")){
                                JSONArray array         = obj_raja_ongkir.getJSONArray("results");

                                for (int i = 0; i < array.length(); i++){
                                    JSONObject object   = array.getJSONObject(i);
                                    String id           = object.getString("subdistrict_id");
                                    String nama_kab     = object.getString("subdistrict_name");

                                    Kecamatan data      = new Kecamatan(id, nama_kab);
                                    list_kecamatan.add(data);
                                }

                                AdapterSpinnerKecamatan adapterSpinnerKecamatan = new AdapterSpinnerKecamatan(RegisterActivity.this, list_kecamatan);
                                spinner_kecamatan.setAdapter(adapterSpinnerKecamatan);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("Salah", e.getMessage());
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(RegisterActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Salah", anError.getMessage());
                        dialog.dismiss();
                    }
                });
    }
}