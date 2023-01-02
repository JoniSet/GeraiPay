package com.optima.gerai_pay.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.optima.gerai_pay.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SaldoAdapter extends RecyclerView.Adapter<SaldoAdapter.ViewHolder> {

    private final ArrayList<String> rvData;

    public SaldoAdapter(ArrayList<String> inputData) {
        rvData = inputData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nominal;

        public ViewHolder(View v) {
            super(v);
            nominal = (TextView) v.findViewById(R.id.nominal);
        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saldo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nominal.setText(rvData.get(position));
    }

    @Override
    public int getItemCount() {
        return rvData.size();
    }
}
