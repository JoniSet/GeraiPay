package com.optima.gerai_pay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Activity.KomisiActivity;
import com.optima.gerai_pay.Model.ListLaporanKomisi;
import com.optima.gerai_pay.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterLaporanKomisi extends RecyclerView.Adapter<AdapterLaporanKomisi.ViewHolder> {
    private Context context;
    private ArrayList<ListLaporanKomisi> listLaporanKomisis;

    public AdapterLaporanKomisi(Context context, ArrayList<ListLaporanKomisi> listLaporanKomisis) {
        this.context = context;
        this.listLaporanKomisis = listLaporanKomisis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListLaporanKomisi data    = listLaporanKomisis.get(position);
        if (KomisiActivity.filter_tipe.equals("hari")){
            holder.txt_tanggal.setText(parseDate(data.getTgl()));
        }
        else if (KomisiActivity.filter_tipe.equals("bulan")){
            holder.txt_tanggal.setText(parseBulan(data.getTgl()));
        }
        else {
            holder.txt_tanggal.setText(parseBulan(data.getTgl()));
        }

        holder.txt_jml_transaksi.setText(data.getName());
        holder.txt_keuntungan.setText(data.getKomisi());
    }

    @Override
    public int getItemCount() {
        return listLaporanKomisis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_tanggal, txt_jml_transaksi, txt_keuntungan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_keuntungan      = itemView.findViewById(R.id.txt_keuntungan);
            txt_jml_transaksi   = itemView.findViewById(R.id.txt_jml_transaksi);
            txt_tanggal         = itemView.findViewById(R.id.txt_tanggal);
        }
    }


    public String parseDate(String time) {
        String inputPattern = "dd-MM";
        String outputPattern = "dd MMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public String parseBulan(String time) {
        String inputPattern = "MM-yyyy";
        String outputPattern = "MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
