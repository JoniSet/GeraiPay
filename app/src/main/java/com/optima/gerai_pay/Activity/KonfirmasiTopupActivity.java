package com.optima.gerai_pay.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.optima.gerai_pay.Helper.Loading;
import com.optima.gerai_pay.Helper.SessionManager;
import com.optima.gerai_pay.Helper.Utils;
import com.optima.gerai_pay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class KonfirmasiTopupActivity extends AppCompatActivity {

    private TextView txt_no_rek, txt_nama_penerima, txt_nominal, txt_notice;
    private LinearLayout L_konfirmasi;
    private EditText edt_nama, edt_norek, edt_bank, edt_tgl, edt_catatan;
    private ImageView img_image, backBtn, img_copy, img_bank, img_copy_bayar;
    private Button btn_lanjut;
    private CardView C_note;
    private int status          = 0;
    private String nominal, total_deposit, nama, norek, nama_bank, tgl, catatan;
    private String encodedImage     = "";
    private Bitmap bitmap;

    SpannableString ss;

    Calendar calendar           = Calendar.getInstance();
    Loading loading             = new Loading();
    Dialog dialog;

    private static final int CAMERA_REQUEST                 = 1888;
    private static final int GALERI_REQUEST                 = 1999;
    private static final int MY_CAMERA_PERMISSION_CODE      = 100;
    private static final int MY_EXTERNAL_PERMISSION_CODE    = 101;

    SessionManager sessionManager;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_topup);

        Intent intent           = getIntent();
        nominal                 = intent.getStringExtra("nominal");

        init_view();
    }

    private void init_view() {
        sessionManager          = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        token                   = user.get(SessionManager.TOKEN);

        Log.d("token", token);

        dialog                  = loading.dialog_helper(KonfirmasiTopupActivity.this);
        txt_no_rek              = findViewById(R.id.txt_no_rek);
        txt_nama_penerima       = findViewById(R.id.txt_nama_penerima);
        txt_nominal             = findViewById(R.id.txt_nominal);
        txt_notice              = findViewById(R.id.txt_notice);

        edt_nama                = findViewById(R.id.edt_nama);
        edt_norek               = findViewById(R.id.edt_norek);
        edt_bank                = findViewById(R.id.edt_bank);
        edt_tgl                 = findViewById(R.id.edt_tgl);
        edt_catatan             = findViewById(R.id.edt_catatan);

        img_image               = findViewById(R.id.img_image);
        img_copy                = findViewById(R.id.img_copy);
        backBtn                 = findViewById(R.id.backBtn);
        img_bank                = findViewById(R.id.img_bank);
        img_copy_bayar          = findViewById(R.id.img_copy_bayar);

        btn_lanjut              = findViewById(R.id.btn_lanjut);

        L_konfirmasi            = findViewById(R.id.L_konfirmasi);
        C_note                  = findViewById(R.id.C_note);

        DatePickerDialog.OnDateSetListener date     = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                pilih_tanggal();
            }

        };
        edt_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(KonfirmasiTopupActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        img_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog_add_bukti();
            }
        });


        img_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text         = txt_no_rek.getText().toString();
                ClipData myClip     = ClipData.newPlainText("text", text);
                ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(KonfirmasiTopupActivity.this, "No rekening berhasil di Copy!", Toast.LENGTH_SHORT).show();
            }
        });


        img_copy_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text         = txt_nominal.getText().toString().substring(3, txt_nominal.getText().toString().length());
                ClipData myClip     = ClipData.newPlainText("text", text.replace(",", ""));
                ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(KonfirmasiTopupActivity.this, "Jumlah pembayaran berhasil di Copy!", Toast.LENGTH_SHORT).show();
            }
        });


        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                nama            = edt_nama.getText().toString();
