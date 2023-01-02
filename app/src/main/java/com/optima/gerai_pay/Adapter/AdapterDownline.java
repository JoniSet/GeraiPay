package com.optima.gerai_pay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Model.ListDownline;
import com.optima.gerai_pay.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterDownline extends RecyclerView.Adapter<AdapterDownline.ViewHolder> {
    private Context context;
    private ArrayList<ListDownline> listDo;
    private static DownlineOnClick downlineOnClick;

    public AdapterDownline(Context context, ArrayList<ListDownline> listDo) {
        this.context = context;
        this.listDo = listDo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_downline, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListDownline data    = listDo.get(position);

        holder.txt_jml_tf.setText(data.getJumlah_transfer());
        holder.txt_notelp.setText(data.getNotelp());
        holder.txt_name.setText(data.getNama());
    }

    @Override
    public int getItemCount() {
        return listDo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt_name, txt_notelp, txt_jml_tf;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_kode);
            txt_notelp = itemView.findViewById(R.id.txt_nama);
            txt_jml_tf  = itemView.findViewById(R.id.txt_jml_tf);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            downlineOnClick.onClick(v, getAdapterPosition());
        }
    }

    public interface DownlineOnClick {
        void onClick(View v, int position);
    }

    public void setOnItemClickListener(DownlineOnClick downlineOnClick){
        AdapterDownline.downlineOnClick = downlineOnClick;
    }


    public String parseDate(String time) {
//        String inputPattern = "yyyy-MM-dd HH:mm:ss";
//        String outputPattern = "dd MMM yyyy HH:mm:ss";
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yyyy";
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
