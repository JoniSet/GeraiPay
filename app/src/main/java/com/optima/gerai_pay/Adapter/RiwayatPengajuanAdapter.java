package com.optima.gerai_pay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Helper.ViewAnimation;
import com.optima.gerai_pay.Model.PengajuanItem;
import com.optima.gerai_pay.R;

import java.util.ArrayList;

public class RiwayatPengajuanAdapter extends RecyclerView.Adapter<RiwayatPengajuanAdapter.ProductViewHolder> {

    private ArrayList<PengajuanItem> dataList;
    Context context;
    private String getId;

    public RiwayatPengajuanAdapter(ArrayList<PengajuanItem> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_riwayat_pengajuan, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        holder.tanggalPengajuan.setText(dataList.get(position).getTanggalPengajuan());
        holder.namaPelanggan.setText(dataList.get(position).getNamaPelanggan());
        holder.product.setText(dataList.get(position).getProductName());
        holder.nomorPelanggan.setText(dataList.get(position).getHandphone());
        holder.emailPelanggan.setText(dataList.get(position).getEmail());

        holder.showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSectionText(holder.showMore);
            }

            private void toggleSectionText(View view) {
                boolean show = toggleArrow(view);
                if (show) {
                    ViewAnimation.expand(holder.detailLayout, () -> Utils.nestedScrollTo(holder.nested_scroll_view, holder.detailLayout));
                } else {
                    ViewAnimation.collapse(holder.detailLayout);
                }
            }
        });

    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView tanggalPengajuan;
        private final ImageView showMore;
        private final View detailLayout;
        private final TextView namaPelanggan;
        private final TextView product;
        private final TextView nomorPelanggan;
        private final TextView emailPelanggan;
        private final NestedScrollView nested_scroll_view;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tanggalPengajuan = itemView.findViewById(R.id.tanggalPengajuan);
            showMore = itemView.findViewById(R.id.showMore);
            detailLayout = itemView.findViewById(R.id.detailLayout);
            namaPelanggan = itemView.findViewById(R.id.namaPelanggan);
            product = itemView.findViewById(R.id.product);
            nomorPelanggan = itemView.findViewById(R.id.nomorPelanggan);
            emailPelanggan = itemView.findViewById(R.id.emailPelanggan);
            nested_scroll_view = itemView.findViewById(R.id.nested_scroll_view);
        }
    }

    public void filterList(ArrayList<PengajuanItem> filteredList) {
        dataList = filteredList;
        notifyDataSetChanged();
    }
}