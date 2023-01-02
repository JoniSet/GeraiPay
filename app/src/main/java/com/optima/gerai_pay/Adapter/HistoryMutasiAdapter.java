package com.optima.gerai_pay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Model.MutasiSaldo;
import com.optima.gerai_pay.R;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryMutasiAdapter extends RecyclerView.Adapter<HistoryMutasiAdapter.HistoryTransaksiViewHolder> {

    private final ArrayList<MutasiSaldo> dataList;
    Context context;
    String getToken;

    public HistoryMutasiAdapter(ArrayList<MutasiSaldo> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryMutasiAdapter.HistoryTransaksiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_mutasi, parent, false);

        SessionManager sessionManager = new SessionManager(context);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getToken = user.get(SessionManager.TOKEN);

        return new HistoryTransaksiViewHolder(view);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(HistoryMutasiAdapter.HistoryTransaksiViewHolder holder, int position) {
        holder.tglMutasi.setText(dataList.get(position).getTgl());
        holder.judulMutasi.setText(dataList.get(position).getKeterangan());

        Utils formatRupiah = new Utils();
        if (dataList.get(position).getStatus().equals("kredit")) {
            holder.nominalMutasi.setText("+" + formatRupiah.formatRupiah(Double.parseDouble(String.valueOf(dataList.get(position).getNominal()))));
            holder.nominalMutasi.setTextColor(ContextCompat.getColor(context, R.color.green_forest_primary));
        } else if (dataList.get(position).getStatus().equals("debit")) {
            holder.nominalMutasi.setText("-" + formatRupiah.formatRupiah(Double.parseDouble(String.valueOf(dataList.get(position).getNominal()))));
            holder.nominalMutasi.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public static class HistoryTransaksiViewHolder extends RecyclerView.ViewHolder {

        private final TextView tglMutasi;
        private final TextView judulMutasi;
        private final TextView nominalMutasi;

        public HistoryTransaksiViewHolder(View itemView) {
            super(itemView);
            tglMutasi = itemView.findViewById(R.id.tglMutasi);
            judulMutasi = itemView.findViewById(R.id.judulMutasi);
            nominalMutasi = itemView.findViewById(R.id.nominalMutasi);
        }
    }

}

