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
import com.optima.gerai_pay.Activity.ChatActivity;
import com.optima.gerai_pay.Adapter.AdapterNotif;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.SqliteHelper;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Model.ListNotif;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotifikasiFragment extends Fragment {

    private RecyclerView notif_rview;
    private AdapterNotif adapter;
    private ArrayList<ListNotif> notifikasiArrayList;
    private LinearLayout linear_kosong;

    SessionManager sessionManager;
    String getToken;

    SqliteHelper sqliteHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifikasi, container, false);

        sessionManager  = new SessionManager(requireContext());
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getToken        = user.get(SessionManager.TOKEN);

        sqliteHelper    = new SqliteHelper(getContext());

        linear_kosong   = view.findViewById(R.id.linear_kosong);
        notif_rview     = view.findViewById(R.id.notif_rview);
        ImageView chatButton = view.findViewById(R.id.chatButton);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), ChatActivity.class));
                getWa();
            }
        });


        notifikasiArrayList     = sqliteHelper.readKategoriSqlite();

        adapter                 = new AdapterNotif (getContext(), notifikasiArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        notif_rview.setLayoutManager(layoutManager);
        notif_rview.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterNotif.bacaNotifOnClick() {
            @Override
            public void onClick(View v, int position) {

            }
        });

        if (notifikasiArrayList.size() == 0){
            linear_kosong.setVisibility(View.VISIBLE);
            notif_rview.setVisibility(View.GONE);
        }
        else {
            linear_kosong.setVisibility(View.GONE);
            notif_rview.setVisibility(View.VISIBLE);
        }

        return view;
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
}