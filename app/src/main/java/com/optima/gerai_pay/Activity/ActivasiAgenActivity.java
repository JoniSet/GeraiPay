package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.optima.gerai_pay.Adapter.AlamatListAdapter;
import com.optima.gerai_pay.Adapter.PekerjaanListAdapter;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Helper.VolleyMultipartRequest;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.Model.AlamatItem;
import com.optima.gerai_pay.Model.PekerjaanItem;
import com.optima.gerai_pay.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.widget.Toast.LENGTH_SHORT;

public class ActivasiAgenActivity extends AppCompatActivity {

    private TextInputEditText etUserID, etEmail, etPassword, etNama, etNoHp,
                                etTanggalLahir, etTempatLahir, etAlamat, etProvinsi, etKabupaten,
                                etKecamatan, etKelurahan, etKodePos, etNpwp, etPekerjaan, etPendapatan,
                                etFbID, etIgID, etNoWa;
    private ImageView insertKtp, ktpImg, insertSelfie, selfieImg;
    private Spinner listJk;

    private final List<AlamatItem> alamatItem = new ArrayList<>();
    private AlamatListAdapter alamatAdapter;

    private final List<PekerjaanItem> pekerjaanItemList = new ArrayList<>();
    private PekerjaanListAdapter pekerjaanAdapter;

    private Bitmap imageKtp;
    private Bitmap imageSelfie;

    private static final int PERMISSION_REQUEST_CODE = 200;

    String apiToken;
    String idProvinsi;
    String idKabupaten;
    String idKecamatan;
    String idKelurahan;
    String idPekerjaan;

    final Calendar myCalendar = Calendar.getInstance();

