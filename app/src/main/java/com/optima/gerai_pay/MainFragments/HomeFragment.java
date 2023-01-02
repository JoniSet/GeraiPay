package com.optima.gerai_pay.MainFragments;

import static android.widget.Toast.LENGTH_SHORT;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.optima.gerai_pay.Activity.ChatActivity;
import com.optima.gerai_pay.Activity.TopUpActivity;
import com.optima.gerai_pay.Activity.TransaksiPembayaranActivity;
import com.optima.gerai_pay.Activity.TransaksiPembelianActivity;
import com.optima.gerai_pay.Adapter.AdapterImageSlider;
import com.optima.gerai_pay.Adapter.AdapterProdukPembayaran;
import com.optima.gerai_pay.Adapter.AdapterProdukPembelian;
import com.optima.gerai_pay.Adapter.AdapterSpinnerKabupaten;
import com.optima.gerai_pay.Adapter.AdapterSpinnerKecamatan;
import com.optima.gerai_pay.Adapter.AdapterSpinnerProvinsi;
import com.optima.gerai_pay.Adapter.AdapterTraining;
import com.optima.gerai_pay.BuildConfig;
import com.optima.gerai_pay.Helper.Loading;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.Model.DataTraining;
import com.optima.gerai_pay.Model.Kabupaten;
import com.optima.gerai_pay.Model.Kecamatan;
import com.optima.gerai_pay.Model.ListImageSlider;
import com.optima.gerai_pay.Model.ListProduk;
import com.optima.gerai_pay.Model.ListProdukPembayaran;
import com.optima.gerai_pay.Model.Province;
import com.optima.gerai_pay.R;
import com.optima.gerai_pay.VideoTutorialActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private TextView saldoTv;
    SessionManager sessionManager;
    String getToken;
    String getReffCode;
    String getAgen;
    String getIdAgen;
    private RecyclerView recycler_produk_pembelian, recycler_produk_pembayaran;

    private SwipeRefreshLayout swipe_home;

    ArrayList<ListProduk> produkPembelian = new ArrayList<>();
    ArrayList<ListProdukPembayaran> produkPembayran = new ArrayList<>();

    LinearLayout linear_home;

    ShimmerFrameLayout shimmer_produk, shimmer_produk2;
    ViewPager2 viewPagerSlider;
    ArrayList<ListImageSlider> listSlider   = new ArrayList<>();

    private RecyclerView recycler_training;

    private ArrayList<DataTraining> dataTraining    = new ArrayList<>();
    private AdapterTraining adapterTraining;

    private Handler slideHandler = new Handler();

    public  static final int RequestPermissionCode  = 1 ;
    private Spinner spinner_provinsi, spinner_kabupaten, spinner_kecamatan;

    Loading loading     = new Loading();
    Dialog dialogg;

    private ArrayList<Province> list_provinsi   = new ArrayList<>();
    private ArrayList<Kabupaten> list_kabupaten = new ArrayList<>();
    private ArrayList<Kecamatan> list_kecamatan = new ArrayList<>();

    Province prov_item;
    Kabupaten kab_item;
    Kecamatan kec_item;

    String id_provinsi, id_kab, id_kec, nama_provinsi = "", nama_kab = "", nama_kec = "";

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sessionManager = new SessionManager(requireContext());
        sessionManager.checkLogin();
        dialogg      = loading.dialog_helper(getContext());
        dialogg.setCancelable(true);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String getSaldo = user.get(SessionManager.SALDO);
        getToken        = user.get(SessionManager.TOKEN);
        getReffCode     = user.get(SessionManager.REFFCODE);
        getAgen         = user.get(SessionManager.AGEN);
        getIdAgen       = user.get(SessionManager.ID_AGEN);

        EnableRuntimePermission();

