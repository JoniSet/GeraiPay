package com.optima.gerai_pay.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.MainActivity;
import com.optima.gerai_pay.Model.HistoryModel;
import com.optima.gerai_pay.R;
import com.optima.gerai_pay.async.AsyncBluetoothEscPosPrint;
import com.optima.gerai_pay.async.AsyncEscPosPrinter;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HistoryTransaksiAdapter extends RecyclerView.Adapter<com.optima.gerai_pay.Adapter.HistoryTransaksiAdapter.HistoryTransaksiViewHolder> {

    private final ArrayList<HistoryModel> dataList;
    Context context;
    String getToken;
    Activity activity;

    BluetoothAdapter bluetoothAdapter;
    private BluetoothConnection selectedDevice;

    public static final int PERMISSION_BLUETOOTH = 1;

    String harga_jual;

    public HistoryTransaksiAdapter(ArrayList<HistoryModel> dataList, Context context, Activity activity) {
        this.dataList = dataList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public com.optima.gerai_pay.Adapter.HistoryTransaksiAdapter.HistoryTransaksiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_transaction_history, parent, false);

        SessionManager sessionManager = new SessionManager(context);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getToken = user.get(SessionManager.TOKEN);

        return new HistoryTransaksiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.optima.gerai_pay.Adapter.HistoryTransaksiAdapter.HistoryTransaksiViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.date.setText(dataList.get(position).getTgl());
        holder.status.setText(dataList.get(position).getStatus());
        holder.product_name.setText(dataList.get(position).getNamaProduk());
        holder.nohp.setText(dataList.get(position).getTarget());

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String sisa_saldo = formatter.format(Long.parseLong(String.valueOf(dataList.get(position).getHarga())));

        holder.saldo.setText("Rp. " + sisa_saldo);

        switch (dataList.get(position).getStatus()) {
            case "gagal":
                holder.status.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
                break;
            case "sukses":
                holder.status.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.hijau)));
                break;
            case "proses":
                holder.status.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.orange_EE9)));
                break;
        }

        holder.cardView.setOnClickListener(view -> getDetail(dataList.get(position).getTransaksiId()));

        holder.img_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetailToPrint(dataList.get(position).getTransaksiId(), "print");
            }
        });

        holder.img_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetailToPrint(dataList.get(position).getTransaksiId(), "share");
            }
        });

    }

    private void getDetail(final String trxID){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait");
        dialog.show();

        String URL_LOGIN = Utils.BASE_URL + "historibyid";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    try{
                        Log.d("Salah", response.toString());
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");
                        JSONArray jsonArray = jsonObject.getJSONArray("history");

                        Log.d("Hasil", response.toString());

                        if (responseCode.equals("200")) {
                            dialog.dismiss();
                            for (int k = 0; k < jsonArray.length(); k++) {
                                JSONObject object = jsonArray.getJSONObject(k);
                                String strTgl = object.getString("tgl");
                                String strTrxID = object.getString("transaksi_id").trim();
                                String strKodeProduk = object.getString("kode_produk");
                                String strNamaProduk = object.getString("nama_produk");
                                String strTarget = object.getString("target");
                                String strHarga = object.getString("harga");
                                String strNote = object.getString("note");
                                String strStatus = object.getString("status");
                                String strNomorMeter = object.getString("no_meter_pln");
                                String strToken = object.getString("token");
                                String harga_jual = object.getString("harga_jual");

                                showTrxDetail(strTgl, strTrxID, strKodeProduk, strNamaProduk, strTarget, strHarga, strNote, strStatus, strNomorMeter, strToken, harga_jual);
                            }
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("transaksi_id", trxID);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showTrxDetail(final String getTrxDate, final String getTrxID, final String getKodeProduk,
                               final String getNamaProduk, final String getTarget, final String getHarga,
                               final String getNote, final String getStatus, final String getNomorMeter,
                               final String getToken, final String harga_jual) {

        Dialog dialog       = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_detail_trx);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        final TextView trxDate      = dialog.findViewById(R.id.trxDate);
        final TextView trxID        = dialog.findViewById(R.id.trxID);
        final TextView kodeProduk   = dialog.findViewById(R.id.kodeProduk);
        final TextView namaProduk   = dialog.findViewById(R.id.namaProduk);
        final TextView target       = dialog.findViewById(R.id.target);
        final TextView harga        = dialog.findViewById(R.id.harga);
        final TextView hrg_jual     = dialog.findViewById(R.id.harga_jual);
        final TextView note         = dialog.findViewById(R.id.note);
        final TextView status       = dialog.findViewById(R.id.trxStatus);
        final LinearLayout tokenLayout = dialog.findViewById(R.id.tokenLayout);
        final TextView token        = dialog.findViewById(R.id.token);
        final ImageView coptyToken  = dialog.findViewById(R.id.copyToken);
        final TextView nomorMeter   = dialog.findViewById(R.id.nomorMeter);

        trxDate.setText(getTrxDate);
        trxID.setText(getTrxID);
        kodeProduk.setText(getKodeProduk);
        namaProduk.setText(getNamaProduk);
        target.setText(getTarget);

        if (getNomorMeter.equals("")) {
            tokenLayout.setVisibility(View.GONE);
        } else if (getNomorMeter.equals("null")) {
            tokenLayout.setVisibility(View.GONE);
        } else {
            token.setText(getToken);
            nomorMeter.setText(getNomorMeter);
        }

        coptyToken.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("text", token.getText().toString());
            clipboard.setPrimaryClip(clip);
            toastIcon("Token Telah Disalin !", R.drawable.ic_checklist, R.color.green_forest_primary);
        });

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String harga_produk = formatter.format(Long.parseLong(String.valueOf(getHarga)));
        harga.setText("Rp. " + harga_produk);

        if (harga_jual.equals("null")){
            hrg_jual.setText("Rp. " + formatter.format(Long.parseLong(String.valueOf(getHarga))));
        }
        else{
            hrg_jual.setText("Rp. " + formatter.format(Long.parseLong(String.valueOf(harga_jual))));
        }

        if (getStatus.equals("sukses")) {
            status.setTextColor(context.getResources().getColor(R.color.green_forest_primary));
            status.setText(getStatus);
        } else if (getStatus.equals("proses")) {
            status.setTextColor(context.getResources().getColor(R.color.colorAccent));
            status.setText(getStatus);
        } else if (getStatus.equals("gagal")) {
            status.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            status.setText(getStatus);
        }

        if (getNote.equals("")) {
            note.setText("-");
        } else {
            note.setText(getNote);
        }
    }

    private void toastIcon(final String message, final int image, final int color) {
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);

        @SuppressLint("InflateParams") View custom_view = LayoutInflater.from(context).inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(image);
        ((ImageView) custom_view.findViewById(R.id.icon)).setColorFilter(android.R.color.white);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(color));

        toast.setView(custom_view);
        toast.show();
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public static class HistoryTransaksiViewHolder extends RecyclerView.ViewHolder {

        private final TextView date;
        private final TextView status;
        private final TextView product_name;
        private final TextView nohp;
        private final TextView saldo;
        private final CardView cardView;
        private final ImageView img_print, img_wa;

        public HistoryTransaksiViewHolder(View itemView) {
            super(itemView);
            date            = itemView.findViewById(R.id.date);
            status          = itemView.findViewById(R.id.status);
            product_name    = itemView.findViewById(R.id.product_name_transaction);
            nohp            = itemView.findViewById(R.id.nohp);
            saldo           = itemView.findViewById(R.id.saldo);
            cardView        = itemView.findViewById(R.id.cardView);
            img_print       = itemView.findViewById(R.id.img_print);
            img_wa          = itemView.findViewById(R.id.img_wa);
        }
    }




    private void getDetailToPrint(final String trxID, String tipe){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait");
        dialog.show();

        String URL_LOGIN = Utils.BASE_URL + "historibyid";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String responseCode = jsonObject.getString("response");
                        JSONArray jsonArray = jsonObject.getJSONArray("history");

                        if (responseCode.equals("200")) {
                            dialog.dismiss();
                            for (int k = 0; k < jsonArray.length(); k++) {
                                JSONObject object = jsonArray.getJSONObject(k);
                                String strTgl = object.getString("tgl");
                                String strTrxID = object.getString("transaksi_id").trim();
                                String strKodeProduk = object.getString("kode_produk");
                                String strNamaProduk = object.getString("nama_produk");
                                String strTarget = object.getString("target");
                                String strHarga = object.getString("harga");
                                String strNote = object.getString("note");
                                String strStatus = object.getString("status");
                                String strNomorMeter = object.getString("no_meter_pln");
                                String strToken = object.getString("token");

                                if (object.getString("harga_jual").equals("null")){
                                    harga_jual  = strHarga;
                                }
                                else{
                                    harga_jual = object.getString("harga_jual");
                                }

                                if (tipe.equals("print")) {
                                    cariBluetooth(
                                            strTrxID,
                                            strTgl,
                                            strTarget,
                                            strNamaProduk,
                                            harga_jual,
                                            strNomorMeter,
                                            strToken,
                                            strStatus
                                    );
                                }
                                else{
                                    sendWa(
                                            strTrxID,
                                            strTgl,
                                            strTarget,
                                            strNamaProduk,
                                            harga_jual,
                                            strNomorMeter,
                                            strToken,
                                            strStatus
                                    );
                                }
                            }
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("transaksi_id", trxID);
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + getToken);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    void cariBluetooth(String strTrxID, String tgl, String no, String produk, String harga, String no_meter, String token, String status){
        bluetoothAdapter    = BluetoothAdapter.getDefaultAdapter();
        try
        {
            if (bluetoothAdapter == null)
            {
                FancyToast.makeText(context,  "Bluetooth Tidak Support!",
                        FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
            else{
                if (!bluetoothAdapter.isEnabled())
                {
                    FancyToast.makeText(context,  "Mohon aktifkan bluetooth anda!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                }
                else {
                    show_dialog(strTrxID, tgl, no, produk, harga, no_meter, token, status);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void show_dialog(String strTrxID, String tgl, String no, String produk, String harga, String no_meter, String token, String status) {
        final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();

        if (bluetoothDevicesList != null) {
            final String[] items = new String[bluetoothDevicesList.length + 1];
            items[0] = "Default Printer";

            int i = 0;
            for (BluetoothConnection device : bluetoothDevicesList) {
                items[++i] = device.getDevice().getName();
            }

            int index = i - 1;
            if(index == -1) {
                selectedDevice = null;
            } else {
                selectedDevice = bluetoothDevicesList[index];
                doPrint(strTrxID, tgl, no, produk, harga, no_meter, token, status);
            }
        }
    }

    public void doPrint(String strTrxID, String tgl, String no, String produk, String harga, String no_meter, String token, String status) {
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH},1);
            } else {

                new AsyncBluetoothEscPosPrint(context).execute(this.getAsyncEscPosPrinter(selectedDevice, strTrxID,  tgl, no, produk, harga, no_meter, token, status));

            }
        } catch (Exception e) {
            Log.e("APP", "Can't print", e);
        }
    }

    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection, String strTrxID,  String tgl, String no, String produk, String harga, String no_meter, String token, String status) {
        Utils formatRupiah = new Utils();

        AsyncEscPosPrinter printer  = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);

        String print = "";
        if (!no_meter.equals("null")){
            print = "[C]" + MainActivity.nama + "\n" +
                    "[C]" + "STRUK PEMBAYARAN" + "\n" +
                    "[C]================================\n" +
                    "[L]" + tgl + "\n\n" +
                    "[L]ID Transaksi[R]" + strTrxID + "\n" +
                    "[L]Nomor[R]" + no + "\n" +
                    "[L]Produk[R]" + produk + "\n" +
                    "[L]Harga[R]" + formatRupiah.formatRupiah(Double.parseDouble(harga)) + "\n\n" +
                    "[L]ID Pel[R]" + no_meter + "\n" +
                    "[L]Status[R]" + status + "\n" +
                    "[L]Token\n" +
                    "[L]" + token + "\n" +
                    "[C]================================\n\n" +
                    "[C]Terima Kasih \n" +
                    "[C]**************** \n" +
                    "[C]GERAI PAY";
        }
        else {
            print = "[C]" + MainActivity.nama + "\n" +
                    "[C]" + "STRUK PEMBELIAN" + "\n" +
                    "[C]================================\n" +
                    "[L]" + tgl + "\n\n" +
                    "[L]ID Transaksi[R]" + strTrxID + "\n" +
                    "[L]Nomor[R]" + no + "\n" +
                    "[L]Produk[R]" + produk + "\n" +
                    "[L]Harga[R]" + formatRupiah.formatRupiah(Double.parseDouble(harga)) + "\n" +
                    "[L]Status[R]" + status + "\n" +
                    "[C]================================\n\n" +
                    "[C]Terima Kasih \n" +
                    "[C]**************** \n" +
                    "[C]GERAI PAY";
        }

        return printer.setTextToPrint(print);
    }

    public void sendWa(String strTrxID, String tgl, String no, String produk, String harga, String no_meter, String token, String status){
        String body;
        if (!no_meter.equals("null")){
            body     =
                    "Tanggal : " + tgl + "\n" +
                            "ID Trans : " + strTrxID + "\n" +
                            "Nomor : " + no + "\n" +
                            "Produk : " + produk + "\n" +
                            "Harga : Rp. " + harga + "\n" +
                            "ID Pel : " + no_meter + "\n" +
                            "Token : " + token + "\n" +
                            "Status : " + status;
        }
        else {
            body     =
                    "Tanggal : " + tgl + "\n" +
                            "ID Trans : " + strTrxID + "\n" +
                            "Nomor : " + no + "\n" +
                            "Produk : " + produk + "\n" +
                            "Harga : Rp. " + harga + "\n" +
                            "Status : " + status
            ;
        }


        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            //sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, body);
            sendIntent.setPackage("com.whatsapp");
            context.startActivity(sendIntent);
        } catch(Exception e) {
            Toast.makeText(context, "Anda tidak punya Aplikasi Whatsapp!", Toast.LENGTH_SHORT).show();
        }

    }
}
//515070060679
