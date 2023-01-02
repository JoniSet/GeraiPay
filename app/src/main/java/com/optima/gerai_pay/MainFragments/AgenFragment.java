package com.optima.gerai_pay.MainFragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.optima.gerai_pay.Activity.ActivasiAgenActivity;
import com.optima.gerai_pay.Adapter.RiwayatPengajuanAdapter;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Model.HistoryPerumahanItem;
import com.optima.gerai_pay.Model.PengajuanItem;
import com.optima.gerai_pay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class AgenFragment extends Fragment {

    private TextView userName;
    private TextView phoneNumber;
    private TextView email;
    private TextView jumlahPelanggan;
    private TextView jumlahPenjualan;
    private LinearLayout notFoundLayout;

    private RecyclerView riwayatRview;
    private RiwayatPengajuanAdapter riwayatPengajuanAdapter;
    private final ArrayList<PengajuanItem> pengajuanItem = new ArrayList<PengajuanItem>();

    SessionManager sessionManager;
    String getAgen;
    String getIdAgen;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agen, container, false);
        sessionManager = new SessionManager(requireContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getAgen = user.get(SessionManager.AGEN);
        getIdAgen = user.get(SessionManager.ID_AGEN);

        LinearLayout panelYes = view.findViewById(R.id.agenYes);
        LinearLayout panelNo = view.findViewById(R.id.agenNo);
        Button btnUpgrade = view.findViewById(R.id.btnUpgrade);
        Button btnOpenApps = view.findViewById(R.id.openApps);
        ImageView chatButton = view.findViewById(R.id.chatButton);
        riwayatRview = view.findViewById(R.id.riwayatRview);
        userName = view.findViewById(R.id.userName);
        phoneNumber = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        jumlahPelanggan = view.findViewById(R.id.jumlahPelanggan);
        jumlahPenjualan = view.findViewById(R.id.jumlahPenjualan);
        notFoundLayout = view.findViewById(R.id.notFoundLayout);
        TextView menuTitle = view.findViewById(R.id.menuTitle);

        if (getAgen.equals("0")) {
            panelNo.setVisibility(View.VISIBLE);
            panelYes.setVisibility(View.GONE);
            chatButton.setVisibility(View.GONE);
            btnOpenApps.setVisibility(View.GONE);
        } else if (getAgen.equals("1")) {
            panelNo.setVisibility(View.GONE);
            panelYes.setVisibility(View.VISIBLE);
            menuTitle.setText("Overview Agen");
            chatButton.setVisibility(View.GONE);
            btnOpenApps.setVisibility(View.VISIBLE);

            getUserDetail();
            getRiwayat();
        }

        btnUpgrade.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), ActivasiAgenActivity.class);
            startActivity(intent);
        });

        btnOpenApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = requireActivity().getPackageManager().getLaunchIntentForPackage("com.imdvlpr.smartagent");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="+"com.imdvlpr.smartagent"));
                    startActivity(marketIntent);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        riwayatRview.setLayoutManager(layoutManager);
        riwayatRview.setHasFixedSize(false);

        return view;
    }

    private void getUserDetail(){

        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        String URL_READ = Utils.AGENT_URL + "getagent";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                response -> {
                    dialog.dismiss();
                    Log.i(TAG, response);
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        if (message.equals("success")){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                userName.setText(object.getString("nama").trim());
                                phoneNumber.setText(object.getString("mobile"));
                                email.setText(object.getString("email"));

                                getJumlahPelanggan();
                                getJumlahPenjualan();

                            }
                        } else {
                            dialog.dismiss();
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(requireContext(), "Jaringan Bermasalah, Coba Lagi !", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(requireContext(), "Jaringan Bermasalah, Coba Lagi !", Toast.LENGTH_SHORT).show();
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", getIdAgen);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    private void getRiwayat() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please Wait");
        dialog.show();

        String URL_ALL = Utils.AGENT_URL + "getpengajuan";
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest getRequest = new StringRequest(Request.Method.POST, URL_ALL,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Gson gson = new Gson();
                        pengajuanItem.clear();

                        if (jsonArray.length()==0) {
                            notFoundLayout.setVisibility(View.VISIBLE);
                            riwayatRview.setVisibility(View.GONE);
                            dialog.dismiss();
                        } else {
                            for (int k = 0; k < jsonArray.length(); k++) {
                                JSONObject object = jsonArray.getJSONObject(k);
                                PengajuanItem data = gson.fromJson(String.valueOf(object), PengajuanItem.class);
                                pengajuanItem.add(data);

                            }
                            riwayatPengajuanAdapter = new RiwayatPengajuanAdapter(pengajuanItem, getContext());
                            riwayatRview.setAdapter(riwayatPengajuanAdapter);
                            riwayatPengajuanAdapter.notifyDataSetChanged();
                            riwayatRview.setVisibility(View.VISIBLE);
                            notFoundLayout.setVisibility(View.GONE);
                            Collections.reverse(pengajuanItem);
                            dialog.dismiss();
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        dialog.dismiss();
                    }
                },
                error -> {
                    notFoundLayout.setVisibility(View.VISIBLE);
                    riwayatRview.setVisibility(View.GONE);
                    dialog.dismiss();
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_agent", getIdAgen);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };
        queue.add(getRequest);
    }

    private void getJumlahPelanggan(){
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please Wait");
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Utils.AGENT_URL + "getcustomer";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int k = 0; k < jsonArray.length(); k++) {
                            int count = jsonArray.length();
                            jumlahPelanggan.setText(String.valueOf(count));
                        }
                        dialog.dismiss();

                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    dialog.dismiss();
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_agent", getIdAgen);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void getJumlahPenjualan(){
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Utils.AGENT_URL + "getpengajuan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        Gson gson = new Gson();
                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject object = jsonArray.getJSONObject(k);
                            HistoryPerumahanItem data = gson.fromJson(String.valueOf(object), HistoryPerumahanItem.class);
                            int count = jsonArray.length();
                            jumlahPenjualan.setText(String.valueOf(count));
                        }

                        dialog.dismiss();


                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_agent", getIdAgen);

                return params;
            }
        };
        queue.add(postRequest);
    }
}