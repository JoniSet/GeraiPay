package com.optima.gerai_pay.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.optima.gerai_pay.Activity.LoginActivity;
import com.optima.gerai_pay.MainActivity;

import java.util.HashMap;

public class SessionManager {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    public Context context;

    private static final String PREF_NAME   = "LOGIN";
    private static final String LOGIN       = "IS_LOGIN";
    public static final String ID           = "ID";
    public static final String NAME         = "NAME";
    public static final String NOTELP       = "NOTELP";
    public static final String EMAIL        = "EMAIL";
    public static final String SALDO        = "SALDO";
    public static final String TOKEN        = "TOKEN";
    public static final String REFFCODE     = "REFFCODE";
    public static final String AGEN         = "AGEN";
    public static final String ID_AGEN      = "ID_AGEN";
    public static final String PROVINSI     = "PROVINSI";
    public static final String KABUPATEN    = "KABUPATEN";
    public static final String KECAMATAN    = "KECAMATAN";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        int PRIVATE_MODE = 0;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String id, String token, String name, String notelp, String email, String saldo, String reffcode, String agen, String id_agen, String provinsi, String kabupaten, String kecamatan){
        editor.putBoolean(LOGIN, true);
        editor.putString(ID, id);
        editor.putString(TOKEN, token);
        editor.putString(NAME, name);
        editor.putString(NOTELP, notelp);
        editor.putString(EMAIL, email);
        editor.putString(SALDO, saldo);
        editor.putString(REFFCODE, reffcode);
        editor.putString(AGEN, agen);
        editor.putString(ID_AGEN, id_agen);
        editor.putString(PROVINSI, provinsi);
        editor.putString(KABUPATEN, kabupaten);
        editor.putString(KECAMATAN, kecamatan);
        editor.apply();
    }

    public void editAlamat(String provinsi, String kabupaten, String kecamatan){
        editor.putString(PROVINSI, provinsi);
        editor.putString(KABUPATEN, kabupaten);
        editor.putString(KECAMATAN, kecamatan);
        editor.apply();
    }

    public void editEmail(String email){
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public void editNotelp(String notelp){
        editor.putString(NOTELP, notelp);
        editor.apply();
    }

    private boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if (!this.isLoggin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(TOKEN, sharedPreferences.getString(TOKEN, null));
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(NOTELP, sharedPreferences.getString(NOTELP, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(SALDO, sharedPreferences.getString(SALDO, null));
        user.put(REFFCODE, sharedPreferences.getString(REFFCODE, null));
        user.put(AGEN, sharedPreferences.getString(AGEN, null));
        user.put(ID_AGEN, sharedPreferences.getString(ID_AGEN, null));
        user.put(PROVINSI, sharedPreferences.getString(PROVINSI, null));
        user.put(KABUPATEN, sharedPreferences.getString(KABUPATEN, null));
        user.put(KECAMATAN, sharedPreferences.getString(KECAMATAN, null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((MainActivity) context).finish();
    }

}

