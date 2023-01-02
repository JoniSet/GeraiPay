package com.optima.gerai_pay.MainFragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.optima.gerai_pay.Activity.ChatActivity;
import com.optima.gerai_pay.Activity.KomisiActivity;
import com.optima.gerai_pay.Activity.MySaldoActivity;
import com.optima.gerai_pay.Activity.ValidationActivity;
import com.optima.gerai_pay.Helper.Loading;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ProfilFragment extends Fragment {

    private SessionManager sessionManager;
    String getToken;
    private TextView txt_edit, userName, email, phone;
    String data_edit, tipe_edit = "0";
    Loading loading     = new Loading();
    Dialog dialog;

    Button cust_services;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        sessionManager = new SessionManager(requireContext());
        sessionManager.checkLogin();

        dialog = loading.dialog_helper(getContext());

        HashMap<String, String> user = sessionManager.getUserDetail();
        String getName = user.get(SessionManager.NAME);
        String getEmail = user.get(SessionManager.EMAIL);
        String getTelp = user.get(SessionManager.NOTELP);
        String getReffCode = user.get(SessionManager.REFFCODE);
        getToken = user.get(SessionManager.TOKEN);

        userName    = view.findViewById(R.id.userName);
        email       = view.findViewById(R.id.email);
        phone       = view.findViewById(R.id.phone);
        TextView reffcode = view.findViewById(R.id.reffCode);
        Button mySaldo = view.findViewById(R.id.mySaldo);
        Button myKomisi = view.findViewById(R.id.myKomisi);
        Button syarat = view.findViewById(R.id.syarat);
        Button privacyPolicy = view.findViewById(R.id.privacyPolicy);
        Button logout = view.findViewById(R.id.logout);
        ImageView chatButton = view.findViewById(R.id.chatButton);
        txt_edit = view.findViewById(R.id.txt_edit);
        cust_services = view.findViewById(R.id.cust_services);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), ChatActivity.class));
                getWa();
            }
        });

        userName.setText(getName);
        email.setText(getEmail);
        phone.setText(getTelp);

        assert getReffCode != null;
        if (getReffCode.equals("")) {
            reffcode.setVisibility(View.GONE);
        } else if (getReffCode.equals("null")) {
            reffcode.setVisibility(View.GONE);
        } else {
            reffcode.setText(getReffCode);
        }

        mySaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MySaldoActivity.class);
                startActivity(intent);
            }
        });

        myKomisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), KomisiActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEdit();
            }
        });

        cust_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWa();
            }
        });

        return view;
    }

    private void dialogEdit(){
        Dialog dialog       = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_profil);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        EditText edt_email  = dialog.findViewById(R.id.edt_email);
        EditText edt_notelp = dialog.findViewById(R.id.edt_notelp);
        EditText edt_password = dialog.findViewById(R.id.edt_password);
        Button btn_simpan   = dialog.findViewById(R.id.btn_simpan);
        LinearLayout linear_notelp      = dialog.findViewById(R.id.linear_notelp);
        LinearLayout linear_email       = dialog.findViewById(R.id.linear_email);
        LinearLayout linear_password    = dialog.findViewById(R.id.linear_password);