//        LinearLayout moreButton         = view.findViewById(R.id.moreButton);
        LinearLayout zenmasBtn          = view.findViewById(R.id.zenmasBtn);
        TextView isiSaldoButton           = view.findViewById(R.id.isiSaldoButton);
        saldoTv                         = view.findViewById(R.id.saldoTv);
        ImageView chatButton            = view.findViewById(R.id.chatButton);

        viewPagerSlider     = view.findViewById(R.id.viewPagerSlider);

        linear_home         = view.findViewById(R.id.linear_home);

        shimmer_produk      = view.findViewById(R.id.shimmer_produk);
        shimmer_produk2     = view.findViewById(R.id.shimmer_produk2);

        recycler_training   = view.findViewById(R.id.recycler_training);

        swipe_home          = view.findViewById(R.id.swipe_home);

        recycler_produk_pembelian   = view.findViewById(R.id.recycler_produk_pembelian);
        recycler_produk_pembayaran  = view.findViewById(R.id.recycler_produk_pembayaran);


        zenmasBtn.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://eformdemo.net/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), ChatActivity.class));
                getWa();

            }
        });

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formattedString = formatter.format(Long.parseLong(String.valueOf(getSaldo)));

        saldoTv.setText("Rp. " + formattedString);
        isiSaldoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TopUpActivity.class);
                startActivity(intent);
            }
        });

        settingImageSlider();
        settingLayout(viewPagerSlider, "slider");
        setDataTraining();
        setProdukPembelian();
        setProdukPembayaran();


        if (MainActivity.provinsi.isEmpty()){
            showDialogAlamat();
        }

        swipe_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSaldo();
            }
        });

        Log.d("provinsi", MainActivity.provinsi);

        checkPermission();
        cekVersi();

        return view;
    }

    private void setProdukPembelian() {
        shimmer_produk.setVisibility(View.VISIBLE);
        produkPembelian.clear();
        recycler_produk_pembelian.setAdapter(null);
        AndroidNetworking.get(Utils.BASE_URL + "kategori")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .setPriority(Priority.LOW)
                .setTag("kategori")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Hasil", response.toString());
                        try {
                            String res_code     = response.getString("response");
                            if (res_code.equals("200")){
                                JSONArray array = response.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++){
                                    JSONObject object   = array.getJSONObject(i);

                                    ListProduk data     = new ListProduk(
                                            object.getString("id"),
                                            object.getString("nama_kategori"),
                                            object.getString("status"),
                                            object.getString("logo")
                                    );

                                    if (object.getString("status").equals("1")) {
                                        produkPembelian.add(data);
                                    }
                                }

                                AdapterProdukPembelian adapterProdukPembelian = new AdapterProdukPembelian(getContext(), produkPembelian);
                                adapterProdukPembelian.setOnItemClickListener(new AdapterProdukPembelian.ProdukOnClick() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        Log.d("Hasil", produkPembelian.get(position).getNama_kategori() + "\n" + produkPembelian.get(position).getId());
                                        Intent intent = new Intent(getContext(), TransaksiPembelianActivity.class);
                                        intent.putExtra("titleMenu", produkPembelian.get(position).getNama_kategori());
                                        intent.putExtra("categoryId", produkPembelian.get(position).getId());
                                        startActivity(intent);
                                    }
                                });

                                recycler_produk_pembelian.setAdapter(adapterProdukPembelian);
                                recycler_produk_pembelian.setLayoutManager(new GridLayoutManager(getContext(), 4));
                                recycler_produk_pembelian.setItemAnimator(new DefaultItemAnimator());
                                recycler_produk_pembelian.setHasFixedSize(true);

                                shimmer_produk.setVisibility(View.GONE);
                            }
                            else{
                                FancyToast.makeText(getContext(), response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Hasil", e.getMessage());
                            FancyToast.makeText(getContext(), "Terjad kesalahan!\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Hasil", anError.getMessage());
                        FancyToast.makeText(getContext(), "Terjad kesalahan!\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
    }

    private void setProdukPembayaran() {
        shimmer_produk2.setVisibility(View.VISIBLE);
        produkPembayran.clear();
        recycler_produk_pembayaran.setAdapter(null);
        AndroidNetworking.get(Utils.BASE_URL + "kategori_tagihan")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .setPriority(Priority.LOW)
                .setTag("kategori_tagihan")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Hasil", response.toString());
                        try {
                            String res_code     = response.getString("response");
                            if (res_code.equals("200")){
                                JSONArray array = response.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++){
                                    JSONObject object   = array.getJSONObject(i);

                                    ListProdukPembayaran data     = new ListProdukPembayaran(
                                            object.getString("id"),
                                            object.getString("nama_kategori"),
                                            object.getString("status"),
                                            object.getString("logo")
                                    );

                                    if (object.getString("status").equals("1")) {
                                        produkPembayran.add(data);
                                    }
                                }

                                AdapterProdukPembayaran adapterProdukPembayaran = new AdapterProdukPembayaran(getContext(), produkPembayran);
                                adapterProdukPembayaran.setOnItemClickListener(new AdapterProdukPembayaran.ProdukPembayaranOnClick() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        Intent intent = new Intent(getContext(), TransaksiPembayaranActivity.class);
                                        intent.putExtra("titleMenu", produkPembayran.get(position).getNama_kategori());
                                        intent.putExtra("categoryId", produkPembayran.get(position).getId());
                                        startActivity(intent);
                                    }
                                });

                                recycler_produk_pembayaran.setAdapter(adapterProdukPembayaran);
                                recycler_produk_pembayaran.setLayoutManager(new GridLayoutManager(getContext(), 4));
                                recycler_produk_pembayaran.setItemAnimator(new DefaultItemAnimator());
                                recycler_produk_pembayaran.setHasFixedSize(true);

                                shimmer_produk2.setVisibility(View.GONE);
                            }
                            else{
                                FancyToast.makeText(getContext(), response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Hasil", e.getMessage());
                            FancyToast.makeText(getContext(), "Terjad kesalahan!\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Hasil", anError.getMessage());
                        FancyToast.makeText(getContext(), "Terjad kesalahan!\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
    }

    public void settingLayout(View v, String tipe) {
        if (tipe.equals("slider")) {
            v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int availableHeight = v.getMeasuredWidth();
                    if (availableHeight > 0) {
                        v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        //save height here and do whatever you want with it
                        ViewGroup.LayoutParams params = v.getLayoutParams();

                        params.height = availableHeight / 2;
                        params.width = availableHeight;
                        v.setLayoutParams(params);
                    }
                }
            });
        }
        else if (tipe.equals("icon")){
            v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int availableHeight = v.getMeasuredWidth();
                    if (availableHeight > 0) {
                        v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        //save height here and do whatever you want with it
                        ViewGroup.LayoutParams params = v.getLayoutParams();

                        params.height = availableHeight;
                        params.width = availableHeight;
                        v.setLayoutParams(params);
                    }
                }
            });
        }
    }

    private void getSaldoAkhir(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Utils.BASE_URL + "getsaldo";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);

                        JSONObject object = jsonObject.getJSONObject("user");
                        String id = object.getString("id").trim();
                        String name = object.getString("name").trim();
                        String notelp = object.getString("notelp").trim();
                        String email = object.getString("email").trim();
                        String saldo = object.getString("saldo").trim();

                        sessionManager.createSession(id, getToken, name, notelp, email, saldo, getReffCode, object.getString("agen"), object.getString("id_agen"), MainActivity.provinsi, MainActivity.kabupaten, MainActivity.kecamatan);
                        progressDialog.dismiss();

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern("#,###,###,###");
                        String formattedString = formatter.format(Long.parseLong(String.valueOf(saldo)));

                        Utils formatRupiah = new Utils();
                        saldoTv.setText("Rp. " + formattedString);

                    } catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Jaringan Bermasalah!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Jaringan Bermasalah!", Toast.LENGTH_SHORT).show()
        ) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void getSaldo(){
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Utils.BASE_URL + "getsaldo";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);

                        JSONObject object = jsonObject.getJSONObject("user");
                        String id = object.getString("id").trim();
                        String name = object.getString("name").trim();
                        String notelp = object.getString("notelp").trim();
                        String email = object.getString("email").trim();
                        String saldo = object.getString("saldo").trim();

                        sessionManager.createSession(id, getToken, name, notelp, email, saldo, getReffCode, object.getString("agen"), object.getString("id_agen"), MainActivity.provinsi, MainActivity.kabupaten, MainActivity.kecamatan);

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern("#,###,###,###");
                        String formattedString = formatter.format(Long.parseLong(String.valueOf(saldo)));

                        Utils formatRupiah = new Utils();
                        saldoTv.setText("Rp. " + formattedString);

                        swipe_home.setRefreshing(false);

                    } catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Jaringan Bermasalah!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Jaringan Bermasalah!", Toast.LENGTH_SHORT).show()
        ) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void settingImageSlider() {
        listSlider.clear();
        viewPagerSlider.setAdapter(null);

        listSlider.add(new ListImageSlider("1", "a", R.drawable.banner1));
        listSlider.add(new ListImageSlider("2", "b", R.drawable.banner2));
        listSlider.add(new ListImageSlider("3", "c", R.drawable.banner3));

        AdapterImageSlider adapterImageSlider = new AdapterImageSlider(viewPagerSlider, listSlider, getContext());
        viewPagerSlider.setAdapter(adapterImageSlider);
        viewPagerSlider.setClipToPadding(false);
        viewPagerSlider.setClipChildren(false);
        viewPagerSlider.setOffscreenPageLimit(3);
        viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPagerSlider.setPageTransformer(new MarginPageTransformer(100));


        viewPagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(slideRunnable);
                slideHandler.postDelayed(slideRunnable, 5000);

            }
        });
    }

    private void setDataTraining() {
        dataTraining.add(new DataTraining("1", "Cara Penggunaan", R.drawable.training3));
        dataTraining.add(new DataTraining("2", "Menjadi Agen PPOB", R.drawable.training2));
//        dataTraining.add(new DataTraining("3", "Desain grafis", R.drawable.training1));
        adapterTraining = new AdapterTraining(getContext(), dataTraining);
        recycler_training.setAdapter(adapterTraining);
        recycler_training.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler_training.setItemAnimator(new DefaultItemAnimator());
        recycler_training.setHasFixedSize(true);

        adapterTraining.setOnItemClickListener(new AdapterTraining.TrainingOnClick() {
            @Override
            public void onClick(View v, int position) {
//                Intent intent = new Intent(getContext(), VideoTutorialActivity.class);
//                intent.putExtra("id", dataTraining.get(position).getId());
//                startActivity(intent);
                FancyToast.makeText(getContext(), "Video belum tersedia", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        });
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.READ_PHONE_STATE))
        {
        } else {
            ActivityCompat.requestPermissions(requireActivity(),new String[]{
                    Manifest.permission.READ_PHONE_STATE}, RequestPermissionCode);
        }
    }

    private void showDialogAlamat() {
        Dialog dialog       = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_edit_profil);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        LinearLayout linear_alamat  = dialog.findViewById(R.id.linear_alamat);
        LinearLayout linear_profil  = dialog.findViewById(R.id.linear_edt_profil);
        Spinner spinner_provinsi    = dialog.findViewById(R.id.spinner_provinsi);
        Spinner spinner_kabupaten   = dialog.findViewById(R.id.spinner_kabupaten);
        Spinner spinner_kecamatan   = dialog.findViewById(R.id.spinner_kecamatan);
        Button btn_batal            = dialog.findViewById(R.id.btn_batal);
        Button btn_simpan_alamat    = dialog.findViewById(R.id.btn_simpan_alamat);

        linear_profil.setVisibility(View.GONE);
        linear_alamat.setVisibility(View.VISIBLE);

        provinsi(spinner_provinsi);

        spinner_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                prov_item       = (Province) parent.getItemAtPosition(position);
                id_provinsi     = prov_item.getProvince_id();
                nama_provinsi   = prov_item.getProvince();

                kabupaten(id_provinsi, spinner_kabupaten);
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

                kecamatan(id_kab, spinner_kecamatan);
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

        btn_simpan_alamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gantiAlamat(dialog);
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void provinsi(Spinner spinner_provinsi) {
        dialogg.show();
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

                                AdapterSpinnerProvinsi adapterSpinnerProvinsi = new AdapterSpinnerProvinsi(getContext(), list_provinsi);
                                spinner_provinsi.setAdapter(adapterSpinnerProvinsi);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("Salah", e.getMessage());
                        }
                        dialogg.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Salah", anError.getMessage());
                        dialogg.dismiss();
                    }
                });
    }

    private void kabupaten(String id_provinsi, Spinner spinner_kabupaten) {
        dialogg.show();
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

                                AdapterSpinnerKabupaten adapterSpinnerKabupaten = new AdapterSpinnerKabupaten(getContext(), list_kabupaten);
                                spinner_kabupaten.setAdapter(adapterSpinnerKabupaten);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("Salah", e.getMessage());
                        }
                        dialogg.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Salah", anError.getMessage());
                        dialogg.dismiss();
                    }
                });
    }

    private void kecamatan(String id_kab, Spinner spinner_kecamatan) {
        dialogg.show();
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

                                AdapterSpinnerKecamatan adapterSpinnerKecamatan = new AdapterSpinnerKecamatan(getContext(), list_kecamatan);
                                spinner_kecamatan.setAdapter(adapterSpinnerKecamatan);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("Salah", e.getMessage());
                        }
                        dialogg.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Salah", anError.getMessage());
                        dialogg.dismiss();
                    }
                });
    }

    private void gantiAlamat(Dialog dialog){
        dialogg.show();
        AndroidNetworking.post(Utils.BASE_URL + "ganti_lokasi")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .addBodyParameter("provinsi", nama_provinsi)
                .addBodyParameter("kabupaten", nama_kab)
                .addBodyParameter("kecamatan", nama_kec)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            dialogg.dismiss();
                            String res_kod = response.getString("response");
                            if (res_kod.equals("200")){
                                FancyToast.makeText(getContext(), "Alamat berhasil disimpan!",
                                        LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                sessionManager.editAlamat(nama_provinsi,nama_kab, nama_kec);
                                MainActivity.provinsi   = nama_provinsi;
                                dialog.dismiss();
                            }
                            else {
                                FancyToast.makeText(getContext(), response.getString("message"),
                                        LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialogg.dismiss();
                            FancyToast.makeText(getContext(), "Alamat gagal disimpan!\n" + e.getMessage(),
                                    LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialogg.dismiss();
                        FancyToast.makeText(getContext(), "Alamat gagal disimpan!\n" + anError.getMessage(),
                                LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
    }

    private Runnable slideRunnable  = new Runnable() {
        @Override
        public void run() {
            viewPagerSlider.setCurrentItem(viewPagerSlider.getCurrentItem() + 1);
        }
    };

    private void getWa(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Utils.BASE_URL + "get_wa";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try{
                        Log.d("Hasil", response.toString());
                        JSONObject jsonObject = new JSONObject(response);
                        progressDialog.dismiss();
                        JSONObject object = jsonObject.getJSONObject("wa");
                        if (object.getString("value").trim().length() > 8){
                            String WA_URL = Utils.WA_API_URL + "+62" + object.getString("value").trim();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(WA_URL));
                            startActivity(i);
                        }
                        else {
                            FancyToast.makeText(getContext(), "Nomor tidak valid", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Jaringan Bermasalah!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Jaringan Bermasalah!", Toast.LENGTH_SHORT).show()
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

    private void cekVersi(){
        AndroidNetworking.post(Utils.BASE_URL + "get_versi")
                .addBodyParameter("versi_code", String.valueOf(BuildConfig.VERSION_CODE))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("201")){
                                Dialog dialog       = new Dialog(getContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.alertupdate);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                dialog.getWindow().setAttributes(lp);
                                dialog.show();

                                Button exit     = dialog.findViewById(R.id.exit);
                                Button update   = dialog.findViewById(R.id.update);
                                TextView title  = dialog.findViewById(R.id.title);

                                title.setText(response.getString("message"));

                                exit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        getActivity().finishAffinity();
                                    }
                                });

                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_PHONE_STATE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Granted");

            }
            else if (requestCode == RequestPermissionCode) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(requireContext(), "Permission Canceled", Toast.LENGTH_LONG).show();

                }
            }
            else {
                Toast.makeText(getContext(), "Permission Denied", LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                            != PackageManager.PERMISSION_GRANTED) {
                        showMessageOKCancel("You need to allow access permissions",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermission();
                                    }
                                });
                    }
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        settingImageSlider();
    }
}