package com.optima.gerai_pay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.optima.gerai_pay.Model.DataTraining;
import com.optima.gerai_pay.R;

import java.util.ArrayList;

public class AdapterTraining extends RecyclerView.Adapter<AdapterTraining.ViewHolder>{
    private Context context;
    private ArrayList<DataTraining> dataTraining;

    private static TrainingOnClick trainingOnClick;

    public AdapterTraining(Context context, ArrayList<DataTraining> dataTraining) {
        this.context = context;
        this.dataTraining = dataTraining;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_training, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataTraining data        = dataTraining.get(position);
        holder.txt_title.setText(data.getTitle());
        holder.img_training.setImageResource(data.getGambar());
    }

    @Override
    public int getItemCount() {
        return dataTraining.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txt_title;
        ImageView img_training;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_training     = itemView.findViewById(R.id.img_training);
            txt_title        = itemView.findViewById(R.id.txt_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            trainingOnClick.onClick(v, getAdapterPosition());
        }
    }

    public interface TrainingOnClick {
        void onClick(View v, int position);
    }

    public void setOnItemClickListener(TrainingOnClick trainingOnClick){
        AdapterTraining.trainingOnClick = trainingOnClick;
    }
}
