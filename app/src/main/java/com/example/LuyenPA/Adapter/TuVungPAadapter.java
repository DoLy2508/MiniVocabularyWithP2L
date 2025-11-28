package com.example.LuyenPA.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import com.example.gheptu.Database.SQLiteConnect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
// Thêm import cần thiết
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.LuyenPA.Activities.SuaTuPaActivity;
import com.example.LuyenPA.Model.LuyenPhatAm;
import com.example.LuyenPA.QuanLiTuPaActivity;
import com.example.minivocabularywithp2l.R;

import java.util.ArrayList;

public class TuVungPAadapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<LuyenPhatAm> listTuPA, listTuBackup, listTuFilter;

    QuanLiTuPaActivity activity;
    private TextToSpeech tts; //Thêm biến TextToSpeech


    // THAY ĐỔI CONSTRUCTOR
    public TuVungPAadapter(QuanLiTuPaActivity context, int resource, ArrayList<LuyenPhatAm> listTuPA, TextToSpeech tts) {
        super(context, resource, listTuPA);
        this.context = context;
        this.resource = resource;
        this.listTuPA = this.listTuBackup = listTuPA;
        this.activity = context;
        this.tts = tts; //Nhận TTS từ Activity
    }

    public int getCount() {
        return listTuPA.size();
    }

    public ArrayList<LuyenPhatAm> getListTu() {
        return listTuPA;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View customView = inflater.inflate(resource, null);

        TextView tvMaTu = customView.findViewById(R.id.tvMaTu);
        TextView tvTiengAnh = customView.findViewById(R.id.tvTiengAnh);
        TextView tvNguAm = customView.findViewById(R.id.tvNguAm);
        TextView tvTiengViet = customView.findViewById(R.id.tvTiengViet);
        ImageView imvPhatAm = customView.findViewById(R.id.imvPhatAm);
        ImageView imvSua = customView.findViewById(R.id.imvSua);
        ImageView imvXoa = customView.findViewById(R.id.imvXoa);

        LuyenPhatAm tuPA = listTuPA.get(position);

        tvMaTu.setText(tuPA.getMaTu());
        tvTiengAnh.setText(tuPA.getTiengAnh());
        tvNguAm.setText(tuPA.getNguAm());
        tvTiengViet.setText(tuPA.getTiengViet());





        imvPhatAm.setOnClickListener(v -> {
            String wordToSpeak = tuPA.getTiengAnh();
            if (tts != null && wordToSpeak != null && !wordToSpeak.isEmpty()) {
                // Sử dụng TextToSpeech để phát âm
                tts.speak(wordToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(context, "Không có từ để phát âm", Toast.LENGTH_SHORT).show();
            }


        });


        imvXoa.setOnClickListener(v -> {
            // Hiển thị hộp thoại xác nhận
            AlertDialog.Builder builder = new AlertDialog.Builder(context); // Dùng context từ constructor
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc chắn muốn xóa từ: " + tuPA.getTiengAnh() + "?");


            // Nút "Có"
            builder.setPositiveButton("Có", (dialog, which) -> {
                SQLiteConnect sqLiteConnect = null;
                try {
                    // Câu lệnh DELETE, xóa dựa trên ID của từ
                    String query = "DELETE FROM tuvungPAm WHERE id = " + tuPA.getId();

                    // Khởi tạo và thực thi
                    sqLiteConnect = new SQLiteConnect(context, context.getString(R.string.db_name), null, SQLiteConnect.DATABASE_VERSION);
                    sqLiteConnect.queryData(query);

                    Toast.makeText(context, "Đã xóa: " + tuPA.getTiengAnh(), Toast.LENGTH_SHORT).show();

                    // Gọi phương thức loadDataTuVung() từ QuanLiTuPaActivity để cập nhật lại ListView
                    // Ép kiểu context về đúng QuanLiTuPaActivity
                    if (context instanceof QuanLiTuPaActivity) {
                        ((QuanLiTuPaActivity) context).loadDataTuVung();
                    }

                } catch (Exception e) {
                    Log.e("DELETE_ERROR", "Lỗi khi xóa từ: " + e.getMessage());
                    Toast.makeText(context, "Lỗi khi xóa dữ liệu", Toast.LENGTH_SHORT).show();
                } finally {
                    if (sqLiteConnect != null) {
                        sqLiteConnect.close();
                    }
                    dialog.dismiss();
                }
            });

            // Nút "Không"
            builder.setNegativeButton("Không", (dialog, which) -> {
                // Đóng hộp thoại
                dialog.cancel();
            });

            // Tạo và hiển thị hộp thoại
            builder.create().show();
        });
        return customView;


    }
}
