package com.optima.gerai_pay.MainFragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.optima.gerai_pay.Activity.ChatActivity;
import com.optima.gerai_pay.Adapter.HistoryTransaksiAdapter;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.Model.HistoryModel;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private RecyclerView history_rview;
    private LinearLayout paneldata;
    SwipeRefreshLayout swLayout;
    private HistoryTransaksiAdapter adapter;
    private ArrayList<HistoryModel> historyTransaksiArrayList;
    SessionManager sessionManager;
    String getToken;
    String getReffCode;
    String getAgen;
    String getIdAgen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        sessionManager = new SessionManager(requireContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getToken = user.get(SessionManager.TOKEN);
        getReffCode = user.get(SessionManager.REFFCODE);
        getAgen = user.get(SessionManager.AGEN);
        getIdAgen = user.get(SessionManager.ID_AGEN);

        paneldata = view.findViewById(R.id.panelError);
        swLayout = view.findViewById(R.id.swlayout);
        ImageView chatButton = view.findViewById(R.id.chatButton);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), ChatActivity.class));
                getWa();
            }
        });

        historyTransaksiArrayList = new ArrayList<>();
        history_rview = view.findViewById(R.id.history_rview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        history_rview.setLayoutManager(layoutManager);
        history_rview.setHasFixedSize(true);

        swLayout.setColorSchemeResources(R.color.red,R.color.blue);

        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swLayout.setRefreshing(false);
                getHistory();
            }
        });

        getHistory();

        return view;
    }

    private void getHistory(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Utils.BASE_URL + "histori";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        Gson gson = new Gson();
                        historyTransaksiArrayList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("history");
                        if (jsonArray.length()==0) {
                            history_rview.setVisibility(View.GONE);
                            paneldata.setVisibility(View.VISIBLE);
                            swLayout.setVisibility(View.GONE);
                        } else {
                            for (int k = 0; k < jsonArray.length(); k++) {
                                history_rview.setVisibility(View.VISIBLE);
                                paneldata.setVisibility(View.GONE);
                                swLayout.setVisibility(View.VISIBLE);
                                JSONObject object = jsonArray.getJSONObject(k);
                                HistoryModel data = gson.fromJson(String.valueOf(object), HistoryModel.class);
                                historyTransaksiArrayList.add(data);

                            }
                        }

                        JSONObject object = jsonObject.getJSONObject("user");
                        String id = object.getString("id").trim();
                        String name = object.getString("name").trim();
                        String notelp = object.getString("notelp").trim();
                        String email = object.getString("email").trim();
                        String saldo = object.getString("saldo").trim();

                        sessionManager.createSession(id, getToken, name, notelp, email, saldo, getReffCode, getAgen, getIdAgen, MainActivity.provinsi, MainActivity.kabupaten, MainActivity.kecamatan);

                        adapter = new HistoryTransaksiAdapter(historyTransaksiArrayList, getContext(), getActivity());
                        history_rview.setAdapter(adapter);
                        Collections.reverse(historyTransaksiArrayList);
                        progressDialog.dismiss();


                    } catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Jaringan Bermasalah, Coba Lagi !", Toast.LENGTH_SHORT).show();
                        paneldata.setVisibility(View.VISIBLE);
                        swLayout.setVisibility(View.GONE);
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Jaringan Bermasalah, Coba Lagi !", Toast.LENGTH_SHORT).show();
                    paneldata.setVisibility(View.VISIBLE);
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

    private void getWa(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Utils.BASE_URL + "get_wa";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try{
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

    private void showChatOption() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_chat_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        final CardView whatsAppChat = dialog.findViewById(R.id.whatsAppChat);
        final CardView liveChat = dialog.findViewById(R.id.liveChat);

        whatsAppChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWa();
            }
        });

        liveChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);*/

                Toast.makeText(requireContext(), "Under Construction", Toast.LENGTH_LONG).show();
            }
        });
    }
}