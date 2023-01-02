package com.optima.gerai_pay.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.optima.gerai_pay.Adapter.AdapterChat;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Tanggal;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Model.ListChat;
import com.optima.gerai_pay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recycler_chat;
    private ArrayList<ListChat> listChat;
    private ListChat dataChat;
    private AdapterChat adapterChat;
    private EditText edt_message;
    private LinearLayout L_kirim;

    String token, pesan;
    private Tanggal tanggal         = new Tanggal();
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SessionManager sessionManager   = new SessionManager(ChatActivity.this);
        HashMap<String, String> user    = sessionManager.getUserDetail();
        token                   = user.get(SessionManager.TOKEN);

        edt_message             = findViewById(R.id.edt_message);
        L_kirim                 = findViewById(R.id.L_kirim);

        L_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesan           = edt_message.getText().toString();
                if (pesan.equals(""))
                {
                    Toast.makeText(ChatActivity.this, "Mohon tulis sesuatu!!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    edt_message.setText("");
                    send_message();
                }
            }
        });

        recycler_chat           = findViewById(R.id.recycler_chat);
        recycler_chat.setLayoutManager(new LinearLayoutManager(this));
        recycler_chat.setItemAnimator(new DefaultItemAnimator());
        recycler_chat.setHasFixedSize(true);

        get_chat();
    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            get_chat();
            mHandler.postDelayed(m_Runnable, 3000);
        }

    };

    private void send_message() {
        ProgressDialog progressDialog       = new ProgressDialog(ChatActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        InputMethodManager inputManager     = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        RequestQueue queue          = Volley.newRequestQueue(ChatActivity.this);
        String url                  = Utils.BASE_URL + "send_pesan";
        StringRequest request       = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject       = new JSONObject(response);
                        String res_kode             = jsonObject.getString("response");
                        if (res_kode.equals("200")){
                            adapterChat.notifyDataSetChanged();
                            get_chat();
                            progressDialog.dismiss();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(ChatActivity.this, "Gagal mengirim pesan!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(ChatActivity.this, "Jaringan bermasalah!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(ChatActivity.this, "Jaringan bermasalah!", Toast.LENGTH_SHORT).show();
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers         = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params          = new HashMap<>();
                params.put("pesan", pesan);
                return params;
            }
        };
        queue.add(request);
    }

    public void get_chat(){
        RequestQueue queue          = Volley.newRequestQueue(ChatActivity.this);
        String url                  = Utils.BASE_URL + "get_pesan";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject       = new JSONObject(response);
                        String res_kode             = jsonObject.getString("response");
                        if (res_kode.equals("200"))
                        {
                            listChat                = new ArrayList<>();
                            JSONArray jsonArray     = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject object   = jsonArray.getJSONObject(i);
                                dataChat            = new ListChat(
                                        object.getString("id"),
                                        object.getString("tujuan_user"),
                                        object.getString("dari_id"),
                                        object.getString("pesan"),
                                        object.getString("tgl"),
                                        object.getString("baca")
                                );
                                listChat.add(dataChat);
                            }
                            adapterChat             = new AdapterChat(ChatActivity.this, listChat);
                            adapterChat.notifyDataSetChanged();
                            recycler_chat.setAdapter(adapterChat);
                            recycler_chat.scrollToPosition(adapterChat.getItemCount()-1);
                        }
                        else {
                            Toast.makeText(ChatActivity.this, "Belum ada percakapan!!!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChatActivity.this, "Jaringan Bermasalah!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(ChatActivity.this, "Jaringan Bermasalah!", Toast.LENGTH_SHORT).show()
        ){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers         = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(stringRequest);
    }
}