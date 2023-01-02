package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.optima.gerai_pay.Adapter.HistorySaldoAdapter;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Tanggal;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.Model.HistorySaldoModel;
import com.optima.gerai_pay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TopUpActivity extends AppCompatActivity
//        implements TransactionFinishedCallback
{

    private EditText input_saldo;
    private LinearLayout panel404;
    private RecyclerView historyRecView;
    private HistorySaldoAdapter adapter;
    private ArrayList<HistorySaldoModel> historySaldoModels;
    private TextView txt_50, txt_100, txt_200, txt_500, txt_1000, txt_2000;
    private SwipeRefreshLayout swipe_saldo;

    String getId;
    String getToken;
    String total;

    Tanggal tanggal         = new Tanggal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        SessionManager sessionManager   = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user    = sessionManager.getUserDetail();
        getId                           = user.get(SessionManager.ID);
        getToken                        = user.get(SessionManager.TOKEN);

        input_saldo                     = findViewById(R.id.input_saldo);
        historyRecView                  = findViewById(R.id.history_rview);
        panel404                        = findViewById(R.id.panel404);
        Button pay                      = findViewById(R.id.pay);
        ImageView backButton            = findViewById(R.id.backBtn);
        swipe_saldo                     = findViewById(R.id.swipe_saldo);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopUpActivity.this, MainActivity.class));
                finish();
            }
        });

        input_saldo.addTextChangedListener(onTextChangedListener());

        pay.setOnClickListener(view -> {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

            total                       = String.format(input_saldo.getText().toString().replaceAll(",", ""));

            if (total.isEmpty()) {
                toastIcon("Anda belum memasukkan nominal top up !", R.drawable.ic_close, android.R.color.holo_red_dark);
            } else if (Integer.parseInt(total) < 50000  ) {
                toastIcon("Minimal Top Up Rp.50.000 !", R.drawable.ic_close, android.R.color.holo_red_dark);
            } else {
                clickPay();
            }
        });

        historySaldoModels = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        historyRecView.setLayoutManager(layoutManager);
        historyRecView.setHasFixedSize(true);

        swipe_saldo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHistory();
            }
        });

        getHistory();
//        makePayment();
        init_textTopupSaldo();
    }

    private void init_textTopupSaldo() {
        txt_50              = (TextView) findViewById(R.id.txt_50);
        txt_100             = (TextView) findViewById(R.id.txt_100);
        txt_200             = (TextView) findViewById(R.id.txt_200);
        txt_500             = (TextView) findViewById(R.id.txt_500);
        txt_1000            = (TextView) findViewById(R.id.txt_1000);
        txt_2000            = (TextView) findViewById(R.id.txt_2000);

        txt_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_50.setBackgroundResource(R.drawable.bg_rounded_hijau);
                txt_100.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_200.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_500.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_1000.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_2000.setBackgroundResource(R.drawable.bg_rounded_orange);

                Long longval            = Long.parseLong("50000");

                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String jumlah           = formatter.format(longval);

                total                   = "50000";
                input_saldo.setText(jumlah);
            }
        });

        txt_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_50.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_100.setBackgroundResource(R.drawable.bg_rounded_hijau);
                txt_200.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_500.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_1000.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_2000.setBackgroundResource(R.drawable.bg_rounded_orange);

                Long longval            = Long.parseLong("100000");

                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String jumlah           = formatter.format(longval);

                total                   = "100000";
                input_saldo.setText(jumlah);
            }
        });

        txt_200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_50.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_100.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_200.setBackgroundResource(R.drawable.bg_rounded_hijau);
                txt_500.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_1000.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_2000.setBackgroundResource(R.drawable.bg_rounded_orange);

                Long longval            = Long.parseLong("200000");

                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String jumlah           = formatter.format(longval);

                total                   = "200000";
                input_saldo.setText(jumlah);
            }
        });

        txt_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_50.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_100.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_200.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_500.setBackgroundResource(R.drawable.bg_rounded_hijau);
                txt_1000.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_2000.setBackgroundResource(R.drawable.bg_rounded_orange);

                Long longval            = Long.parseLong("500000");

                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String jumlah           = formatter.format(longval);

                total                   = "500000";
                input_saldo.setText(jumlah);
            }
        });

        txt_1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_50.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_100.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_200.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_500.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_1000.setBackgroundResource(R.drawable.bg_rounded_hijau);
                txt_2000.setBackgroundResource(R.drawable.bg_rounded_orange);

                Long longval            = Long.parseLong("1000000");

                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String jumlah           = formatter.format(longval);

                total                   = "1000000";
                input_saldo.setText(jumlah);
            }
        });

        txt_2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_50.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_100.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_200.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_500.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_1000.setBackgroundResource(R.drawable.bg_rounded_orange);
                txt_2000.setBackgroundResource(R.drawable.bg_rounded_hijau);

                Long longval            = Long.parseLong("2000000");

                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String jumlah           = formatter.format(longval);

                total                   = "2000000";
                input_saldo.setText(jumlah);
            }
        });
    }

    private void getHistory(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Utils.BASE_URL + "histori_deposit";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        JSONArray jsonArray = jsonObject.getJSONArray("history");
                        Gson gson = new Gson();
                        historySaldoModels.clear();

                        if (jsonArray.length()==0) {
                            historyRecView.setVisibility(View.GONE);
                            panel404.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();

                        } else {
                            for (int k = 0; k < jsonArray.length(); k++) {
                                historyRecView.setVisibility(View.VISIBLE);
                                panel404.setVisibility(View.GONE);
                                JSONObject object = jsonArray.getJSONObject(k);
                                HistorySaldoModel data = gson.fromJson(String.valueOf(object), HistorySaldoModel.class);
                                historySaldoModels.add(data);

                            }

                            adapter = new HistorySaldoAdapter(historySaldoModels, TopUpActivity.this);
                            Collections.reverse(historySaldoModels);
                            historyRecView.setAdapter(adapter);
                            adapter.setOnItemClickListener(new HistorySaldoAdapter.NewsOnClick() {
                                @Override
                                public void onClick(View v, int position) {
                                    Intent intent = new Intent(TopUpActivity.this, DetailTopUpHistory.class);
                                    intent.putExtra("orderId", historySaldoModels.get(position).getOrderId());
                                    startActivity(intent);
                                }
                            });

                            progressDialog.dismiss();
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                        toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark);
                    }
                },
                error -> toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark)
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

        swipe_saldo.setRefreshing(false);
    }

