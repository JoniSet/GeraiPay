package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.optima.gerai_pay.Adapter.OperatorTagihanAdapter;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.Model.OperatorTagihan;
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

public class TransaksiPembayaranActivity extends AppCompatActivity {

    private TextInputEditText etIDPel;
    private Spinner spnProvider;
    private LinearLayout panelData, panelAvailable, panelUnavailable, panelOperator;
    private TextView namaProduk;
    private TextView nomorPelanggan;
    private TextView namaPelanggan;
    private TextView periodePembayaran;
    private TextView jumlahTagihan;
    private TextView biayaAdmin;
    private TextView jumlahBayar;
    private TextView message;
    private Button payBtn;
    private ImageView img_next;

    private final ArrayList<OperatorTagihan> operatorTagihans = new ArrayList<>();
    private final ArrayList<OperatorTagihanAdapter> operatorAdapters = new ArrayList<>();

    String getId;
    String getToken;
    String kodeProduk;
    String setJumlahBayar;
    String setIDTagihan;
    String setNamaProduk;
    String getNomorHp;

    public  static final int RequestPermissionCode  = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_pembayaran);

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        getToken = user.get(SessionManager.TOKEN);
        getNomorHp = user.get(SessionManager.NOTELP);

        etIDPel = findViewById(R.id.etIDPel);
        spnProvider = findViewById(R.id.spn_provider);
        panelData = findViewById(R.id.panelData);
        panelAvailable = findViewById(R.id.panelAvailable);
        panelUnavailable = findViewById(R.id.panelUnavailable);
        panelOperator = findViewById(R.id.panelOperator);
        payBtn = findViewById(R.id.payBtn);

        namaProduk  = findViewById(R.id.namaProduk);
        nomorPelanggan      = findViewById(R.id.nomorPelanggan);
        namaPelanggan       = findViewById(R.id.namaPelanggan);
        periodePembayaran   = findViewById(R.id.periodePembayaran);
        jumlahTagihan       = findViewById(R.id.jumlahTagihan);
        biayaAdmin  = findViewById(R.id.biayaAdmin);
        jumlahBayar = findViewById(R.id.jumlahBayar);
        message     = findViewById(R.id.message);
        img_next    = findViewById(R.id.img_next);
        TextView menuTitle  = findViewById(R.id.menuTitle);
        ImageView backBtn   = findViewById(R.id.backBtn);

        String getTitleName = getIntent().getStringExtra("titleMenu");
        menuTitle.setText(getTitleName);

        requestOperator();

        backBtn.setOnClickListener(v -> finish());

        etIDPel.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                checkPayment();
                panelOperator.setVisibility(View.VISIBLE);
                return true;
            }
            return false;
        });


        etIDPel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etIDPel.getText().toString().length() > 5){
                    img_next.setVisibility(View.VISIBLE);
                }
                else {
                    img_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        payBtn.setOnClickListener(v -> {
            Intent intent = new Intent(TransaksiPembayaranActivity.this, KonfirmasiTransaksiPembayaran.class);
            intent.putExtra("jumlahBayar", setJumlahBayar);
            intent.putExtra("IDTransaksi", setIDTagihan);
            intent.putExtra("IDPelanggan", etIDPel.getText().toString());
            intent.putExtra("namaProduk", setNamaProduk);
            startActivity(intent);
        });

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPayment();
                panelOperator.setVisibility(View.VISIBLE);
            }
        });

    }

    private void requestOperator() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        String getCategoryId = getIntent().getStringExtra("categoryId");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String URL_READ = Utils.BASE_URL + "operator_tagihan";
        System.out.print(URL_READ);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                (Response.Listener<String>) response -> {
                    System.out.println(response);
                    dialog.dismiss();
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("operator");
                        Gson gson = new Gson();
                        for (int k = 0; k < jsonArray.length(); k++) {
                            dialog.dismiss();
                            JSONObject object = jsonArray.getJSONObject(k);
                            OperatorTagihan data = gson.fromJson(String.valueOf(object), OperatorTagihan.class);
                            String strOperatorID = object.getString("kode_produk");
                            String strNamaProduk = object.getString("nama_produk");
                            operatorTagihans.add(data);
                            operatorAdapters.add(new OperatorTagihanAdapter(strNamaProduk, strOperatorID));

                            ArrayAdapter<OperatorTagihanAdapter> spinnerArrayAdapter = new ArrayAdapter<>(TransaksiPembayaranActivity.this,  R.layout.item_spinner, operatorAdapters);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnProvider.setAdapter(spinnerArrayAdapter);
                            dialog.dismiss();
                            spnProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    OperatorTagihanAdapter swt = (OperatorTagihanAdapter) parent.getItemAtPosition(position);
                                    kodeProduk = (String) swt.tag;
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        toastIcon("Gagal memuat produk!\n" + e.getMessage(), R.drawable.ic_close, android.R.color.holo_red_dark);
                    }
                },
                (Response.ErrorListener) error -> {
                    dialog.dismiss();
                    toastIcon("Gagal memuat produk!\n" + error.getMessage(), R.drawable.ic_close, android.R.color.holo_red_dark);
                })
        {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kategori_id", getCategoryId);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void checkPayment(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        Log.d("Cek", kodeProduk + "\n" + getNomorHp + "\n" + Objects.requireNonNull(etIDPel.getText()).toString() + "\n" + MainActivity.token);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Utils.BASE_URL + "cek_tagihan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try{
                        final JSONObject jsonObject = new JSONObject(response);
                        String getMessage = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");

                        if (responseCode.equals("200") && getMessage.equals("Sukses")) {
                            dialog.dismiss();

                            setJumlahBayar = jsonObject.getString("jumlah_bayar");
                            setIDTagihan = jsonObject.getString("tagihan_id");
                            setNamaProduk = jsonObject.getString("produk");

                            panelData.setVisibility(View.VISIBLE);
                            panelUnavailable.setVisibility(View.GONE);
                            panelAvailable.setVisibility(View.VISIBLE);
                            payBtn.setVisibility(View.VISIBLE);
                            namaProduk.setText(jsonObject.getString("produk"));
                            nomorPelanggan.setText(jsonObject.getString("no_pelanggan"));
                            namaPelanggan.setText(jsonObject.getString("nama"));
                            periodePembayaran.setText(jsonObject.getString("periode"));

                            Utils formatRupiah = new Utils();
                            jumlahTagihan.setText(formatRupiah.formatRupiah(Double.parseDouble(String.valueOf(jsonObject.getString("jumlah_tagihan")))));
                            biayaAdmin.setText(formatRupiah.formatRupiah(Double.parseDouble(String.valueOf(jsonObject.getString("biaya_admin")))));
                            jumlahBayar.setText(formatRupiah.formatRupiah(Double.parseDouble(String.valueOf(setJumlahBayar))));

                        } else if (responseCode.equals("203") && getMessage.equals("Tagihan sudah terbayar")) {
                            dialog.dismiss();
                            panelData.setVisibility(View.VISIBLE);
                            panelUnavailable.setVisibility(View.VISIBLE);
                            panelAvailable.setVisibility(View.GONE);
                            payBtn.setVisibility(View.GONE);
                            message.setText(getMessage);
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        dialog.dismiss();
                        toastIcon("Gagal memuat produk!\n" + e.getMessage(), R.drawable.ic_close, android.R.color.holo_red_dark);
                    }
                },
                error -> {
                    dialog.dismiss();
                    toastIcon("Gagal memuat produk!\n" + error.getMessage(), R.drawable.ic_close, android.R.color.holo_red_dark);
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("kode_produk", kodeProduk);
                params.put("nomor", getNomorHp);
                params.put("no_pelanggan", Objects.requireNonNull(etIDPel.getText()).toString());
                return params;

            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}