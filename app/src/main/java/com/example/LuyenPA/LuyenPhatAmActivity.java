package com.example.LuyenPA;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.LuyenPA.Model.LuyenPhatAm;
import com.example.gheptu.Database.SQLiteConnect;
import com.example.minivocabularywithp2l.MainActivity;
import com.example.minivocabularywithp2l.R;

import java.util.ArrayList;


// activ cua trang luyen phat am

public class LuyenPhatAmActivity extends AppCompatActivity {
    private ImageButton imbtnQuanLiTuPA;
    private ArrayList<LuyenPhatAm> listTuPA;
    private TextView tvNguAm, tvNghiaTV, tvTiengAnh;
    private ImageView imvNghe;
    private Button btnContinue;
    private MediaPlayer mediaPlayer;
    private int currentIndex = 0;
    private ImageView imvTroVe;
    private SQLiteConnect db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_luyenphatam);
        imvTroVe = findViewById(R.id.imvTroVe);
        imbtnQuanLiTuPA = findViewById(R.id.imbtnQuanLiTuPA);
        btnContinue = findViewById(R.id.btnContinue);

        tvTiengAnh = findViewById(R.id.tvTiengAnh);
        tvNguAm = findViewById(R.id.tvNguAm);
        tvNghiaTV = findViewById(R.id.tvNghiaTV);
        imvNghe = findViewById(R.id.imvNghe);


        imvTroVe.setOnClickListener(v -> {
            Log.d("CLICK_TEST", "Click Tro Ve");
            startActivity(new Intent(LuyenPhatAmActivity.this, MainActivity.class));
            finish();


        });
        imbtnQuanLiTuPA.setOnClickListener(v -> {
            Log.d("CLICK_TEST", "Click QuanLiTuPA");
            Intent intentPA = new Intent(LuyenPhatAmActivity.this, QuanLiTuPaActivity.class);
            startActivity(intentPA);
        });


       db = new SQLiteConnect(this, getString(R.string.db_name), null, SQLiteConnect.DATABASE_VERSION);


        listTuPA = new ArrayList<>();

            Cursor cursor = db.getData("SELECT * FROM tuvungPAm");
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String maTu = cursor.getString(1);
                String tiengAnh = cursor.getString(2);
                String nguAm = cursor.getString(3);
                String tiengViet = cursor.getString(4);
                String tenAudio = cursor.getString(5);
                listTuPA.add(new LuyenPhatAm(id, maTu, tiengAnh, nguAm, tiengViet, tenAudio));
            }
            cursor.close();

            displayCurrentWord();
            imvNghe.setOnClickListener(v -> {
            LuyenPhatAm tu = listTuPA.get(currentIndex); // lấy từ hiện tại
            String tenAudio = tu.getTenAudio().trim();

            int audioId = getResources().getIdentifier(tenAudio, "raw", getPackageName());
            if (audioId != 0) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(this, audioId);
                mediaPlayer.start();

                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    mediaPlayer = null;
                });
            } else {
                Toast.makeText(this, "Không tìm thấy audio: " + tenAudio, Toast.LENGTH_SHORT).show();
            }
        });



            btnContinue.setOnClickListener(v -> {
                currentIndex++;
                if (currentIndex < listTuPA.size()) {
                    displayCurrentWord();
                } else {
                    Toast.makeText(this, "Đã hết từ vựng!", Toast.LENGTH_SHORT).show();
                }

        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (db != null) {
            db.close();
        }
    }
    private void displayCurrentWord() {
        if (currentIndex >= listTuPA.size()) {
            Toast.makeText(this, "Đã hết từ vựng!", Toast.LENGTH_SHORT).show();
            return;
        }

        LuyenPhatAm tu = listTuPA.get(currentIndex);
        tvTiengAnh.setText(tu.getTiengAnh());
        tvNguAm.setText("/" + tu.getNguAm() + "/"); // nếu bạn có phiên âm lưu trong DB thì dùng
        tvNghiaTV.setText(tu.getTiengViet());


    }
}