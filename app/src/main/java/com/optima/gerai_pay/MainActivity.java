package com.optima.gerai_pay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.optima.gerai_pay.Adapter.AdapterSpinnerKabupaten;
import com.optima.gerai_pay.Adapter.AdapterSpinnerKecamatan;
import com.optima.gerai_pay.Adapter.AdapterSpinnerProvinsi;
import com.optima.gerai_pay.Helper.Loading;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainFragments.DownlineFragment;
import com.optima.gerai_pay.MainFragments.HistoryFragment;
import com.optima.gerai_pay.MainFragments.HomeFragment;
import com.optima.gerai_pay.MainFragments.NotifikasiFragment;
import com.optima.gerai_pay.MainFragments.ProfilFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.optima.gerai_pay.Model.Kabupaten;
import com.optima.gerai_pay.Model.Kecamatan;
import com.optima.gerai_pay.Model.Province;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;
    public static String getId, nama, token, ref = "", provinsi, kabupaten, kecamatan;

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    private Fragment fragment = null;
    String getTelp;
    String getEmail;
    String getSaldo;
    String getAgen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
        }

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId   = user.get(SessionManager.ID);

        nama    = user.get(SessionManager.NAME);
        token   = user.get(SessionManager.TOKEN);
        ref     = user.get(SessionManager.REFFCODE);
        provinsi    = user.get(SessionManager.PROVINSI);
        kabupaten   = user.get(SessionManager.KABUPATEN);
        kecamatan   = user.get(SessionManager.KECAMATAN);
        getTelp = user.get(SessionManager.NOTELP);
        getEmail = user.get(SessionManager.EMAIL);
        getSaldo = user.get(SessionManager.SALDO);
        getAgen = user.get(SessionManager.AGEN);

        Log.d("Token", token + "\n" + provinsi);

        ChipNavigationBar chipNavigationBar = findViewById(R.id.chipNavigation);

        chipNavigationBar.setItemSelected(R.id.home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();


        if (ref == null)
        {
            ref                   = "kosong";
        }
//        else {
//            if (provinsi == null){
//                showDialogAlamat();
//            }
//        }

        if (ref.equals("null")){
            chipNavigationBar.removeViewAt(2);
        }

        Log.d("REF", ref); 

        Bundle bundle   = getIntent().getExtras();
        if (bundle != null){
            Log.d("Hasil", bundle.getString("tipe"));
            if(bundle.getString("tipe").equals("3")){
                dialogPopupIklan();
            }
        }

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.history:
                        fragment = new HistoryFragment();
                        break;
                    case R.id.agen:
                        fragment = new DownlineFragment();
                        break;
                    case R.id.notifikasi:
                        fragment = new NotifikasiFragment();
                        break;
                    case R.id.user:
                        fragment = new ProfilFragment();
                        break;

                }
                if (fragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
            }
        });

    }

    @Override
    public void onBackPressed(){
        dialogKeluar();
    }

    private void dialogKeluar() {
        final Dialog dialog     = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_keluar);
        dialog.setCancelable(true);

        Button btn_keluar   = dialog.findViewById(R.id.btn_keluar);
        Button btn_batal    = dialog.findViewById(R.id.btn_batal);

        btn_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                finishAffinity();
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnitmation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void dialogPopupIklan() {
        Dialog dialog       = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        ImageView img_close     = dialog.findViewById(R.id.img_close);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}