//    50649

//    private void makePayment(){
//        String client_key = SdkConfig.MERCHANT_CLIENT_KEY;
//        String base_url = SdkConfig.MERCHANT_BASE_CHECKOUT_URL;
//        SdkUIFlowBuilder.init()
//                .setContext(this)
//                .setMerchantBaseUrl(base_url)
//                .setClientKey(client_key)
//                .setTransactionFinishedCallback(this)
//                .enableLog(true)
//                .setColorTheme(new CustomColorTheme("#777777","#f77474" , "#3f0d0d"))
//                .buildSDK();
//    }

    private void clickPay(){
        //moota
        Intent intent           = new Intent(TopUpActivity.this, KonfirmasiTopupActivity.class);
//        Intent intent           = new Intent(TopUpActivity.this, TopupProsesActivity.class);
        intent.putExtra("nominal", input_saldo.getText().toString());
        startActivity(intent);

//        if (total.matches("")){
//            Toast.makeText(this,"Anda belum mengisi jumlah saldo",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        int valueInt = Integer.parseInt(total);
//
//        MidtransSDK.getInstance().setTransactionRequest(transactionRequest("101", valueInt));
//        MidtransSDK.getInstance().startPaymentUiFlow(TopUpActivity.this );
//
//        UIKitCustomSetting uisetting = new UIKitCustomSetting();
//        uisetting.setSkipCustomerDetailsPages(true);
//        MidtransSDK.getInstance().setUIKitCustomSetting(uisetting);
    }

//    public static TransactionRequest transactionRequest(String id, int amount){
//        String getProduk = "Top Up Saldo";
//        TransactionRequest request =  new TransactionRequest(System.currentTimeMillis() + " " , amount);
//        ItemDetails details = new ItemDetails(id, amount, 1, getProduk);
//
//        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
//        itemDetails.add(details);
//        request.setItemDetails(itemDetails);
//        CreditCard creditCard = new CreditCard();
//        creditCard.setSaveCard(false);
//        details.getId();
//
//        request.setCreditCard(creditCard);
//        return request;
//    }
//
//    @Override
//    public void onTransactionFinished(TransactionResult result) {
//        if(result.getResponse() != null){
//            switch (result.getStatus()){
//                case TransactionResult.STATUS_SUCCESS:
//                    toastIcon("Transaksi Sukses " + result.getResponse().getOrderId(), R.drawable.ic_checklist, R.color.green_forest_primary);
//                    postData(result.getResponse().getOrderId(), result.getResponse().getTransactionId());
//                    break;
//                case TransactionResult.STATUS_PENDING:
//                    toastIcon("Transaksi Pending, Segera Selesaikan Pembayaran Anda" + result.getResponse().getOrderId(), R.drawable.ic_checklist, android.R.color.holo_orange_light);
//                    postData(result.getResponse().getOrderId(), result.getResponse().getTransactionId());
//                    break;
//                case TransactionResult.STATUS_FAILED:
//                    toastIcon("Transaksi Gagal" + result.getResponse().getOrderId(), R.drawable.ic_close, android.R.color.holo_red_dark);
//                    postData(result.getResponse().getOrderId(), result.getResponse().getTransactionId());
//                    break;
//            }
//            result.getResponse().getValidationMessages();
//        }else if(result.isTransactionCanceled()){
//            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show();
//        }else{
//            if(result.getStatus().equalsIgnoreCase((TransactionResult.STATUS_INVALID))){
//                Toast.makeText(this, "Transaction Invalid" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
//                toastIcon("Transaksi Gagal!" + result.getResponse().getOrderId(), R.drawable.ic_close, android.R.color.holo_red_dark);
//            }else{
//                Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show();
//                toastIcon("Something Wrong", R.drawable.ic_close, android.R.color.holo_red_dark);
//            }
//        }
//    }

    private void postData(final String orderID, final String TransaksiID){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String URL = Utils.BASE_URL + "deposit";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");
                        if (responseCode.equals("200")) {
                            progressDialog.dismiss();
                            toastIcon(message, R.drawable.ic_checklist, R.color.green_forest_primary);
                            finish();
                        } else if (responseCode.equals("201")) {
                            progressDialog.dismiss();
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                            finish();
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        progressDialog.dismiss();
                        toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_checklist, android.R.color.holo_red_dark);

                    }
                },
                error -> {
                    progressDialog.dismiss();
                    toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_checklist, android.R.color.holo_red_dark);
                })

        {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("order_id", orderID);
                params.put("user_id", getId);
                params.put("transaksi_id", TransaksiID);
                params.put("produk", "Saldo PPOB");
                params.put("saldo", input_saldo.getText().toString());
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
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

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                input_saldo.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    input_saldo.setText(formattedString);
                    input_saldo.setSelection(input_saldo.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                input_saldo.addTextChangedListener(this);
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TopUpActivity.this, MainActivity.class));
        finish();
    }
}