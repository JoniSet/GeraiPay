package com.optima.gerai_pay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.optima.gerai_pay.Model.Kabupaten;
import com.optima.gerai_pay.R;

import java.util.ArrayList;

public class AdapterSpinnerKabupaten extends ArrayAdapter<Kabupaten> {

    public AdapterSpinnerKabupaten(@NonNull Context context, ArrayList<Kabupaten> listItem) {
        super(context, 0, listItem);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_spinner, parent, false
            );
        }
        TextView textViewName           = convertView.findViewById(R.id.textView);
        TextView textViewId             = convertView.findViewById(R.id.textViewId);
        Kabupaten currentItem           = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getKabputen());
            textViewId.setText(currentItem.getKabaupatenid());
        }
        return convertView;
    }
}
