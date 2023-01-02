package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.optima.gerai_pay.Adapter.AdapterLaporanKomisi;
import com.optima.gerai_pay.Adapter.AdapterLaporanPenjualan;
import com.optima.gerai_pay.Adapter.HistoryKomisiAdapter;
import com.optima.gerai_pay.Helper.MonthPicker;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Tanggal;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Helper.YearPicker;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.Model.ListKomisiItem;
import com.optima.gerai_pay.Model.ListLaporanKomisi;
import com.optima.gerai_pay.Model.ListLaporanPenjualan;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class KomisiActivity extends AppCompatActivity {

    private TextView komisiValue, txt_filter_hari, txt_filter_bulan, txt_filter_tahun, txt_tanggal, txt_detail_bulan, txt_jam,
            txt_produk, txt_total, txt_detail_bulan_komisi, txt_produk_komisi, txt_total_komisi, txt_total_penjualan_komisi;
    private LinearLayout panelData, panel404, linear_komisi;
    private RecyclerView histroyRview, list_rekapan, list_rekapan_komisi;

    private HistoryKomisiAdapter adapter;
    private final ArrayList<ListKomisiItem> listKomisiItems = new ArrayList<>();

    private ArrayList<ListLaporanPenjualan> listLaporanPenjualan = new ArrayList<>();
    private ArrayList<ListLaporanKomisi> listLaporanKomisi = new ArrayList<>();
    private AdapterLaporanPenjualan adapterLaporanPenjualan;
    private AdapterLaporanKomisi adapterLaporanKomisi;

    public static String getToken, filter_tipe = "hari";

    private DatePickerDialog.OnDateSetListener dateSetListener;
    MonthPicker monthPicker     = new MonthPicker();
    YearPicker yearPicker     = new YearPicker();
    int mYear, mMonth, mDay,
            total_keuntungan_komisi = 0,
            total_keuntungan_penjualan = 0,
            total_penjualan_komisi = 0;

    Tanggal tanggal = new Tanggal();

    int sum_hari, sum_bulan, sum_tahun = 0, sum_hari2, sum_bulan2, sum_tahun2 = 0;
    String startDateStr, endDateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komisi);
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getToken = user.get(SessionManager.TOKEN);

        ImageView backBtn   = findViewById(R.id.backBtn);
        komisiValue         = findViewById(R.id.komisiValue);
        panelData           = findViewById(R.id.panelData);
        panel404            = findViewById(R.id.panel404);
        histroyRview        = findViewById(R.id.history_rview);

        backBtn.setOnClickListener(v -> finish());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        histroyRview.setLayoutManager(linearLayoutManager);
        histroyRview.setHasFixedSize(true);

        Calendar calendar   = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay  = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay   = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        startDateStr        = df.format(monthFirstDay);
        endDateStr          = df.format(monthLastDay);

        initView();
    }

    private void initView() {
        txt_filter_hari             = findViewById(R.id.txt_filter_hari);
        txt_filter_bulan            = findViewById(R.id.txt_filter_bulan);
        txt_filter_tahun            = findViewById(R.id.txt_filter_tahun);
        txt_tanggal                 = findViewById(R.id.txt_tanggal);
        txt_detail_bulan            = findViewById(R.id.txt_detail_bulan);
        txt_jam                     = findViewById(R.id.txt_jml_transaksi);
        txt_produk                  = findViewById(R.id.txt_produk);
        txt_total                   = findViewById(R.id.txt_total);
        txt_detail_bulan_komisi     = findViewById(R.id.txt_detail_bulan_komisi);
        txt_produk_komisi           = findViewById(R.id.txt_produk_komisi);
        txt_total_komisi            = findViewById(R.id.txt_total_komisi);
        txt_total_penjualan_komisi  = findViewById(R.id.txt_total_penjualan_komisi);

        list_rekapan                = findViewById(R.id.list_rekapan);
        list_rekapan_komisi         = findViewById(R.id.list_rekapan_komisi);

        linear_komisi               = findViewById(R.id.linear_komisi);

        setButton();
        getKomisiHari();
    }

    private void setButton() {
        txt_filter_hari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_filter_hari.setTextColor(KomisiActivity.this.getResources().getColor(R.color.putih));
                txt_filter_hari.setBackgroundResource(R.drawable.bg_rounded_putih);
                txt_filter_hari.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_FF7)));

                txt_filter_bulan.setTextColor(KomisiActivity.this.getResources().getColor(R.color.text_24));
                txt_filter_bulan.setBackgroundResource(R.drawable.bg_rounded_putih);
                txt_filter_bulan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.putih)));

                txt_filter_tahun.setTextColor(KomisiActivity.this.getResources().getColor(R.color.text_24));
                txt_filter_tahun.setBackgroundResource(R.drawable.bg_rounded_putih);
                txt_filter_tahun.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.putih)));

                filter_tipe = "hari";
                total_keuntungan_komisi = 0;
                total_keuntungan_penjualan = 0;
                getKomisiHari();
            }
        });

        txt_filter_bulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_filter_bulan.setTextColor(KomisiActivity.this.getResources().getColor(R.color.putih));
                txt_filter_bulan.setBackgroundResource(R.drawable.bg_rounded_putih);
                txt_filter_bulan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_FF7)));

                txt_filter_hari.setTextColor(KomisiActivity.this.getResources().getColor(R.color.text_24));
                txt_filter_hari.setBackgroundResource(R.drawable.bg_rounded_putih);
                txt_filter_hari.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.putih)));

                txt_filter_tahun.setTextColor(KomisiActivity.this.getResources().getColor(R.color.text_24));
                txt_filter_tahun.setBackgroundResource(R.drawable.bg_rounded_putih);
                txt_filter_tahun.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.putih)));

                filter_tipe = "bulan";
                total_keuntungan_komisi = 0;
                total_keuntungan_penjualan = 0;
                getKomisiBulan();
            }
        });

        txt_filter_tahun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_filter_tahun.setTextColor(KomisiActivity.this.getResources().getColor(R.color.putih));
                txt_filter_tahun.setBackgroundResource(R.drawable.bg_rounded_putih);
                txt_filter_tahun.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_FF7)));

                txt_filter_bulan.setTextColor(KomisiActivity.this.getResources().getColor(R.color.text_24));
                txt_filter_bulan.setBackgroundResource(R.drawable.bg_rounded_putih);
                txt_filter_bulan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.putih)));

                txt_filter_hari.setTextColor(KomisiActivity.this.getResources().getColor(R.color.text_24));
                txt_filter_hari.setBackgroundResource(R.drawable.bg_rounded_putih);
                txt_filter_hari.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.putih)));

                filter_tipe = "tahun";
                total_keuntungan_komisi = 0;
                total_keuntungan_penjualan = 0;
                getKomisiTahun();
            }
        });

