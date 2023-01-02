package com.optima.gerai_pay.MainFragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.optima.gerai_pay.Activity.DetailDownlineActivity;
import com.optima.gerai_pay.Adapter.AdapterDownline;
import com.optima.gerai_pay.Adapter.AdapterSpinnerKabupaten;
import com.optima.gerai_pay.Adapter.AdapterSpinnerKecamatan;
import com.optima.gerai_pay.Adapter.AdapterSpinnerProvinsi;
import com.optima.gerai_pay.Helper.DropwdownListHelper;
import com.optima.gerai_pay.Helper.Loading;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.Model.Kabupaten;
import com.optima.gerai_pay.Model.Kecamatan;
import com.optima.gerai_pay.Model.ListDownline;
import com.optima.gerai_pay.Model.Province;
import com.optima.gerai_pay.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DownlineFragment extends Fragment {
    private ImageView backBtn;
    private TextView txt_title, txt_tambah_downline;
    private EditText edt_nama, edt_nohp, edt_email;
    private Spinner spinner_tenor, spinner_provinsi, spinner_kabupaten, spinner_kecamatan;
    private Button btn_daftar;
    private LinearLayout linear_add_downline, linear_daftar_downline, linear_kosong;
    DropwdownListHelper dropwdownListHelper;
    private SwipeRefreshLayout swipe_downline;

    private ArrayList<ListDownline> listDownline  = new ArrayList<>();
    private AdapterDownline adapterDownline;
    private RecyclerView list_downline;

    private ArrayList<Province> list_provinsi   = new ArrayList<>();
    private ArrayList<Kabupaten> list_kabupaten = new ArrayList<>();
    private ArrayList<Kecamatan> list_kecamatan = new ArrayList<>();

    Province prov_item;
    Kabupaten kab_item;
    Kecamatan kec_item;

    String status   = "list", markup;
    String id_provinsi, id_kab, id_kec;

    Loading loading     = new Loading();
    Dialog dialog;

    public DownlineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downline, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        dialog                  = loading.dialog_helper(getContext());
        backBtn                 = view.findViewById(R.id.backBtn);

        txt_title               = view.findViewById(R.id.txt_title);
        txt_tambah_downline     = view.findViewById(R.id.txt_tambah_downline);

        edt_nama                = view.findViewById(R.id.edt_nama);
        edt_nohp                = view.findViewById(R.id.edt_nohp);
        edt_email               = view.findViewById(R.id.edt_email);
        spinner_tenor           = view.findViewById(R.id.spinner_tenor);
        spinner_provinsi        = view.findViewById(R.id.spinner_provinsi);
        spinner_kabupaten       = view.findViewById(R.id.spinner_kabupaten);
        spinner_kecamatan       = view.findViewById(R.id.spinner_kecamatan);

        btn_daftar              = view.findViewById(R.id.btn_daftar);

        swipe_downline          = view.findViewById(R.id.swipe_downline);

        linear_add_downline     = view.findViewById(R.id.linear_add_downline);
        linear_daftar_downline  = view.findViewById(R.id.linear_daftar_downline);
        linear_kosong           = view.findViewById(R.id.linear_kosong);

        dropwdownListHelper     = new DropwdownListHelper();

        list_downline           = view.findViewById(R.id.list_downline);

        setButton();
        setView();
        setDownline();
    }

    private void setView() {
        List<String> listMarkup             = dropwdownListHelper.listMarkup();
        ArrayAdapter<String> markupAdapter  = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listMarkup);

        spinner_tenor.setAdapter(markupAdapter);
        spinner_tenor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                markup  = spinner_tenor.getSelectedItem().toString().substring(4, spinner_tenor.getSelectedItem().toString().length());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipe_downline.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_downline.setRefreshing(false);
                setDownline();
            }
        });

        spinner_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                prov_item       = (Province) parent.getItemAtPosition(position);
                id_provinsi     = prov_item.getProvince_id();

                kabupaten(id_provinsi);
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

                kecamatan(id_kab);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setButton() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("list")){
                    backBtn.setVisibility(View.GONE);
                }
                else{
                    edt_nama.setText("");
                    edt_nohp.setText("");

                    linear_add_downline.setVisibility(View.GONE);
                    linear_daftar_downline.setVisibility(View.VISIBLE);
                    txt_tambah_downline.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.GONE);

                    txt_title.setText("Downline");

                    status      = "list";
                }
            }
        });

        txt_tambah_downline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("list")){
                    linear_add_downline.setVisibility(View.VISIBLE);
                    linear_daftar_downline.setVisibility(View.GONE);
                    backBtn.setVisibility(View.VISIBLE);
                    txt_tambah_downline.setVisibility(View.GONE);

                    txt_title.setText("Daftar Downline");

                    status      = "daftar";
//                    provinsi();
                }
                else{
                    linear_add_downline.setVisibility(View.GONE);
                    linear_daftar_downline.setVisibility(View.VISIBLE);
                    backBtn.setVisibility(View.GONE);

                    txt_title.setText("Downline");

                    status      = "list";
                }
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_nama.getText().toString().isEmpty()){
                    edt_nama.setError("Mohon isi nama downline!");
                    edt_nama.requestFocus();
                }
                else if (edt_nohp.getText().toString().isEmpty()){
                    edt_nohp.setError("Mohon isi nomor handphone downline!");
                    edt_nohp.requestFocus();
                }
                else if (edt_email.getText().toString().isEmpty()){
                    edt_email.setError("Mohon isi email downline!");
                    edt_email.requestFocus();
                }
                else{
                    regDownline(
                            edt_email.getText().toString(),
                            edt_nohp.getText().toString(),
                            edt_email.getText().toString()
                    );
                }
            }
        });
    }

    private void regDownline(String nama, String nohp, String email) {
        dialog.show();
        AndroidNetworking.post(Utils.BASE_URL + "daftar_downline")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("name", nama)
                .addBodyParameter("email", email)
                .addBodyParameter("notelp", nohp)
                .addBodyParameter("markup", markup)
                .setPriority(Priority.MEDIUM)
                .setTag("daftar_downline")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            dialog.dismiss();
                            String res_kode     = response.getString("response");
                            if (res_kode.equals("200")){
                                linear_add_downline.setVisibility(View.GONE);
                                linear_daftar_downline.setVisibility(View.VISIBLE);
                                backBtn.setVisibility(View.GONE);

                                txt_title.setText("Downline");

                                status      = "list";
                                FancyToast.makeText(getContext(), "Downline berhasil ditambahkan",
                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            }
                            else{
                                edt_email.setText("");
                                edt_nohp.setText("");

                                FancyToast.makeText(getContext(), response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            FancyToast.makeText(getContext(), "Gagal menambahkan downline\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            Log.d("Salah", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        FancyToast.makeText(getContext(), "Gagal menambahkan downline\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        Log.d("Salah", anError.getErrorDetail());
                    }
                });
    }

    private void setDownline() {
        dialog.show();
        list_downline.setAdapter(null);
        listDownline.clear();
        AndroidNetworking.get(Utils.BASE_URL + "get_downline")
                .addHeaders("Authorization", "Bearer " + MainActivity.token)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .setTag("get_downline")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Hasil", response.toString());
                            if (response.getString("response").equals("200")){
                                JSONArray array     = response.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++){
                                    JSONObject obj  = array.getJSONObject(i);
                                    ListDownline data   = new ListDownline(
                                            obj.getString("id"),
                                            obj.getString("name"),
                                            obj.getString("notelp"),
                                            obj.getString("jumlah_transaksi")
                                    );

                                    listDownline.add(data);
                                }
                                adapterDownline = new AdapterDownline(getContext(), listDownline);
                                list_downline.setAdapter(adapterDownline);
                                list_downline.setLayoutManager(new LinearLayoutManager(getContext()));
                                list_downline.setItemAnimator(new DefaultItemAnimator());
                                list_downline.setHasFixedSize(true);

                                adapterDownline.setOnItemClickListener(new AdapterDownline.DownlineOnClick() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        Intent intent   = new Intent(getContext(), DetailDownlineActivity.class);
                                        intent.putExtra("id", listDownline.get(position).getId());
                                        startActivity(intent);
                                    }
                                });

                                if (listDownline.size() == 0){
                                    linear_kosong.setVisibility(View.VISIBLE);
                                    list_downline.setVisibility(View.GONE);
                                }
                                else{
                                    linear_kosong.setVisibility(View.GONE);
                                    list_downline.setVisibility(View.VISIBLE);
                                }
                            }
                            else {
                                FancyToast.makeText(getContext(), response.getString("message"),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false);
                                linear_kosong.setVisibility(View.VISIBLE);
                                list_downline.setVisibility(View.GONE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            FancyToast.makeText(getContext(), "Gagal memuat downline\n" + e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false);
                            linear_kosong.setVisibility(View.VISIBLE);
                            list_downline.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        FancyToast.makeText(getContext(), "Gagal memuat downline\n" + anError.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false);
                        linear_kosong.setVisibility(View.VISIBLE);
                        list_downline.setVisibility(View.GONE);
                    }
                });

    }

    private void provinsi() {
        dialog.show();
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
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Salah", anError.getMessage());
                        dialog.dismiss();
                    }
                });
    }

    private void kabupaten(String id_provinsi) {
        dialog.show();
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
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Salah", anError.getMessage());
                        dialog.dismiss();
                    }
                });
    }

    private void kecamatan(String id_kab) {
        dialog.show();
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
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Salah", anError.getMessage());
                        dialog.dismiss();
                    }
                });
    }
}