//                norek           = edt_norek.getText().toString();
//                nama_bank       = edt_bank.getText().toString();
//                tgl             = edt_tgl.getText().toString();
//                catatan         = edt_catatan.getText().toString();
//
//                if (status == 0){
//                    L_konfirmasi.setVisibility(View.VISIBLE);
//                    btn_lanjut.setText("Lanjut");
//                    status = 1;
//                }
//                else{
//                    if (nama.isEmpty() || norek.isEmpty() || nama_bank.isEmpty() || tgl.isEmpty()){
//                        Toast.makeText(KonfirmasiTopupActivity.this, "Mohon isi semua field!", Toast.LENGTH_SHORT).show();
//                    }
//                    else if (encodedImage.equals("")){
//                        Toast.makeText(KonfirmasiTopupActivity.this, "Mohon upload bukti transfer!", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        L_konfirmasi.setVisibility(View.GONE);
//                        btn_lanjut.setText("Konfirmasi");
//                        status = 0;
//                        Toast.makeText(KonfirmasiTopupActivity.this, encodedImage, Toast.LENGTH_SHORT).show();
//                    }
//
//                }

                Intent intent = new Intent(KonfirmasiTopupActivity.this, TopupProsesActivity.class);
                startActivity(intent);

            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txt_notice.startAnimation(anim);

        getBank();
    }

    private void getBank() {
        dialog.show();
        AndroidNetworking.get(Utils.BASE_URL + "bank_moota")
                .addHeaders("Authorization", "Bearer " + token)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_kode = response.getString("response");
                            String message  = response.getString("message");
                            if (res_kode.equals("200")){
                                txt_no_rek.setText(response.getString("rekening"));
                                txt_nama_penerima.setText(response.getString("atas_nama"));
                                depositMoota();

                            }
                            else {
                                dialog.dismiss();
                                toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            toastIcon("Gagal memuat informasi bank\n" + e.getMessage(), R.drawable.ic_close, android.R.color.holo_red_dark);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        toastIcon("Gagal memuat informasi bank\n" + anError.getMessage(), R.drawable.ic_close, android.R.color.holo_red_dark);
                    }
                });
    }

    private void depositMoota() {
        AndroidNetworking.post(Utils.BASE_URL + "deposit_moota")
                .addHeaders("Authorization", "Bearer " + token)
                .addBodyParameter("topup", nominal.replace(",", ""))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        try {
                            String res_kode = response.getString("response");
                            String message  = response.getString("message");
                            if (res_kode.equals("200")){
                                total_deposit           = response.getString("total_deposit");

                                Long longval            = Long.parseLong(total_deposit);

                                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                                formatter.applyPattern("#,###,###,###");
                                String jumlah           = "Rp " + formatter.format(longval);

                                ss                      = new SpannableString(jumlah);
                                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.GRAY);
                                ss.setSpan(fcs, 0, jumlah.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                txt_nominal.setText(ss);
                            }
                            else {
                                toastIcon(message, R.drawable.ic_close, android.R.color.holo_red_dark);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            toastIcon("Gagal memuat informasi bank\n" + e.getMessage(), R.drawable.ic_close, android.R.color.holo_red_dark);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        toastIcon("Gagal memuat informasi bank\n" + anError.getMessage(), R.drawable.ic_close, android.R.color.holo_red_dark);
                    }
                });
    }

    //========================= mengambil image bukti TF============================================

    private void show_dialog_add_bukti() {
        final Dialog dialog                           = new Dialog(KonfirmasiTopupActivity.this);
        dialog.setContentView(R.layout.dialog_ambil_gambar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        LinearLayout L_batal            = dialog.findViewById(R.id.L_batal);
        ImageView img_camera            = dialog.findViewById(R.id.img_camera);
        ImageView img_galeri            = dialog.findViewById(R.id.img_galeri);

        L_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    dialog.dismiss();
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    dialog.dismiss();
                }
            }
        });

        img_galeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(KonfirmasiTopupActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, MY_EXTERNAL_PERMISSION_CODE);
                    dialog.dismiss();
                }
                else
                {
                    Intent image_picker     = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(image_picker, GALERI_REQUEST);
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == MY_EXTERNAL_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Read external storage permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, GALERI_REQUEST);
            }
            else
            {
                Toast.makeText(this, "Read external storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img_image.setImageBitmap(photo);

            ByteArrayOutputStream stream        = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageBytes                   = stream.toByteArray();
            encodedImage                        = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        else if (requestCode == GALERI_REQUEST && resultCode == Activity.RESULT_OK){
            Uri selectedImageUri        = data.getData();
            String selectedImagePath    = getPath(selectedImageUri);
            img_image.setImageURI(selectedImageUri);

            try {
                InputStream inputStream             = getContentResolver().openInputStream(selectedImageUri);
                Bitmap photo                        = BitmapFactory.decodeStream(inputStream);

                ByteArrayOutputStream stream        = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageBytes                   = stream.toByteArray();
                encodedImage                        = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPath(Uri uri)
    {
        String[] projection={MediaStore.Images.Media.DATA};
        Cursor cursor=managedQuery(uri,projection,null,null,null);
        int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    //==============================================================================================

    private void pilih_tanggal() {
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        edt_tgl.setText(sdf.format(calendar.getTime()));
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