//        edt_email.setText(email.getText().toString());
//        edt_notelp.setText(phone.getText().toString());

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipe_edit.equals("0")){
                    FancyToast.makeText(getContext(), "Tidak ada perubahan info profil",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();

                    dialog.dismiss();
                }
                else if (tipe_edit.equals("email")){
                    dialog.dismiss();
                    gantiEmail();
                }
                else if (tipe_edit.equals("notelp")){
                    dialog.dismiss();
                    Intent intent   = new Intent(getContext(), ValidationActivity.class);
                    intent.putExtra("noTelp", phone.getText().toString());
                    intent.putExtra("noTelpBaru", data_edit);
                    intent.putExtra("kode_verifikasi", "2");
                    startActivity(intent);
                }
                else if (tipe_edit.equals("password")){
                    dialog.dismiss();
                    Intent intent   = new Intent(getContext(), ValidationActivity.class);
                    intent.putExtra("noTelp", phone.getText().toString());
                    intent.putExtra("password", data_edit);
                    intent.putExtra("kode_verifikasi", "3");
                    startActivity(intent);
                }
            }
        });

        edt_notelp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                total.setText(edt_harga_jual.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_notelp.removeTextChangedListener(this);
                edt_notelp.addTextChangedListener(this);
                if (edt_notelp.getText().length() > 0){
                    linear_email.setVisibility(View.GONE);
                    linear_password.setVisibility(View.GONE);
                    linear_notelp.setVisibility(View.VISIBLE);

                    tipe_edit = "notelp";
                }
                else{
                    linear_email.setVisibility(View.VISIBLE);
                    linear_notelp.setVisibility(View.VISIBLE);
                    linear_password.setVisibility(View.VISIBLE);

                    tipe_edit = "0";
                }
                data_edit   = edt_notelp.getText().toString();
            }
        });

        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                total.setText(edt_harga_jual.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_email.removeTextChangedListener(this);
                edt_email.addTextChangedListener(this);
                if (edt_email.getText().length() > 0){
                    linear_email.setVisibility(View.VISIBLE);
                    linear_notelp.setVisibility(View.GONE);
                    linear_password.setVisibility(View.GONE);

                    tipe_edit = "email";
                }
                else{
                    linear_email.setVisibility(View.VISIBLE);
                    linear_notelp.setVisibility(View.VISIBLE);
                    linear_password.setVisibility(View.VISIBLE);

                    tipe_edit = "0";
                }
                data_edit   = edt_email.getText().toString();
            }
        });

        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                total.setText(edt_harga_jual.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_password.removeTextChangedListener(this);
                edt_password.addTextChangedListener(this);
                if (edt_password.getText().length() > 0){
                    linear_email.setVisibility(View.GONE);
                    linear_notelp.setVisibility(View.GONE);
                    linear_password.setVisibility(View.VISIBLE);

                    tipe_edit = "password";
                }
                else{
                    linear_email.setVisibility(View.VISIBLE);
                    linear_notelp.setVisibility(View.VISIBLE);
                    linear_password.setVisibility(View.VISIBLE);

                    tipe_edit = "0";
                }
                data_edit   = edt_password.getText().toString();
            }
        });
    }

    private void Logout(){
        final ProgressDialog dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Please Wait");
        dialog.setCancelable(false);
        dialog.show();

        String URL_LOGOUT = Utils.BASE_URL + "logout";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_LOGOUT,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");

                        if (responseCode.equals("200")){
                            dialog.dismiss();
                            sessionManager.logout();

                        } else {
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                            dialog.dismiss();
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark);
                        dialog.dismiss();
                    }
                },
                error -> {
                    toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark);
                    dialog.dismiss();
                })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
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

    private void gantiEmail(){
        dialog.show();
        AndroidNetworking.post(Utils.BASE_URL + "ganti_email")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .addBodyParameter("email", data_edit)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            dialog.dismiss();
                            String res_kode     = response.getString("response");
                            if (res_kode.equals("200")){
                                FancyToast.makeText(getContext(), "Berhasil merubah email",
                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                email.setText(data_edit);
                                sessionManager.editEmail(data_edit);
                            }
                            else{
                                FancyToast.makeText(getContext(), response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                            data_edit = "";
                            tipe_edit = "0";
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            FancyToast.makeText(getContext(), "Gagal merubah info profil\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            data_edit = "";
                            tipe_edit = "0";
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        FancyToast.makeText(getContext(), "Gagal merubah info profil\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        data_edit = "";
                        tipe_edit = "0";
                    }
                });
    }

    private void toastIcon(final String message, final int image, final int color) {
        Toast toast = new Toast(requireContext());
        toast.setDuration(Toast.LENGTH_LONG);

        @SuppressLint("InflateParams") View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(image);
        ((ImageView) custom_view.findViewById(R.id.icon)).setColorFilter(android.R.color.white);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(color));

        toast.setView(custom_view);
        toast.show();
    }

    private final
    Context applicationContext = MainActivity.getContextOfApplication();
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), filePath);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
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

    @Override
    public void onResume() {
        super.onResume();
    }
}