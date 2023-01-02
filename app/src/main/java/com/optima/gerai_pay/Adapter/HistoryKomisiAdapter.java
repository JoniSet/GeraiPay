package com.optima.gerai_pay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Model.ListKomisiItem;
import com.optima.gerai_pay.R;

import java.util.ArrayList;

public class HistoryKomisiAdapter extends RecyclerView.Adapter<HistoryKomisiAdapter.HistoryKomisiViewHolder> {

    private final ArrayList<ListKomisiItem> dataList;
    Context context;

    public HistoryKomisiAdapter(ArrayList<ListKomisiItem> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryKomisiAdapter.HistoryKomisiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_komisi, parent, false);
        return new HistoryKomisiViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(HistoryKomisiAdapter.HistoryKomisiViewHolder holder, int position) {
        holder.namaDownline.setText(dataList.get(position).getDownlineName());
        holder.levelDownline.setText("Level Downline : " + String.valueOf(dataList.get(position).getLevelDownline()));

        Utils formatRupiah = new Utils();
        holder.komisiValue.setText(formatRupiah.formatRupiah((double) dataList.get(position).getKomisi()));
        holder.trxID.setText(dataList.get(position).getTransaksiId());

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public static class HistoryKomisiViewHolder extends RecyclerView.ViewHolder {

        private final TextView namaDownline, levelDownline, komisiValue, trxID;

        public HistoryKomisiViewHolder(View itemView) {
            super(itemView);
            namaDownline = itemView.findViewById(R.id.namaDownline);
            levelDownline = itemView.findViewById(R.id.levelDownline);
            komisiValue = itemView.findViewById(R.id.komisiValue);
            trxID = itemView.findViewById(R.id.trxID);
        }
    }

}

