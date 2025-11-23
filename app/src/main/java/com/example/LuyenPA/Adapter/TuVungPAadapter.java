package com.example.LuyenPA.Adapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.LuyenPA.Model.LuyenPhatAm;
import com.example.minivocabularywithp2l.R;

import java.util.ArrayList;

public class TuVungPAadapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<LuyenPhatAm> listTuPA, listTuBackup, listTuFilter;
    MediaPlayer mediaPlayer;

    public TuVungPAadapter(Activity context, int resource, ArrayList<LuyenPhatAm> listTuPA) {

        super(context, resource, listTuPA);
        this.context = context;
        this.resource = resource;
        this.listTuPA = this.listTuBackup = listTuPA;
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
        TextView tvTenAudio = customView.findViewById(R.id.tvTenAudio);

        ImageView imvPhatAm = customView.findViewById(R.id.imvPhatAm);
        ImageView imvSua = customView.findViewById(R.id.imvSua);
        ImageView imvXoa = customView.findViewById(R.id.imvXoa);

        LuyenPhatAm tuPA = listTuPA.get(position);

        tvMaTu.setText(tuPA.getMaTu());
        tvTiengAnh.setText(tuPA.getTiengAnh());
        tvNguAm.setText(tuPA.getNguAm());
        tvTiengViet.setText(tuPA.getTiengViet());
        tvTenAudio.setText(tuPA.getTenAudio());




        imvPhatAm.setOnClickListener(v -> {

            String tenAudio = tuPA.getTenAudio().trim();



            int audioId = context.getResources().getIdentifier(
                    tenAudio,
                    "raw",
                    context.getPackageName()
            );


            if (audioId == 0) {
                Toast.makeText(context, "Không tìm thấy audio: " + tenAudio,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Nếu đang phát thì dừng & giải phóng trước
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
            }

            // Dừng và giải phóng MediaPlayer cũ nếu đang phát
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            // Tạo MediaPlayer mới và phát audio
            mediaPlayer = MediaPlayer.create(context, audioId);
            mediaPlayer.start();

            // Release khi audio kết thúc
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        });









        return customView;


    }
}
