package com.optima.gerai_pay.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.optima.gerai_pay.Helper.DeviceInformation;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private Button Login, signUp;
    private ProgressBar loading;
    SessionManager sessionManager;

    TextView txt_lupa_pass;
    String token, ip_public, device_type, os_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);

        loading = findViewById(R.id.loading);
        etUsername = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Login = findViewById(R.id.logIn);
        signUp = findViewById(R.id.signUp);
        txt_lupa_pass = findViewById(R.id.txt_lupa_pass);

        Login.setOnClickListener(view -> {
            String mEmail = etUsername.getText().toString().trim();
            String mPass = etPassword.getText().toString().trim();

            if (!mEmail.isEmpty() && !mPass.isEmpty()){
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }
                                token = task.getResult();
                                Login(mEmail, mPass);
                            }
                        });

            } else {
                etUsername.setError("Masukan email terlebih dahulu");
                etPassword.setError("Masukan password terlebih dahulu");
            }
        });

        signUp.setOnClickListener(view -> {
            Intent intent =new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        txt_lupa_pass.setOnClickListener(view -> {
            Intent intent =new Intent(LoginActivity.this, LupaPasswordActivity.class);
            startActivity(intent);
            finish();
        });

        getIP();
    }

    private void getIP() {
        DeviceInformation deviceInformation = new DeviceInformation();
        device_type = deviceInformation.getType(LoginActivity.this);
        os_version  = deviceInformation.getVer(LoginActivity.this);

        loading.setVisibility(View.VISIBLE);
        Login.setVisibility(View.GONE);

        AndroidNetworking.get("https://api.ipify.org/")
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()) {
                            ip_public = response;
                            loading.setVisibility(View.GONE);
                            Login.setVisibility(View.VISIBLE);

                            Log.d("Cek", ip_public + "\n" + device_type + "\n" +os_version);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(LoginActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        Login.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void Login(final String email, final String password){
        loading.setVisibility(View.VISIBLE);
        Login.setVisibility(View.GONE);

        Log.d("Salah", token);

        String URL_LOGIN = Utils.BASE_URL +  "login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");

                        if (responseCode.equals("200")){
                            String token        = jsonObject.getString("token");
                            JSONObject object   = jsonObject.getJSONObject("user");
                            String id           = object.getString("id").trim();
                            String name         = object.getString("name").trim();
                            String notelp       = object.getString("notelp").trim();
                            String email1       = object.getString("email").trim();
                            String saldo        = object.getString("saldo").trim();
                            String reffcode     = object.getString("reff_code").trim();
                            String agen         = object.getString("agen").trim();
                            String id_agen      = object.getString("id_agen").trim();
                            String prov         = object.getString("provinsi").trim();
                            String kab          = object.getString("kabupaten").trim();
                            String kec          = object.getString("kecamatan").trim();

                            sessionManager.createSession(id, token, name, notelp, email1, saldo, reffcode, agen, id_agen, prov, kab, kec);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            loading.setVisibility(View.GONE);
                            Login.setVisibility(View.VISIBLE);
                            finish();

                        } else if (responseCode.equals("201")) {
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                            loading.setVisibility(View.GONE);
                            Login.setVisibility(View.VISIBLE);

                        } else if (responseCode.equals("202")) {
                            SendOTP(jsonObject.getString("notelp"));
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);

                        } else if (responseCode.equals("203")) {
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                            loading.setVisibility(View.GONE);
                            Login.setVisibility(View.VISIBLE);

                        }
                        else {
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                            loading.setVisibility(View.GONE);
                            Login.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        loading.setVisibility(View.GONE);
                        Login.setVisibility(View.VISIBLE);
                        toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark);
                    }
                },
                error -> {
                    loading.setVisibility(View.GONE);
                    Login.setVisibility(View.VISIBLE);
                    toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark);
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                params.put("device_token", token);
                params.put("ip_public", ip_public);
                params.put("device_model", device_type);
                params.put("versi_os", os_version);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SendOTP(final String notelp){
        String URL_LOGIN = Utils.BASE_URL + "otp";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");

                        if (responseCode.equals("200") && message.equals("Send OTP Sukses")){
                            Intent intent = new Intent(LoginActivity.this, ValidationActivity.class);
                            intent.putExtra("otpID", jsonObject.getString("otp_id"));
                            intent.putExtra("noTelp", jsonObject.getString("notelp"));
                            intent.putExtra("kode_verifikasi", "1");
                            startActivity(intent);
                            finish();

                        } else {
                            toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        loading.setVisibility(View.GONE);
                        Login.setVisibility(View.VISIBLE);
                        toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark);
                    }
                },
                error -> {
                    loading.setVisibility(View.GONE);
                    Login.setVisibility(View.VISIBLE);
                    toastIcon("Jaringan Bermasalah, Coba Lagi !", R.drawable.ic_close, android.R.color.holo_red_dark);
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("notelp", notelp);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

    boolean doubleBackPressed=false;
    @Override
    public void onBackPressed(){
        if (doubleBackPressed){
            finish();
            System.exit(0);
        } else {
            doubleBackPressed=true;
            final ConstraintLayout linearLayout = findViewById(R.id.LinearLayout);
            Snackbar.make(linearLayout, getString(R.string.pressbackagain),Snackbar.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(() -> doubleBackPressed=false,2000);
        }
    }
}