//        txt_tanggal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (filter_tipe.equals("hari")){
//                    Calendar calendar   = Calendar.getInstance(TimeZone.getDefault());
//                    calendar.setTimeInMillis(System.currentTimeMillis());
//                    int tahun           = calendar.get(Calendar.YEAR);
//                    int bulan           = calendar.get(Calendar.MONTH);
//                    int hari            = calendar.get(Calendar.DAY_OF_MONTH);
//
//                    DatePickerDialog dialog     = new DatePickerDialog(KomisiActivity.this,
//                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                            dateSetListener,
//                            tahun, bulan, hari);
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    dialog.show();
//                }
//                else if (filter_tipe.equals("bulan")){
//                    monthPicker.setListener(new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
//                            String format = ""+year+"-"+month+"";
//                            Date date = new Date();
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
//                            SimpleDateFormat sisi             =new SimpleDateFormat("MMMM yyyy");
//                            try {
//                                date = simpleDateFormat.parse(format);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            txt_tanggal.setText(sisi.format(date));
//                        }
//                    });
//                    monthPicker.show(KomisiActivity.this.getSupportFragmentManager(), "MonthYearPickerDialog");
//                }
//                else{
//                    yearPicker.setListener(new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
//                            String format = String.valueOf(year);
//                            Date date = new Date();
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
//                            try {
//                                date = simpleDateFormat.parse(format);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            txt_tanggal.setText(simpleDateFormat.format(date));
//                        }
//                    });
//                    yearPicker.show(KomisiActivity.this.getSupportFragmentManager(), "MonthYearPickerDialog");
//                }
//            }
//        });

        dateSetListener     = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month           = month + 1;
                String tanggal  = year + "-" + month + "-" + dayOfMonth;

                String inputPattern = "yyyy-MM-dd";
                String outputPattern = "dd MMM yyyy";
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                Date date = null;
                String str = null;

                try {
                    date = inputFormat.parse(tanggal);
                    str = outputFormat.format(date);

                    txt_tanggal.setText(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void getKomisiHari(){
        listLaporanKomisi.clear();
        list_rekapan_komisi.setAdapter(null);

        listLaporanPenjualan.clear();
        list_rekapan.setAdapter(null);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        sum_hari    = 0;
        sum_hari2   = 0;

        AndroidNetworking.post(Utils.BASE_URL + "komisi_day")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .addBodyParameter("startdate", startDateStr)
                .addBodyParameter("enddate", endDateStr)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Hasil", response.toString());
                        progressDialog.dismiss();

                        try {
                            if (response.getString("response").equals("200")){
                                JSONArray array_topup       = response.getJSONArray("penjualan_topup");
                                JSONArray array_agen        = response.getJSONArray("penjualan_agen");

                                for (int i = 0; i < array_topup.length(); i++){
                                    JSONObject obj_topup    = array_topup.getJSONObject(i);
                                    ListLaporanPenjualan data_topup = new ListLaporanPenjualan(
                                            obj_topup.getString("tgl"),
                                            obj_topup.getString("transaksi"),
                                            obj_topup.getString("laba")
                                    );

                                    listLaporanPenjualan.add(data_topup);
                                }

                                adapterLaporanPenjualan     = new AdapterLaporanPenjualan(KomisiActivity.this, listLaporanPenjualan);
                                list_rekapan.setAdapter(adapterLaporanPenjualan);
                                list_rekapan.setLayoutManager(new LinearLayoutManager(KomisiActivity.this));
                                list_rekapan.setItemAnimator(new DefaultItemAnimator());
                                list_rekapan.setHasFixedSize(true);


                                for (int i = 0; i < array_agen.length(); i++){
                                    JSONObject obj_agen     = array_agen.getJSONObject(i);
                                    ListLaporanKomisi data_komisi   = new ListLaporanKomisi(
                                            obj_agen.getString("tgl"),
                                            obj_agen.getString("downline_name"),
                                            obj_agen.getString("laba")
                                    );

                                    listLaporanKomisi.add(data_komisi);
                                }

                                adapterLaporanKomisi     = new AdapterLaporanKomisi(KomisiActivity.this, listLaporanKomisi);
                                list_rekapan_komisi.setAdapter(adapterLaporanKomisi);
                                list_rekapan_komisi.setLayoutManager(new LinearLayoutManager(KomisiActivity.this));
                                list_rekapan_komisi.setItemAnimator(new DefaultItemAnimator());
                                list_rekapan_komisi.setHasFixedSize(true);

                                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                                formatter.applyPattern("#,###,###,###");
                                String formattedString = formatter.format(Long.parseLong(response.getString("total_komisi")));

                                txt_total_penjualan_komisi.setText(
                                        "Rp " + formattedString
                                );
                                txt_tanggal.setText(parseDate(tanggal.getTanggal(), "yyyy-MM-dd", "dd MMM yyyy"));


                                for (int a = 0; a < listLaporanPenjualan.size(); a++){
                                    sum_hari = sum_hari + Integer.parseInt(listLaporanPenjualan.get(a).getKomisi());
                                }
                                txt_total.setText(String.valueOf(sum_hari));

                                for (int b = 0; b < listLaporanKomisi.size(); b++){
                                    sum_hari2 = sum_hari2 + Integer.parseInt(listLaporanKomisi.get(b).getKomisi());
                                }
                                txt_total_komisi.setText(String.valueOf(sum_hari2));
                            }
                            else {
                                FancyToast.makeText(KomisiActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            FancyToast.makeText(KomisiActivity.this, "Gagal memuat komisi\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Hasil", anError.getMessage());
                        progressDialog.dismiss();
                        FancyToast.makeText(KomisiActivity.this, "Gagal memuat komisi\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
    }

    private void getKomisiBulan(){
        listLaporanKomisi.clear();
        list_rekapan_komisi.setAdapter(null);

        listLaporanPenjualan.clear();
        list_rekapan.setAdapter(null);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        sum_bulan   = 0;
        sum_bulan2  = 0;

        AndroidNetworking.post(Utils.BASE_URL + "komisi_month")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .addBodyParameter("startdate", "2022-01-01")
                .addBodyParameter("enddate", "2022-12-31")
                .setPriority(Priority.MEDIUM)
                .setTag("komisi_month")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Hasil", response.toString());
                        progressDialog.dismiss();
                        try {
                            if (response.getString("response").equals("200")){
                                JSONArray array_topup       = response.getJSONArray("penjualan_topup");
                                JSONArray array_agen        = response.getJSONArray("penjualan_agen");

                                for (int i = 0; i < array_topup.length(); i++){
                                    JSONObject obj_topup    = array_topup.getJSONObject(i);
                                    ListLaporanPenjualan data_topup = new ListLaporanPenjualan(
                                            obj_topup.getString("tgl"),
                                            obj_topup.getString("jml"),
                                            obj_topup.getString("laba")
                                    );

                                    listLaporanPenjualan.add(data_topup);
                                }

                                adapterLaporanPenjualan     = new AdapterLaporanPenjualan(KomisiActivity.this, listLaporanPenjualan);
                                list_rekapan.setAdapter(adapterLaporanPenjualan);
                                list_rekapan.setLayoutManager(new LinearLayoutManager(KomisiActivity.this));
                                list_rekapan.setItemAnimator(new DefaultItemAnimator());
                                list_rekapan.setHasFixedSize(true);

                                txt_detail_bulan.setVisibility(View.VISIBLE);
                                txt_jam.setVisibility(View.GONE);
                                txt_produk.setVisibility(View.VISIBLE);


                                for (int i = 0; i < array_agen.length(); i++){
                                    JSONObject obj_agen     = array_agen.getJSONObject(i);
                                    ListLaporanKomisi data_komisi   = new ListLaporanKomisi(
                                            obj_agen.getString("tgl"),
                                            obj_agen.getString("downline_name"),
                                            obj_agen.getString("laba")
                                    );

                                    listLaporanKomisi.add(data_komisi);
                                }

                                adapterLaporanKomisi     = new AdapterLaporanKomisi(KomisiActivity.this, listLaporanKomisi);
                                list_rekapan_komisi.setAdapter(adapterLaporanKomisi);
                                list_rekapan_komisi.setLayoutManager(new LinearLayoutManager(KomisiActivity.this));
                                list_rekapan_komisi.setItemAnimator(new DefaultItemAnimator());
                                list_rekapan_komisi.setHasFixedSize(true);

                                txt_detail_bulan_komisi.setVisibility(View.VISIBLE);
                                txt_produk_komisi.setVisibility(View.VISIBLE);

                                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                                formatter.applyPattern("#,###,###,###");
                                String formattedString = formatter.format(Long.parseLong(response.getString("total_komisi")));

                                txt_total_penjualan_komisi.setText(
                                        "Rp " + formattedString
                                );
                                txt_tanggal.setText(parseDate(response.getString("tgl"), "yyyy-MM", "MMM yyyy"));
                                txt_detail_bulan.setText("Tanggal");
                                txt_detail_bulan_komisi.setText("Tanggal");

                                for (int a = 0; a < listLaporanPenjualan.size(); a++){
                                    if (!listLaporanPenjualan.get(a).getKomisi().equals("null")) {
                                        sum_bulan = sum_bulan + Integer.parseInt(listLaporanPenjualan.get(a).getKomisi());
                                    }
                                }
                                txt_total.setText(String.valueOf(sum_bulan));

                                for (int b = 0; b < listLaporanKomisi.size();b++){
                                    sum_bulan2 = sum_bulan2 + Integer.parseInt(listLaporanKomisi.get(b).getKomisi());
                                }
                                txt_total_komisi.setText(String.valueOf(sum_bulan2));
                            }
                            else {
                                FancyToast.makeText(KomisiActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            FancyToast.makeText(KomisiActivity.this, "Gagal memuat komisi\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Hasil", anError.getMessage());
                        progressDialog.dismiss();
                        FancyToast.makeText(KomisiActivity.this, "Gagal memuat komisi\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
    }

    private void getKomisiTahun(){
        listLaporanKomisi.clear();
        list_rekapan_komisi.setAdapter(null);

        listLaporanPenjualan.clear();
        list_rekapan.setAdapter(null);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        sum_tahun   = 0;
        sum_tahun2   = 0;

        AndroidNetworking.get(Utils.BASE_URL + "komisi_year")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .setPriority(Priority.MEDIUM)
                .setTag("komisi_year")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Hasil", response.toString());
                        progressDialog.dismiss();

                        try {
                            if (response.getString("response").equals("200")){
                                JSONArray array_topup       = response.getJSONArray("penjualan_topup");
                                JSONArray array_agen        = response.getJSONArray("penjualan_agen");

                                for (int i = 0; i < array_topup.length(); i++){
                                    JSONObject obj_topup    = array_topup.getJSONObject(i);
                                    ListLaporanPenjualan data_topup = new ListLaporanPenjualan(
                                            obj_topup.getString("tgl"),
                                            obj_topup.getString("nama_produk"),
                                            obj_topup.getString("komisi")
                                    );

                                    listLaporanPenjualan.add(data_topup);
                                }

                                adapterLaporanPenjualan     = new AdapterLaporanPenjualan(KomisiActivity.this, listLaporanPenjualan);
                                list_rekapan.setAdapter(adapterLaporanPenjualan);
                                list_rekapan.setLayoutManager(new LinearLayoutManager(KomisiActivity.this));
                                list_rekapan.setItemAnimator(new DefaultItemAnimator());
                                list_rekapan.setHasFixedSize(true);

                                txt_detail_bulan.setVisibility(View.VISIBLE);
                                txt_jam.setVisibility(View.GONE);
                                txt_produk.setVisibility(View.GONE);


                                for (int i = 0; i < array_agen.length(); i++){
                                    JSONObject obj_agen     = array_agen.getJSONObject(i);
                                    ListLaporanKomisi data_komisi   = new ListLaporanKomisi(
                                            obj_agen.getString("tgl"),
                                            obj_agen.getString("name"),
                                            obj_agen.getString("komisi")
                                    );

                                    listLaporanKomisi.add(data_komisi);
                                }

                                adapterLaporanKomisi     = new AdapterLaporanKomisi(KomisiActivity.this, listLaporanKomisi);
                                list_rekapan_komisi.setAdapter(adapterLaporanKomisi);
                                list_rekapan_komisi.setLayoutManager(new LinearLayoutManager(KomisiActivity.this));
                                list_rekapan_komisi.setItemAnimator(new DefaultItemAnimator());
                                list_rekapan_komisi.setHasFixedSize(true);

                                txt_detail_bulan_komisi.setVisibility(View.VISIBLE);
                                txt_produk_komisi.setVisibility(View.GONE);

                                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                                formatter.applyPattern("#,###,###,###");
                                String formattedString = formatter.format(Long.parseLong(response.getString("total_komisi")));

                                txt_total_penjualan_komisi.setText(
                                        "Rp " + formattedString
                                );
                                txt_tanggal.setText(response.getString("tgl").substring(0, 4));
                                txt_detail_bulan.setText("Bulan");
                                txt_detail_bulan_komisi.setText("Bulan");

                                for (int a = 0; a < listLaporanPenjualan.size(); a++){
                                    sum_tahun = sum_tahun + Integer.parseInt(listLaporanPenjualan.get(a).getKomisi());
                                }
                                txt_total.setText(String.valueOf(sum_tahun));

                                for (int b = 0; b < listLaporanKomisi.size(); b++){
                                    sum_tahun2 = sum_tahun2 + Integer.parseInt(listLaporanKomisi.get(b).getKomisi());
                                }
                                txt_total_komisi.setText(String.valueOf(sum_tahun2));
                            }
                            else {
                                FancyToast.makeText(KomisiActivity.this, response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            FancyToast.makeText(KomisiActivity.this, "Gagal memuat komisi\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        FancyToast.makeText(KomisiActivity.this, "Gagal memuat komisi\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        Log.d("Hasil", anError.getErrorDetail());
                    }
                });
    }

    public String parseDate(String time, String inputPattern, String outputPattern) {
//        String inputPattern = "yyyy-MM-dd HH:mm:ss";
//        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}