    SessionManager sessionManager;
    String getId;
    String getToken;
    String getNama;
    String getTelp;
    String getEmail;
    String getSaldo;
    String getReffCode;
    String getAgen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activasi_agen);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        getToken = user.get(SessionManager.TOKEN);
        getNama = user.get(SessionManager.NAME);
        getTelp = user.get(SessionManager.NOTELP);
        getEmail = user.get(SessionManager.EMAIL);
        getSaldo = user.get(SessionManager.SALDO);
        getReffCode = user.get(SessionManager.REFFCODE);
        getAgen = user.get(SessionManager.AGEN);

        etUserID = findViewById(R.id.etUserID);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etNama = findViewById(R.id.etNama);
        etNoHp = findViewById(R.id.etNoHP);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etTempatLahir = findViewById(R.id.etTempatLahir);
        etAlamat = findViewById(R.id.etAlamat);
        etProvinsi = findViewById(R.id.etProvinsi);
        etKabupaten = findViewById(R.id.etKabupaten);
        etKecamatan = findViewById(R.id.etKecamatan);
        etKelurahan = findViewById(R.id.etKelurahan);
        etKodePos = findViewById(R.id.etKodePos);
        etNpwp = findViewById(R.id.etNpwp);
        etPekerjaan = findViewById(R.id.etPekerjaan);
        etPendapatan = findViewById(R.id.etPendapatan);
        etFbID = findViewById(R.id.etFbID);
        etIgID = findViewById(R.id.etIgID);
        etNoWa = findViewById(R.id.etNoWa);
        insertKtp = findViewById(R.id.insertKtp);
        ktpImg = findViewById(R.id.ktpImg);
        insertSelfie = findViewById(R.id.insertSelfie);
        selfieImg = findViewById(R.id.selfieImg);
        Button registerBtn = findViewById(R.id.registerBtn);
        listJk = findViewById(R.id.listJk);
        ImageView backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        etTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ActivasiAgenActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etProvinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken("Provinsi");
            }
        });

        etKabupaten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken("Kabupaten");
            }
        });

        etKecamatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken("Kecamatan");
            }
        });

        etKelurahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken("Kelurahan");
            }
        });

        etPekerjaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListPekerjaan();
            }
        });

        insertKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    showReminder("insertKtp");
                } else {
                    requestPermission();
                }
            }
        });

        insertSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    showReminder("insertSelfie");
                } else {
                    requestPermission();
                }
            }
        });

        ktpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    showReminder("insertKtp");
                } else {
                    requestPermission();
                }
            }
        });

        selfieImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    showReminder("insertSelfie");
                } else {
                    requestPermission();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAgen();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etTanggalLahir.setText(sdf.format(myCalendar.getTime()));
    }

    private void chooseFile(final String titleMenu) {

        if (titleMenu.equals("insertKtp")) {
            Intent insertKtp = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(insertKtp, 1);
        } else if (titleMenu.equals("insertSelfie")) {
            Intent insertSelfie = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(insertSelfie, 2);
        }
    }

    private void getToken(final String titleMenu){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        String URL_READ = Utils.RAJAONGKIR_API + "poe";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                response -> {
                    dialog.dismiss();
                    try{
                        JSONObject jsonObject = new JSONObject(response);

                        switch (titleMenu) {
                            case "Provinsi":
                                apiToken = jsonObject.getString("token");
                                getListProvinsi(titleMenu);
                                break;
                            case "Kabupaten":
                                apiToken = jsonObject.getString("token");
                                getListKabupaten(titleMenu);
                                break;
                            case "Kecamatan":
                                apiToken = jsonObject.getString("token");
                                getListKecamatan(titleMenu);
                                break;
                            case "Kelurahan":
                                apiToken = jsonObject.getString("token");
                                getListKelurahan(titleMenu);
                                break;
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                })
        {
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showDialog(final String titleMenu) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_list_alamat);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        final ImageView closeBtn = dialog.findViewById(R.id.backBtn);
        final TextView menuTitle = dialog.findViewById(R.id.menuTitle);
        final TextInputEditText etSearch = dialog.findViewById(R.id.etSearch);
        final ListView alamatListView = dialog.findViewById(R.id.alamatListView);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        menuTitle.setText(titleMenu);

        if (titleMenu.equals("Pekerjaan")) {
            alamatListView.setAdapter(pekerjaanAdapter);
        } else {
            alamatListView.setAdapter(alamatAdapter);
        }

        switch (titleMenu) {
            case "Provinsi":
                alamatListView.setOnItemClickListener((parent, view, position, id) -> {
                    etProvinsi.setText(alamatItem.get(position).getName());
                    idProvinsi = String.valueOf(alamatItem.get(position).getId());
                    dialog.dismiss();
                });
                break;
            case "Kabupaten":
                alamatListView.setOnItemClickListener((parent, view, position, id) -> {
                    etKabupaten.setText(alamatItem.get(position).getName());
                    idKabupaten = String.valueOf(alamatItem.get(position).getId());
                    dialog.dismiss();
                });
                break;
            case "Kecamatan":
                alamatListView.setOnItemClickListener((parent, view, position, id) -> {
                    etKecamatan.setText(alamatItem.get(position).getName());
                    idKecamatan = String.valueOf(alamatItem.get(position).getId());
                    dialog.dismiss();
                });
                break;
            case "Kelurahan":
                alamatListView.setOnItemClickListener((parent, view, position, id) -> {
                    etKelurahan.setText(alamatItem.get(position).getName());
                    idKelurahan = String.valueOf(alamatItem.get(position).getId());
                    dialog.dismiss();
                });
                break;
            case "Pekerjaan":
                alamatListView.setOnItemClickListener((parent, view, position, id) -> {
                    etPekerjaan.setText(pekerjaanItemList.get(position).getPekerjaan());
                    idPekerjaan = String.valueOf(pekerjaanItemList.get(position).getId());
                    dialog.dismiss();
                });
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void showReminder(final String titleMenu) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_reminder);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        final ImageView contohImg = dialog.findViewById(R.id.contohImg);
        final TextView message = dialog.findViewById(R.id.message);
        final Button cancelButton = dialog.findViewById(R.id.cancelButton);
        final Button confirmButton = dialog.findViewById(R.id.confirmButton);

        if (titleMenu.equals("insertKtp")) {
            contohImg.setImageResource(R.drawable.ktp_contoh);
            message.setText("Foto KTP Anda sesuai dengan petunjuk yang tersedia pada gambar di atas...");
        } else if (titleMenu.equals("insertSelfie")) {
            contohImg.setImageResource(R.drawable.selfie_contoh);
            message.setText("Foto sambil memegang KTP Anda sesuai dengan petunjuk yang tersedia pada gambar di atas...");
        }

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        confirmButton.setOnClickListener(v -> {
            chooseFile(titleMenu);
            dialog.dismiss();
        });
    }

    private void getListProvinsi(final String titleMenu){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL_READ = Utils.RAJAONGKIR_API + "MeP7c5ne" + apiToken + "/m/wilayah/provinsi";
        System.out.print(URL_READ);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_READ,null,
                response -> {
                    dialog.dismiss();
                    System.out.println(response);
                    try{
                        JSONArray jsonArray = response.getJSONArray("data");
                        Gson gson = new Gson();
                        alamatItem.clear();
                        for (int k = 0; k < jsonArray.length(); k++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(k);
                            AlamatItem data = gson.fromJson(String.valueOf(jsonObject), AlamatItem.class);
                            alamatItem.add(data);

                        }

                        alamatAdapter = new AlamatListAdapter(alamatItem,R.layout.item_list, ActivasiAgenActivity.this);
                        alamatAdapter.notifyDataSetChanged();
                        showDialog(titleMenu);

                        dialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getListKabupaten(final String titleMenu){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL_READ = Utils.RAJAONGKIR_API + "MeP7c5ne" + apiToken + "/m/wilayah/kabupaten?idpropinsi=" + idProvinsi;
        System.out.print(URL_READ);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_READ,null,
                response -> {
                    dialog.dismiss();
                    System.out.println(response);
                    try{
                        JSONArray jsonArray = response.getJSONArray("data");
                        Gson gson = new Gson();
                        alamatItem.clear();
                        for (int k = 0; k < jsonArray.length(); k++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(k);
                            AlamatItem data = gson.fromJson(String.valueOf(jsonObject), AlamatItem.class);
                            alamatItem.add(data);

                        }

                        alamatAdapter = new AlamatListAdapter(alamatItem,R.layout.item_list, ActivasiAgenActivity.this);
                        alamatAdapter.notifyDataSetChanged();
                        showDialog(titleMenu);

                        dialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getListKecamatan(final String titleMenu){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL_READ = Utils.RAJAONGKIR_API + "MeP7c5ne" + apiToken + "/m/wilayah/kecamatan?idkabupaten=" + idKabupaten;
        System.out.print(URL_READ);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_READ,null,
                response -> {
                    dialog.dismiss();
                    System.out.println(response);
                    try{
                        JSONArray jsonArray = response.getJSONArray("data");
                        Gson gson = new Gson();
                        alamatItem.clear();
                        for (int k = 0; k < jsonArray.length(); k++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(k);
                            AlamatItem data = gson.fromJson(String.valueOf(jsonObject), AlamatItem.class);
                            alamatItem.add(data);

                        }

                        alamatAdapter = new AlamatListAdapter(alamatItem,R.layout.item_list, ActivasiAgenActivity.this);
                        alamatAdapter.notifyDataSetChanged();
                        showDialog(titleMenu);

                        dialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                })
        {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getListKelurahan(final String titleMenu){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL_READ = Utils.RAJAONGKIR_API + "MeP7c5ne" + apiToken + "/m/wilayah/kelurahan?idkecamatan=" + idKecamatan;
        System.out.print(URL_READ);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_READ,null,
                response -> {
                    dialog.dismiss();
                    System.out.println(response);
                    try{
                        JSONArray jsonArray = response.getJSONArray("data");
                        Gson gson = new Gson();
                        alamatItem.clear();
                        for (int k = 0; k < jsonArray.length(); k++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(k);
                            AlamatItem data = gson.fromJson(String.valueOf(jsonObject), AlamatItem.class);
                            alamatItem.add(data);

                        }

                        alamatAdapter = new AlamatListAdapter(alamatItem,R.layout.item_list, ActivasiAgenActivity.this);
                        alamatAdapter.notifyDataSetChanged();
                        showDialog(titleMenu);

                        dialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                })
        {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getListPekerjaan(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL_READ = Utils.MISC_API + "pekerjaan";
        System.out.print(URL_READ);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_READ,null,
                response -> {
                    dialog.dismiss();
                    System.out.println(response);
                    try{
                        JSONArray jsonArray = response.getJSONArray("data");
                        Gson gson = new Gson();
                        pekerjaanItemList.clear();
                        for (int k = 0; k < jsonArray.length(); k++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(k);
                            PekerjaanItem data = gson.fromJson(String.valueOf(jsonObject), PekerjaanItem.class);
                            pekerjaanItemList.add(data);

                        }
                        pekerjaanAdapter = new PekerjaanListAdapter(pekerjaanItemList,R.layout.item_list, ActivasiAgenActivity.this);
                        pekerjaanAdapter.notifyDataSetChanged();
                        showDialog("Pekerjaan");

                        dialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(ActivasiAgenActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                })
        {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void registerAgen(){

        if (etUserID.equals("")){
            Toast.makeText(this,"Anda belum mengisi Username",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etEmail.equals("")){
            Toast.makeText(this,"Anda belum mengisi Email",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etPassword.equals("")){
            Toast.makeText(this,"Anda belum mengisi Password",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etNama.equals("")){
            Toast.makeText(this,"Anda belum mengisi Nama",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etNoHp.equals("")){
            Toast.makeText(this,"Anda belum mengisi Nomor Handphone",Toast.LENGTH_SHORT).show();
            return;
        }

        if (listJk.getSelectedItem().equals("Jenis Kelamin")){
            Toast.makeText(this,"Anda belum mengisi Password",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etTanggalLahir.equals("")){
            Toast.makeText(this,"Anda belum mengisi Tanggal Lahir",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etTempatLahir.equals("")){
            Toast.makeText(this,"Anda belum mengisi Tempat Lahir",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etAlamat.equals("")){
            Toast.makeText(this,"Anda belum mengisi Alamat",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etProvinsi.equals("")){
            Toast.makeText(this,"Anda belum mengisi Provinsi",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etKabupaten.equals("")){
            Toast.makeText(this,"Anda belum mengisi Kabupaten",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etKecamatan.equals("")){
            Toast.makeText(this,"Anda belum mengisi Kecamatan",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etKelurahan.equals("")){
            Toast.makeText(this,"Anda belum mengisi Kelurahan",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etKodePos.equals("")){
            Toast.makeText(this,"Anda belum mengisi Kode Pos",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etNpwp.equals("")){
            Toast.makeText(this,"Anda belum mengisi Nomor NPWP",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etPekerjaan.equals("")){
            Toast.makeText(this,"Anda belum mengisi Pekerjaan",Toast.LENGTH_SHORT).show();
            return;
        }

        if (etNoWa.equals("")){
            Toast.makeText(this,"Anda belum mengisi Nomor Whatsapp",Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        String URL_REGISTER = Utils.AGENT_URL + "add_user";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL_REGISTER,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(new String(response.data));
                        String message = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");

                        if (responseCode.equals("200")) {
                            sessionManager.createSession(getId, getToken, getNama, getTelp, getEmail, getSaldo, getReffCode, getAgen, jsonObject.getString("id_user"), MainActivity.provinsi, MainActivity.kabupaten, MainActivity.kecamatan);
                            Toast.makeText(ActivasiAgenActivity.this, "Registrasi Agen", Toast.LENGTH_SHORT).show();
                            getUpAgen(jsonObject.getString("id_user"));
                            finish();

                        } else {
                            Toast.makeText(ActivasiAgenActivity.this, message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(ActivasiAgenActivity.this, "Registrasi Agen Gagal!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                },
                error -> {
                    Toast.makeText(ActivasiAgenActivity.this, "Registrasi Agen Gagal!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })

        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid", Objects.requireNonNull(etUserID.getText()).toString());
                params.put("email", Objects.requireNonNull(etEmail.getText()).toString());
                params.put("password", Objects.requireNonNull(etPassword.getText()).toString());
                params.put("nama", Objects.requireNonNull(etNama.getText()).toString());
                params.put("mobile", Objects.requireNonNull(etNoHp.getText()).toString());
                params.put("jenis_kelamin", listJk.getSelectedItem().toString());
                params.put("tgl_lahir", Objects.requireNonNull(etTanggalLahir.getText()).toString());
                params.put("tempat_lahir", Objects.requireNonNull(etTempatLahir.getText()).toString());
                params.put("alamat_ktp", Objects.requireNonNull(etAlamat.getText()).toString());
                params.put("provinsi_ktp", Objects.requireNonNull(etProvinsi.getText()).toString());
                params.put("kabupaten_ktp", Objects.requireNonNull(etKabupaten.getText()).toString());
                params.put("kecamatan_ktp", Objects.requireNonNull(etKecamatan.getText()).toString());
                params.put("kelurahan_ktp", Objects.requireNonNull(etKelurahan.getText()).toString());
                params.put("kodepos_ktp", Objects.requireNonNull(etKodePos.getText()).toString());
                params.put("npwp", Objects.requireNonNull(etNpwp.getText()).toString());
                params.put("pekerjaan", Objects.requireNonNull(etPekerjaan.getText()).toString());
                params.put("pendapatan", Objects.requireNonNull(etPendapatan.getText()).toString());
                params.put("fb_id", Objects.requireNonNull(etFbID.getText()).toString());
                params.put("ig_id", Objects.requireNonNull(etIgID.getText()).toString());
                params.put("no_wa", Objects.requireNonNull(etNoWa.getText()).toString());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("foto_ktp", new DataPart(imagename + ".png", getFileDataFromDrawable(imageKtp)));
                params.put("selfie_ktp", new DataPart(imagename + ".png", getFileDataFromDrawable(imageSelfie)));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(volleyMultipartRequest);

    }

    private void getUpAgen(final String idUser) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Utils.BASE_URL + "up_agen";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");
                        if (responseCode.equals("200")) {
                            sessionManager.createSession(getId, getToken, getNama, getTelp, getEmail, getSaldo, getReffCode, "1", jsonObject.getString("id_agen"), MainActivity.provinsi, MainActivity.kabupaten, MainActivity.kecamatan);
                            Intent intent = new Intent(ActivasiAgenActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ActivasiAgenActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();


                    } catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(ActivasiAgenActivity.this, "Registrasi Agen Gagal!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(ActivasiAgenActivity.this, "Registrasi Agen Gagal!", Toast.LENGTH_SHORT).show()
        ) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_agen", idUser);
                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            Bitmap ktpImage = (Bitmap) data.getExtras().get("data");
            imageKtp = ktpImage;
            insertKtp.setVisibility(View.GONE);
            ktpImg.setVisibility(View.VISIBLE);
            ktpImg.setImageBitmap(ktpImage);

        }

        if(requestCode == 2 && resultCode == Activity.RESULT_OK)
        {
                Bitmap selfieImage = (Bitmap) data.getExtras().get("data");
                imageSelfie = selfieImage;
                insertSelfie.setVisibility(View.GONE);
                selfieImg.setVisibility(View.VISIBLE);
                selfieImg.setImageBitmap(selfieImage);
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted", LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied", LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        showMessageOKCancel("You need to allow access permissions",
                                (dialog, which) -> requestPermission());
                    }
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ActivasiAgenActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}