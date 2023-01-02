package com.optima.gerai_pay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Helper.SqliteHelper;
import com.optima.gerai_pay.Model.ListNotif;
import com.optima.gerai_pay.R;

import java.util.ArrayList;

public class AdapterNotif extends RecyclerView.Adapter<AdapterNotif.ViewHolder> {
    private Context context;
    private ArrayList<ListNotif> listNotifs;
    private static bacaNotifOnClick bacaNotifOnClicks;

    SqliteHelper sqliteHelper;

    public AdapterNotif(Context context, ArrayList<ListNotif> listNotifs) {
        this.context = context;
        this.listNotifs = listNotifs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifikasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListNotif data   = listNotifs.get(position);
        holder.txt_tanggal.setText(data.getTanggal());
        holder.txt_notif_title.setText(data.getTitle());
        holder.txt_notif_body.setText(data.getBody());

        if (data.getKategori().equals("4")){ //informasi
            holder.img_type.setImageResource(R.drawable.ic_informasi);
            holder.notif_tipe.setText("Informasi");
        }
        else {
            holder.img_type.setImageResource(R.drawable.ic_penawaran);
            holder.notif_tipe.setText("Penawaran");
        }
    }

    @Override
    public int getItemCount() {
        return listNotifs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt_tanggal, txt_notif_title, txt_notif_body, notif_tipe;
        ImageView img_type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_tanggal     = itemView.findViewById(R.id.date);
            txt_notif_title = itemView.findViewById(R.id.notif_title);
            txt_notif_body  = itemView.findViewById(R.id.notif_subtitle);
            notif_tipe      = itemView.findViewById(R.id.notif_tipe);
            img_type        = itemView.findViewById(R.id.img_type);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            bacaNotifOnClicks.onClick(v, getAdapterPosition());
        }
    }

    public interface bacaNotifOnClick{
        void onClick(View v, int position);
    }

    public void setOnItemClickListener(bacaNotifOnClick bacaNotifOnClicks){
        AdapterNotif.bacaNotifOnClicks   = bacaNotifOnClicks;
    }

}
