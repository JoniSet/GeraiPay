package com.optima.gerai_pay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Helper.SqliteHelper;
import com.optima.gerai_pay.Model.ListProdukPembayaran;
import com.optima.gerai_pay.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProdukPembayaran extends RecyclerView.Adapter<AdapterProdukPembayaran.ViewHolder> {
    private Context context;
    private ArrayList<ListProdukPembayaran> listProduk;
    private static ProdukPembayaranOnClick produkOnClick;

    SqliteHelper sqliteHelper;

    public AdapterProdukPembayaran(Context context, ArrayList<ListProdukPembayaran> listProduk) {
        this.context    = context;
        this.listProduk = listProduk;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListProdukPembayaran data   = listProduk.get(position);

        holder.txt_produk.setText(data.getNama_kategori());
        if (data.getLogo().isEmpty()){
            holder.img_produk.setImageResource(R.drawable.ic_pulsa);
        }
        else{
            Picasso.get()
                    .load(data.getLogo())
                    .into(holder.img_produk);
        }

        settingLayout(holder.img_produk);
    }

    @Override
    public int getItemCount() {
        return listProduk.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt_produk;
        ImageView img_produk;
        RelativeLayout layout_parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_produk      = itemView.findViewById(R.id.txt_produk);
            img_produk      = itemView.findViewById(R.id.img_produk);
            layout_parent   = itemView.findViewById(R.id.layout_parent);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            produkOnClick.onClick(v, getAdapterPosition());
        }
    }

    public interface ProdukPembayaranOnClick{
        void onClick(View v, int position);
    }

    public void setOnItemClickListener(ProdukPembayaranOnClick produkOnClick){
        AdapterProdukPembayaran.produkOnClick   = produkOnClick;
    }

    public void settingLayout(View v) {
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int availableHeight = v.getMeasuredWidth();
                if (availableHeight > 0) {
                    v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    //save height here and do whatever you want with it
                    ViewGroup.LayoutParams params = v.getLayoutParams();

                    params.height = availableHeight;
                    params.width = availableHeight;
                    v.setLayoutParams(params);
                }
            }
        });
    }

}
