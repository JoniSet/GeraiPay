package com.optima.gerai_pay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Activity.KonfirmasiTransaksiPembelian;
import com.optima.gerai_pay.Model.TagihanList;
import com.optima.gerai_pay.R;

import java.util.ArrayList;

public class TagihanAdapter extends RecyclerView.Adapter<TagihanAdapter.ProdukViewHolder> {
    private ArrayList<TagihanList> itemList;
    private Context context;

    public TagihanAdapter(Context context, ArrayList<TagihanList> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_pembayaran, parent,false);
        return new ProdukViewHolder(layoutView);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ProdukViewHolder holder, int position) {
        holder.titleNominal.setText(itemList.get(position).getProductName());
        holder.price.setText(itemList.get(position).getBiayaAdmin());
        holder.cardView.setOnClickListener(view -> {
            Intent intent=new Intent(context, KonfirmasiTransaksiPembelian.class);
            //intent.putExtra("product_name",itemList.get(position).getProductName());
            //intent.putExtra("price",itemList.get(position).getPrice());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public static class ProdukViewHolder extends RecyclerView.ViewHolder{
        public TextView titleNominal;
        public TextView price;
        public CardView cardView;
        public ProdukViewHolder(View itemView) {
            super(itemView);
            titleNominal = (TextView)itemView.findViewById(R.id.title_nominal);
            price = (TextView)itemView.findViewById(R.id.price);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
