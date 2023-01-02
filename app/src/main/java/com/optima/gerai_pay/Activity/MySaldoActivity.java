package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.optima.gerai_pay.Adapter.HistoryMutasiAdapter;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Model.MutasiSaldo;
import com.optima.gerai_pay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MySaldoActivity extends AppCompatActivity {

    private RecyclerView mutasiRecView;
    SwipeRefreshLayout swLayout;
    private HistoryMutasiAdapter adapterHistory;
    private ArrayList<MutasiSaldo> mutasiSaldos;
    private LinearLayout panelData, panel404;

    String getId;
    String getToken;
    String getSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_saldo);

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        getSaldo = user.get(SessionManager.SALDO);
        getToken = user.get(SessionManager.TOKEN);

        Button topup = findViewById(R.id.top_up);
        TextView saldo = findViewById(R.id.saldo_value);
        ImageView backButton = findViewById(R.id.backBtn);
        panelData = findViewById(R.id.panelData);
        panel404 = findViewById(R.id.panel404);
        swLayout = findViewById(R.id.swlayout);

        mutasiSaldos = new ArrayList<>();
        mutasiRecView = findViewById(R.id.mutasiRecView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mutasiRecView.setLayoutManager(layoutManager);
        mutasiRecView.setHasFixedSize(true);

        swLayout.setColorSchemeResources(R.color.red,R.color.blue);

        swLayout.setOnRefreshListener(() -> {
            swLayout.setRefreshing(false);
            getMutasi();
        });

        getMutasi();

        Utils formatRupiah = new Utils();
        saldo.setText(formatRupiah.formatRupiah(Double.parseDouble(String.valueOf(getSaldo))));

        backButton.setOnClickListener(v -> finish());

        topup.setOnClickListener(view -> {
            Intent intent =new Intent(MySaldoActivity.this, TopUpActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getMutasi(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Utils.BASE_URL + "mutasi";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("mutasi");
                        Gson gson = new Gson();
                        mutasiSaldos.clear();
                        if (jsonArray.length()==0) {
                            panelData.setVisibility(View.GONE);
                            panel404.setVisibility(View.VISIBLE);
                            swLayout.setVisibility(View.GONE);
                        } else {
                            for (int k = 0; k < jsonArray.length(); k++) {
                                JSONObject object = jsonArray.getJSONObject(k);
                                panelData.setVisibility(View.VISIBLE);
                                panel404.setVisibility(View.GONE);
                                swLayout.setVisibility(View.VISIBLE);
                                MutasiSaldo data = gson.fromJson(String.valueOf(object), MutasiSaldo.class);
                                mutasiSaldos.add(data);
                            }
                        }

                        adapterHistory = new HistoryMutasiAdapter(mutasiSaldos, MySaldoActivity.this);
                        mutasiRecView.setAdapter(adapterHistory);
                        Collections.reverse(mutasiSaldos);
                        progressDialog.dismiss();


                    } catch (JSONException e){
                        e.printStackTrace();
                        panel404.setVisibility(View.VISIBLE);
                        swLayout.setVisibility(View.GONE);
                    }
                },
                error -> {
                    panel404.setVisibility(View.VISIBLE);
                    swLayout.setVisibility(View.GONE);
                }
        ) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }
        };
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
    public void onResume() {
        super.onResume();
    }
}