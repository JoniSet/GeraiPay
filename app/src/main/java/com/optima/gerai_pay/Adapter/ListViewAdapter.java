package com.optima.gerai_pay.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.Model.HargaItem;
import com.optima.gerai_pay.R;

import java.util.List;


public class ListViewAdapter extends ArrayAdapter<HargaItem> {

    private final List<HargaItem> itemList;

    private Activity activity;

    public ListViewAdapter(List<HargaItem> itemList, int item_menu_pembelian, Activity activity) {
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
        ImageView img_premium = view.findViewById(R.id.img_premium);


        HargaItem ItemList = itemList.get(position);

        productName.setText(ItemList.getNamaProduk());
        Utils formatRupiah = new Utils();
        productPrice.setText(formatRupiah.formatRupiah(Double.parseDouble(String.valueOf(ItemList.getHarga()))));

        if (itemList.get(position).getPremium().equals("1")){
            img_premium.setVisibility(View.VISIBLE);
        }
        else{
            img_premium.setVisibility(View.GONE);
        }

        return view;
    }
}
