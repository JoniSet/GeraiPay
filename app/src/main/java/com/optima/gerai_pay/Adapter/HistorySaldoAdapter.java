package com.optima.gerai_pay.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Activity.DetailTopUpHistory;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Model.HistorySaldoModel;
import com.optima.gerai_pay.R;

import java.util.ArrayList;

public class HistorySaldoAdapter extends RecyclerView.Adapter<HistorySaldoAdapter.HistoryTransaksiViewHolder> {

    private final ArrayList<HistorySaldoModel> dataList;
    Context context;
    private static NewsOnClick newsOnClick;

    public HistorySaldoAdapter(ArrayList<HistorySaldoModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistorySaldoAdapter.HistoryTransaksiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_saldo_history, parent, false);
        return new HistoryTransaksiViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(HistorySaldoAdapter.HistoryTransaksiViewHolder holder, int position) {
        holder.orderID.setText("Order ID : " + dataList.get(position).getOrderId());
        holder.status.setText(dataList.get(position).getStatus());
        holder.productName.setText(dataList.get(position).getProduk());
        holder.trxDate.setText(dataList.get(position).getTgl());

        Utils formatRupiah = new Utils();
        holder.jumlahSaldo.setText(formatRupiah.formatRupiah(Double.parseDouble(String.valueOf(dataList.get(position).getTotal()))));

        switch (dataList.get(position).getStatus()) {
            case "gagal":
                holder.statusLayout.setBackgroundResource(R.drawable.round_button_red);
                break;
            case "sukses":
                holder.statusLayout.setBackgroundResource(R.drawable.round_button_green);
                break;
            case "proses":
                holder.statusLayout.setBackgroundResource(R.drawable.round_button_orange);
                break;
        }

//        holder.cardView.setOnClickListener(view -> {
//            Intent intent = new Intent(context, DetailTopUpHistory.class);
//            intent.putExtra("orderId", dataList.get(position).getOrderId());
//            context.startActivity(intent);
//            ((Activity)context).finish();
//        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public static class HistoryTransaksiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView orderID;
        private final TextView productName;
        private final TextView trxDate;
        private final TextView status;
        private final TextView jumlahSaldo;
        private final LinearLayout statusLayout;
        private final CardView cardView;

        public HistoryTransaksiViewHolder(View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.orderID);
            productName = itemView.findViewById(R.id.productName);
            trxDate = itemView.findViewById(R.id.trxDate);
            status = itemView.findViewById(R.id.status);
            jumlahSaldo = itemView.findViewById(R.id.jumlahSaldo);
            statusLayout = itemView.findViewById(R.id.statusLayout);
            cardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            newsOnClick.onClick(v, getAdapterPosition());
        }
    }

    public interface NewsOnClick {
        void onClick(View v, int position);
    }

    public void setOnItemClickListener(NewsOnClick newsOnClick){
        HistorySaldoAdapter.newsOnClick = newsOnClick;
    }

}

