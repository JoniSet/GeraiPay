package com.optima.gerai_pay.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Model.Notifikasi;
import com.optima.gerai_pay.R;

import java.util.ArrayList;

public class NotifikasiAdapter extends RecyclerView.Adapter<com.optima.gerai_pay.Adapter.NotifikasiAdapter.NotifikasiViewHolder> {

    private ArrayList<Notifikasi> dataList;
    private ItemClickListener mClickListener;

    public NotifikasiAdapter(ArrayList<Notifikasi> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public com.optima.gerai_pay.Adapter.NotifikasiAdapter.NotifikasiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_notifikasi, parent, false);
        return new com.optima.gerai_pay.Adapter.NotifikasiAdapter.NotifikasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.optima.gerai_pay.Adapter.NotifikasiAdapter.NotifikasiViewHolder holder, int position) {
        holder.date.setText(dataList.get(position).getDate());
        holder.notif_title.setText(dataList.get(position).getNotif_title());
        holder.notif_subtitle.setText(dataList.get(position).getNotif_subtitle());
}

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class NotifikasiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView date, notif_title, notif_subtitle;

        public NotifikasiViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            notif_title = itemView.findViewById(R.id.notif_title);
            notif_subtitle = itemView.findViewById(R.id.notif_subtitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}

