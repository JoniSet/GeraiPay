package com.optima.gerai_pay.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.optima.gerai_pay.Model.AlamatItem;
import com.optima.gerai_pay.R;

import java.util.List;

public class AlamatListAdapter extends ArrayAdapter<AlamatItem> {

    private final List<AlamatItem> itemList;

    private Activity activity;

    public AlamatListAdapter(List<AlamatItem> itemList, int item_list, Activity activity) {
        super(activity, R.layout.item_list, itemList);
        this.itemList = itemList;
        this.activity = activity;
    }

    @NonNull
    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_list, null, true);

        }

        TextView nama_list = view.findViewById(R.id.nama_list);


        AlamatItem ItemList = itemList.get(position);

        nama_list.setText(ItemList.getName());

        return view;
    }
}