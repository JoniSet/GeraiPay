package com.optima.gerai_pay.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.optima.gerai_pay.Model.TagihanList;
import com.optima.gerai_pay.R;

import java.util.List;


public class TagihanListViewAdapter extends ArrayAdapter<TagihanList> {

    private final List<TagihanList> itemList;

    private Activity activity;

    public TagihanListViewAdapter(List<TagihanList> itemList, int item_menu_pembelian, Activity activity) {
        super(activity, R.layout.item_menu_pembelian, itemList);
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
            view = layoutInflater.inflate(R.layout.item_menu_pembelian, null, true);

        }

        TextView productName = view.findViewById(R.id.title_nominal);
        TextView productPrice = view.findViewById(R.id.price);


        TagihanList ItemList = itemList.get(position);

        productName.setText(ItemList.getProductName());
        productPrice.setText(ItemList.getBiayaAdmin());

        return view;
    }
}