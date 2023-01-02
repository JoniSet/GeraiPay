package com.optima.gerai_pay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.optima.gerai_pay.R;

public class AllMenuPpobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_menu_ppob);

        //Pembelian
        ImageView backBtn = findViewById(R.id.backBtn);
        LinearLayout tokenListrik = findViewById(R.id.tokenListrik);
        LinearLayout vocGplay = findViewById(R.id.vocGplay);
        LinearLayout freeFire = findViewById(R.id.freeFire);
        LinearLayout pubgMobile = findViewById(R.id.pubgMobile);
        LinearLayout pulsaSms = findViewById(R.id.pulsaSms);
        LinearLayout pulsaTransfer = findViewById(R.id.pulsaTransfer);
        LinearLayout itunesGiftCard = findViewById(R.id.itunesGiftCard);
        LinearLayout voucherGame = findViewById(R.id.voucherGame);
        LinearLayout eToll = findViewById(R.id.eToll);
        LinearLayout eMoney = findViewById(R.id.eMoney);

        //Pembayaran
        LinearLayout keretaApi = findViewById(R.id.keretaApi);
        LinearLayout asuransi = findViewById(R.id.asuransi);
        LinearLayout tv = findViewById(R.id.tv);
        LinearLayout teleponKabel = findViewById(R.id.teleponKabel);
        LinearLayout zakat = findViewById(R.id.zakat);
        LinearLayout multifinance = findViewById(R.id.multifinance);

        backBtn.setOnClickListener(v -> finish());

        //Pembelian
        tokenListrik.setOnClickListener(v -> {
            final String titleMenu = "Tagihan Listrik";
            final String categoryId = "38";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembayaranActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);
        });

        vocGplay.setOnClickListener(v -> {
            toastIcon("Under Maintenance !", R.drawable.ic_close, android.R.color.holo_red_dark);
            /*final String titleMenu = "Voucher Google Play";
            final String categoryId = "4";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembelianActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);*/
        });

        freeFire.setOnClickListener(v -> {
            toastIcon("Under Maintenance !", R.drawable.ic_close, android.R.color.holo_red_dark);
            /*final String titleMenu = "Diamond Free Fire";
            final String categoryId = "22";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembelianActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);*/
        });

        pubgMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastIcon("Under Maintenance !", R.drawable.ic_close, android.R.color.holo_red_dark);
                /*final String titleMenu = "PUBG Mobile UC";
                final String categoryId = "12";
                Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembelianActivity.class);
                intent.putExtra("titleMenu", titleMenu);
                intent.putExtra("categoryId", categoryId);
                startActivity(intent);*/
            }
        });

        pulsaSms.setOnClickListener(v -> {
            final String titleMenu = "Pulsa SMS Telephone";
            final String categoryId = "5";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembelianActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);
        });

        pulsaTransfer.setOnClickListener(v -> {
            final String titleMenu = "Pulsa Transfer";
            final String categoryId = "6";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembelianActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);
        });

        itunesGiftCard.setOnClickListener(v -> {
            toastIcon("Under Maintenance !", R.drawable.ic_close, android.R.color.holo_red_dark);
            /*final String titleMenu = "iTunes Gift Card";
            final String categoryId = "7";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembelianActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);*/
        });

        voucherGame.setOnClickListener(v -> {
            toastIcon("Under Maintenance !", R.drawable.ic_close, android.R.color.holo_red_dark);
            /*final String titleMenu = "Voucher Game";
            final String categoryId = "11";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembelianActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);*/
        });

        eToll.setOnClickListener(v -> {
            toastIcon("Under Maintenance !", R.drawable.ic_close, android.R.color.holo_red_dark);
            /*final String titleMenu = "E-Toll";
            final String categoryId = "20";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembelianActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);*/
        });

        eMoney.setOnClickListener(v -> {
            final String titleMenu = "E-Money";
            final String categoryId = "25";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembelianActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);
        });
        //------End Pembelian------

        //Pembayaran
        keretaApi.setOnClickListener(v -> {
            final String titleMenu = "Pembayaran Kereta Api";
            final String categoryId = "40";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembayaranActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);
        });

        asuransi.setOnClickListener(v -> {
            final String titleMenu = "Pembayaran Asuransi";
            final String categoryId = "41";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembayaranActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);
        });

        tv.setOnClickListener(v -> {
            final String titleMenu = "Pembayaran TV";
            final String categoryId = "42";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembayaranActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);
        });

        teleponKabel.setOnClickListener(v -> {
            final String titleMenu = "Pembayaran Telepon Kabel";
            final String categoryId = "44";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembayaranActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);
        });

        zakat.setOnClickListener(v -> {
            final String titleMenu = "Pembayaran Zakat";
            final String categoryId = "46";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembayaranActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);
        });

        multifinance.setOnClickListener(v -> {
            final String titleMenu = "Pembayaran Multifinance";
            final String categoryId = "47";
            Intent intent = new Intent(AllMenuPpobActivity.this, TransaksiPembayaranActivity.class);
            intent.putExtra("titleMenu", titleMenu);
            intent.putExtra("categoryId", categoryId);
            startActivity(intent);
        });

        //------End Pembayaran------
    }

    private void toastIcon(final String message, final int image, final int color) {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        @SuppressLint("InflateParams") View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(image);
        ((ImageView) custom_view.findViewById(R.id.icon)).setColorFilter(android.R.color.white);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(color));

        toast.setView(custom_view);
        toast.show();
    }
}