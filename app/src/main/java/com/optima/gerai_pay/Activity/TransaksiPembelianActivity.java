package com.optima.gerai_pay.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.optima.gerai_pay.Adapter.ListViewAdapter;
import com.optima.gerai_pay.Adapter.OperatorPembelianAdapter;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Model.HargaItem;
import com.optima.gerai_pay.Model.OperatorPembelian;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.widget.Toast.LENGTH_SHORT;

public class TransaksiPembelianActivity extends AppCompatActivity {

    private ListView nominal_lview;
    private LinearLayout providerLayout, panelNohp, panelGame, linear_zona;
    private TextInputEditText etPhone, etIDPel;
    private EditText providerName, etIDuser, etZona;
    private Spinner spnOperator;
    private ImageView getContact;
    private TextView txt_title_id;

    private List<HargaItem> nominalList;

    private final ArrayList<OperatorPembelian> operatorPembelians = new ArrayList<>();
    private final ArrayList<OperatorPembelianAdapter> operatorAdapters = new ArrayList<>();

    public  static final int RequestPermissionCode  = 1 ;
    String getId;
    String getToken;
    String tipe_button  = "kontak";
    String provider_code     = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_pembelian);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
        }

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        getToken = user.get(SessionManager.TOKEN);

        EnableRuntimePermission();

        nominal_lview       = findViewById(R.id.nominal_lview);
        ImageView backBtn   = findViewById(R.id.backBtn);
        TextView titleMenu  = findViewById(R.id.menuTitle);
        etPhone = findViewById(R.id.etPhone);
        etIDPel = findViewById(R.id.etIDPel);
        LinearLayout panelToken = findViewById(R.id.panelToken);
        providerLayout      = findViewById(R.id.providerLayout);
        getContact          = findViewById(R.id.openPhonebook);
        spnOperator         = findViewById(R.id.spn_provider);
        panelNohp           = findViewById(R.id.panelNohp);
        txt_title_id        = findViewById(R.id.txt_title_id);
        panelGame           = findViewById(R.id.panelGame);
        linear_zona         = findViewById(R.id.linear_zona);
        etIDuser            = findViewById(R.id.etIDuser);
        etZona              = findViewById(R.id.etZona);

        if (spnOperator.getAdapter() == null){
            spnOperator.setEnabled(false);
        }
        else{
            spnOperator.setEnabled(true);
        }

        String getTitle = getIntent().getStringExtra("titleMenu");
        titleMenu.setText(getTitle);

        if (getTitle.equals("TOKEN LISTRIK")) {
            panelToken.setVisibility(View.VISIBLE);
            panelNohp.setVisibility(View.VISIBLE);
            txt_title_id.setText("No. Rekening / ID Pelanggan");
            etIDPel.setHint("No. Rekening / ID Pelanggan");
        }
        else if (getTitle.equals("E-TOLL")){
            panelToken.setVisibility(View.VISIBLE);
            panelNohp.setVisibility(View.GONE);
            txt_title_id.setText("Nomor Kartu");
            etIDPel.setHint("Nomor Kartu");
        }
        else {
            panelToken.setVisibility(View.GONE);
            panelNohp.setVisibility(View.VISIBLE);
        }

        backBtn.setOnClickListener(v -> finish());

        getContact.setOnClickListener(v -> {
            if (tipe_button.equals("kontak")) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
            else{
                requestProvider();
            }
        });

        nominal_lview.setNestedScrollingEnabled(true);

        nominalList = new ArrayList<>();

        etPhone.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                requestProvider();
                providerLayout.setVisibility(View.VISIBLE);
                return true;
            }
            return false;
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPhone.getText().toString().length() > 9){
                    getContact.setImageResource(R.drawable.ic_key_next);
                    tipe_button     = "lanjut";
                }
                else {
                    getContact.setImageResource(R.drawable.ic_contact_book);
                    tipe_button     = "kontak";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etIDPel.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                requestProvider();
                providerLayout.setVisibility(View.VISIBLE);
                return true;
            }
            return false;
        });

        nominal_lview.setOnItemClickListener((parent, view, position, id) -> {
            String productName = nominalList.get(position).getNamaProduk();
            String price = Integer.toString(nominalList.get(position).getHarga());
            String phone = Objects.requireNonNull(etPhone.getText()).toString().trim();
            String code = nominalList.get(position).getKodeProduk();

            if (getTitle.equals("TOKEN LISTRIK")) {
                if (etIDPel.getText().toString().isEmpty()){
                    Toast.makeText(TransaksiPembelianActivity.this,"Anda belum mengisi ID Pelanggan", Toast.LENGTH_SHORT).show();
                }
                else if (phone.matches("")){
                    Toast.makeText(TransaksiPembelianActivity.this,"Anda belum mengisi nomor handphone", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(TransaksiPembelianActivity.this, KonfirmasiTransaksiPembelian.class);
                    intent.putExtra("idPelanggan", etIDPel.getText().toString());
                    intent.putExtra("inquiry", "Token Listrik");
                    intent.putExtra("product_name", productName);
                    intent.putExtra("price", price);
                    intent.putExtra("phone", phone);
                    intent.putExtra("code", code);
                    startActivity(intent);
                    finish();
                }
            }  else if (getTitle.equals("E-TOLL")) {
                if (etIDPel.getText().toString().isEmpty()){
                    FancyToast.makeText(TransaksiPembelianActivity.this, "AAnda belum mengisi Nomor Kartu!", LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    Intent intent = new Intent(TransaksiPembelianActivity.this, KonfirmasiTransaksiPembelian.class);
                    intent.putExtra("idPelanggan", etIDPel.getText().toString());
                    intent.putExtra("inquiry", "E-TOLL");
                    intent.putExtra("product_name", productName);
                    intent.putExtra("price", price);
                    intent.putExtra("phone", etIDPel.getText().toString());
                    intent.putExtra("code", code);
                    startActivity(intent);
//                    finish();
                }
            }
            else if (provider_code.equals("148")){
                if (etIDuser.getText().toString().isEmpty()){
                    FancyToast.makeText(TransaksiPembelianActivity.this, "Anda belum mengisi User ID game!", LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else if (etZona.getText().toString().isEmpty()){
                    FancyToast.makeText(TransaksiPembelianActivity.this, "Anda belum mengisi Zone ID game!", LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    Intent intent = new Intent(TransaksiPembelianActivity.this, KonfirmasiTransaksiPembelian.class);
                    intent.putExtra("product_name", productName);
                    intent.putExtra("price", price);
                    intent.putExtra("phone", etIDuser.getText().toString() + etZona.getText().toString());
                    intent.putExtra("code", code);
                    intent.putExtra("inquiry", "GAME");
                    startActivity(intent);
//                    finish();

                    Log.d("Hasil", etIDuser.getText().toString() + etZona.getText().toString());
                }
            }
            else if (provider_code.equals("143") || provider_code.equals("142")){
                if (etIDuser.getText().toString().isEmpty()){
                    FancyToast.makeText(TransaksiPembelianActivity.this, "Anda belum mengisi User ID game!", LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    Intent intent = new Intent(TransaksiPembelianActivity.this, KonfirmasiTransaksiPembelian.class);
                    intent.putExtra("product_name", productName);
                    intent.putExtra("price", price);
                    intent.putExtra("phone", etIDuser.getText().toString());
                    intent.putExtra("code", code);
                    intent.putExtra("inquiry", "GAME");
                    startActivity(intent);
//                    finish();
                    Log.d("Hasil", etIDuser.getText().toString());
                }
            }
            else {
                if (phone.matches("")) {
                    Toast.makeText(TransaksiPembelianActivity.this, "Anda belum mengisi nomor handphone", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(TransaksiPembelianActivity.this, KonfirmasiTransaksiPembelian.class);
                    intent.putExtra("product_name", productName);
                    intent.putExtra("price", price);
                    intent.putExtra("phone", phone);
                    intent.putExtra("code", code);
                    intent.putExtra("inquiry", "");
                    startActivity(intent);
//                    finish();
                }
            }

        });
    }

    private void requestProvider() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        String getCategoryId = getIntent().getStringExtra("categoryId");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String URL_READ = Utils.BASE_URL + "operator";
        System.out.print(URL_READ);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                response -> {
                    System.out.println(response);
                    dialog.dismiss();
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("operator");
                        Gson gson = new Gson();
                        for (int k = 0; k < jsonArray.length(); k++) {
                            dialog.dismiss();
                            JSONObject object = jsonArray.getJSONObject(k);
                            OperatorPembelian data = gson.fromJson(String.valueOf(object), OperatorPembelian.class);
                            String strProviderId = object.getString("id_produk");
                            String strProviderProduk = object.getString("nama_produk").trim();

                            operatorPembelians.add(data);
                            operatorAdapters.add(new OperatorPembelianAdapter(strProviderProduk, strProviderId));

                            ArrayAdapter<OperatorPembelianAdapter> spinnerArrayAdapter = new ArrayAdapter<>(TransaksiPembelianActivity.this,  R.layout.item_spinner, operatorAdapters);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnOperator.setAdapter(spinnerArrayAdapter);
                            spnOperator.setEnabled(true);
                            dialog.dismiss();
                            spnOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    OperatorPembelianAdapter swt = (OperatorPembelianAdapter) parent.getItemAtPosition(position);
                                    String productId    = (String) swt.tag;
                                    provider_code       = (String) swt.tag;
                                    requestNominal(productId);

                                    if (productId.equals("148")){
                                        linear_zona.setVisibility(View.VISIBLE);
                                        panelGame.setVisibility(View.VISIBLE);
                                    }
                                    else if (productId.equals("143")) {
                                        linear_zona.setVisibility(View.GONE);
                                        panelGame.setVisibility(View.VISIBLE);
                                    }
                                    else if (productId.equals("142")) {
                                        linear_zona.setVisibility(View.GONE);
                                        panelGame.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        linear_zona.setVisibility(View.GONE);
                                        panelGame.setVisibility(View.GONE);
                                    }

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }

                        getContact.setImageResource(R.drawable.ic_contact_book);
                        tipe_button     = "kontak";

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(TransaksiPembelianActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                        Log.d("Salah", e.getMessage());
                    }
                },
                (Response.ErrorListener) error -> {
                    dialog.dismiss();
                    Toast.makeText(TransaksiPembelianActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                    Log.d("Salah", error.getMessage());
                })
        {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("prefix", Objects.requireNonNull(etPhone.getText()).toString());
                params.put("kategori_id", getCategoryId);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void requestNominal(final String providerId){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        String getCategoryId = getIntent().getStringExtra("categoryId");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL_READ = Utils.BASE_URL + "harga";
        System.out.print(URL_READ);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                response -> {
                    System.out.println(response);
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("harga");
                        Gson gson = new Gson();
                        nominalList.clear();
                        for (int k = 0; k < jsonArray.length(); k++) {

                            JSONObject object = jsonArray.getJSONObject(k);
                            HargaItem data = gson.fromJson(String.valueOf(object), HargaItem.class);
                            nominalList.add(data);

                        }
                        ListViewAdapter adapter = new ListViewAdapter(nominalList,R.layout.item_menu_pembelian, TransaksiPembelianActivity.this);
                        adapter.notifyDataSetChanged();
                        nominal_lview.setAdapter(adapter);
                        dialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Log.d("Salah", e.getMessage());
                        Toast.makeText(TransaksiPembelianActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    Log.d("Salah", error.getMessage());
                    Toast.makeText(TransaksiPembelianActivity.this, "Jaringan Bermasalah, Coba Lagi !", LENGTH_SHORT).show();
                })
        {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_kategori", getCategoryId);
                params.put("id_produk", providerId);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(TransaksiPembelianActivity.this,
                Manifest.permission.READ_CONTACTS))
        {
        } else {
            ActivityCompat.requestPermissions(TransaksiPembelianActivity.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, @NonNull String per[], @NonNull int[] PResult) {

        super.onRequestPermissionsResult(RC, per, PResult);
        if (RC == RequestPermissionCode) {
            if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                Toast.makeText(TransaksiPembelianActivity.this, "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

            }
        }
    }

    @SuppressLint("Recycle")
    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);

        switch (RequestCode) {

            case (1):
                if (ResultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;

                    uri = ResultIntent.getData();

                    assert uri != null;
                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    assert cursor1 != null;
                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.parseInt(IDresult) ;

                        if (IDresultHolder == 1) {

                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            assert cursor2 != null;
                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String final_number = TempNumberHolder.replace("-", "");
                                etPhone.setText(final_number.replace(" ", ""));

                            }
                        }

                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}