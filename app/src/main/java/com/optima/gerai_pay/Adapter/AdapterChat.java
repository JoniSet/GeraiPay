package com.optima.gerai_pay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Helper.Tanggal;
import com.optima.gerai_pay.Model.ListChat;
import com.optima.gerai_pay.R;

import java.util.ArrayList;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ViewHolder> {

    private Context context;
    private ArrayList<ListChat> listChat;
    private Tanggal tanggal         = new Tanggal();

    public AdapterChat(Context context, ArrayList<ListChat> listChat) {
        this.context = context;
        this.listChat = listChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view           = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListChat data                       = listChat.get(position);

        //id, tujuan_user, dari_id (0 jika admin), pesan, tgl, baca
        //tujuan_user 0 = user ke admin  -- dari_id 0 = admin ke user

        if (!data.getTujuan_user().equals("0"))
        {
            holder.L_admin.setVisibility(View.VISIBLE);
            holder.L_user.setVisibility(View.GONE);
            holder.txt_admin.setText(data.getPesan());

            if (data.getTgl().substring(0, 10).equals(tanggal.getTanggal()))
            {
                holder.txt_waktu_admin.setText(data.getTgl().substring(11, 16));
            }
            else {
                holder.txt_waktu_admin.setText(data.getTgl().substring(0, 10));
            }
        }
        else
        {
            holder.L_admin.setVisibility(View.GONE);
            holder.L_user.setVisibility(View.VISIBLE);
            holder.txt_user.setText(data.getPesan());

            if (data.getTgl().substring(0, 10).equals(tanggal.getTanggal()))
            {
                holder.txt_waktu_user.setText(data.getTgl().substring(11, 16));
            }
            else {
                holder.txt_waktu_user.setText(data.getTgl().substring(0, 10));
            }
        }
    }

    @Override
    public int getItemCount() {
        return listChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout L_admin, L_user;
        TextView txt_admin, txt_user, txt_waktu_user, txt_waktu_admin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            L_admin                 = itemView.findViewById(R.id.L_admin);
            L_user                  = itemView.findViewById(R.id.L_user);
            txt_admin               = itemView.findViewById(R.id.txt_chat_admin);
            txt_user                = itemView.findViewById(R.id.txt_chat_user);
            txt_waktu_user          = itemView.findViewById(R.id.txt_jam_uaer);
            txt_waktu_admin         = itemView.findViewById(R.id.txt_jam_admin);


        }
    }
}
