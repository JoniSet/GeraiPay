package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailTopUpHistory extends AppCompatActivity {

    private TextView trxDate, orderID, trxStatus, paymentType, paymentSite, paymentCode, totalTopUp;
    String getToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_top_up_history);

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getToken = user.get(SessionManager.TOKEN);

        ImageView backButton = findViewById(R.id.backBtn);
        trxDate = findViewById(R.id. trxDate);
        orderID = findViewById(R.id.orderID);
        trxStatus = findViewById(R.id.trxStatus);
        paymentType = findViewById(R.id.paymentType);
        paymentSite = findViewById(R.id.paymentSite);
        paymentCode = findViewById(R.id.paymentCode);
        totalTopUp = findViewById(R.id.totalTopUp);

        getData();

        backButton.setOnClickListener(view -> finish());
    }

    private void getData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Intent intent = getIntent();
        final String getOrderID = intent.getStringExtra("orderId");

        String url = Utils.BASE_URL + "depositbyid";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    progressDialog.dismiss();
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("history");
                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject object = jsonArray.getJSONObject(k);
                            trxDate.setText(object.getString("tgl"));
                            orderID.setText(object.getString("order_id"));
                            paymentType.setText(object.getString("payment_type"));
                            paymentSite.setText(object.getString("bank"));
                            paymentCode.setText(object.getString("va_number"));

                            Utils formatRupiah = new Utils();
                            totalTopUp.setText(formatRupiah.formatRupiah(Double.parseDouble(object.getString("total"))));
                            trxStatus.setText(object.getString("status"));

                            if (object.getString("status").equals("sukses")) {
                                trxStatus.setTextColor(getResources().getColor(R.color.green_forest_primary));
                                trxStatus.setText(object.getString("status"));
                            } else if (object.getString("status").equals("proses")) {
                                trxStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                                trxStatus.setText(object.getString("status"));
                            } else if (object.getString("status").equals("gagal")) {
                                trxStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                                trxStatus.setText(object.getString("status"));
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark);
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark);
                })
        {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("order_id", getOrderID);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}