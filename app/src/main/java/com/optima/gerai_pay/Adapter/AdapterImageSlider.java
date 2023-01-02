package com.optima.gerai_pay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.optima.gerai_pay.Model.ListImageSlider;
import com.optima.gerai_pay.R;

import java.util.ArrayList;

public class AdapterImageSlider extends RecyclerView.Adapter<AdapterImageSlider.ViewHolder> {
    ViewPager2 viewPager2;
    ArrayList<ListImageSlider> sliderItems;
    Context context;
    private static BannerOnClick bannerOnClick;

    public AdapterImageSlider(ViewPager2 viewPager2, ArrayList<ListImageSlider> sliderItems, Context context) {
        this.viewPager2     = viewPager2;
        this.sliderItems    = sliderItems;
        this.context        = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view       = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_slider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(sliderItems.get(position).getGambar());

        if (position == sliderItems.size()- 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        RoundedImageView imageView, myimage2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView       = itemView.findViewById(R.id.myimage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            bannerOnClick.onClick(v, getAdapterPosition());
        }
    }

    private Runnable runnable  = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public interface BannerOnClick {
        void onClick(View v, int position);
    }

    public void setOnCLickListener(BannerOnClick bannerOnClick){
        AdapterImageSlider.bannerOnClick = bannerOnClick;
    }
}
