package com.example.gheptu;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.NhiemVu.NhiemVuActivity;
import com.example.gheptu.Database.SQLiteConnect;
import com.example.gheptu.Model.TuVungGhepTu;

import com.example.minivocabularywithp2l.MainActivity;
import com.example.minivocabularywithp2l.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;



// activ cua trang ghep tu
public class GhepTuActivity extends AppCompatActivity {
    private TextView tvTimer;
    private GridLayout gridLayout;
    private HashMap<String, String> pairs;
    private List<String> allWords;
    List<TuVungGhepTu> listPairs = new ArrayList<>();
    private ImageButton imbtnQuanLiTu;
    private Button btnHoanThanh;
    SQLiteConnect sqLiteConnect;








    private MaterialCardView firstCard = null;
    private String firstValue = "";
    private int timeElapsed = 0;

    private Handler handler = new Handler();
    private Runnable runnable;
    private int matchedPairs = 0;
    private int totalPairsOnScreen = 0;
    private ImageButton imbtnHome;
    private ImageButton imbtnHoc;
    private ImageButton imbtnCheckList;
    private ImageButton imbtnGame;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_gheptu);
        boolean isAdmin = getIntent().getBooleanExtra("isAdmin", false);



        sqLiteConnect = new SQLiteConnect(GhepTuActivity.this, getString(R.string.db_name),
                null,
                SQLiteConnect.DATABASE_VERSION
        );
        imbtnQuanLiTu = findViewById(R.id.imbtnQuanLiTu);
        gridLayout = findViewById(R.id.gridLayout);
        tvTimer = findViewById(R.id.tvTimer);
        btnHoanThanh = findViewById(R.id.btnHoanThanh);
        imbtnHoc = findViewById(R.id.imbtnHoc);
        imbtnCheckList = findViewById(R.id.imbtnCheckList);
        imbtnGame = findViewById(R.id.imbtnGame);
        imbtnHome = findViewById(R.id.imbtnHome);

        // Ẩn nút quản lý từ nếu không phải admin
        if (!isAdmin) {
            imbtnQuanLiTu.setVisibility(View.GONE);
        }

        imbtnQuanLiTu.setOnClickListener(v -> {
            startActivity(new Intent(GhepTuActivity.this, QuanLiTuActivity.class));
        });

        imbtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTrangChu = new Intent(GhepTuActivity.this, MainActivity.class);
                intentTrangChu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentTrangChu.putExtra("isAdmin", isAdmin);

                startActivity(intentTrangChu);
            }


        });
        imbtnCheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentNhiemVu = new Intent(GhepTuActivity.this, NhiemVuActivity.class);
                intentNhiemVu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // (Tùy chọn) Nếu NhiemVuActivity cũng cần quyền admin, hãy truyền nó đi
                //intentNhiemVu.putExtra("isAdmin", isAdmin);

                startActivity(intentNhiemVu);
            }
        });



        btnHoanThanh.setOnClickListener(v -> xuLyHoanThanh());

        resetGame();


    }
    private void resetGame() {
        handler.removeCallbacks(runnable);
        timeElapsed = 0;
        matchedPairs = 0;
        firstCard = null;
        firstValue = "";

        setupDuLieu();
        setupKhung();
        batDauThoiGian();
    }
    private void xuLyHoanThanh() {
        boolean isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        if (!isAdmin) {
            imbtnQuanLiTu.setVisibility(View.GONE);
        }
        if (matchedPairs == totalPairsOnScreen) {
            Intent intent = new Intent(GhepTuActivity.this, VictoryActivity.class);
            intent.putExtra("isAdmin", isAdmin);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Bạn chưa chơi ghép từ xong!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //resetGame();
    }

    private void setupDuLieu() {
        pairs = new HashMap<>();
        allWords = new ArrayList<>();



        listPairs.clear();


        Cursor cursor = sqLiteConnect.getData("SELECT * FROM tuvungGT_v2 ORDER BY RANDOM() LIMIT 6");
        while (cursor.moveToNext()) {
            int key = cursor.getInt(0);
            String maTu = cursor.getString(1);
            String en = cursor.getString(2);
            String vi = cursor.getString(3);

            listPairs.add(new TuVungGhepTu(key, maTu, en, vi));
            pairs.put(en, vi);
        }
        cursor.close();

        totalPairsOnScreen = listPairs.size();
        Collections.shuffle(listPairs);


        for (TuVungGhepTu item : listPairs) {
            if (Math.random() < 0.5) {
                allWords.add(item.getTiengAnh());
                allWords.add(item.getTiengViet());
            } else {
                allWords.add(item.getTiengViet());
                allWords.add(item.getTiengAnh());
            }
        }
    }
    private void setupKhung() {
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(3); // 3 cột

        for (String word : allWords) {
            MaterialCardView card = (MaterialCardView)
                    getLayoutInflater().inflate(R.layout.item_card, gridLayout, false);

            TextView txtWord = card.findViewById(R.id.txtWord);
            txtWord.setText(word);

            card.setOnClickListener(v -> handleCardClick(card, word));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            card.setLayoutParams(params);



            gridLayout.addView(card);
        }
    }

    private void handleCardClick(MaterialCardView card, String word) {
        if (firstCard == null) {
            firstCard = card;
            firstValue = word;
            setVienThe(card, Color.parseColor("#FF9800"));
            return;
        }

        if (card == firstCard) {
            clearVienThe(card);
            firstCard = null;
            return;
        }

        setVienThe(card, Color.parseColor("#FF9800"));

        boolean ok = checkMatch(firstValue, word);

        if (ok) {
            setVienThe(firstCard, Color.GREEN);
            setVienThe(card, Color.GREEN);

            matchedPairs++;


            handler.postDelayed(() -> {
                firstCard.setVisibility(View.INVISIBLE);
                card.setVisibility(View.INVISIBLE);
                firstCard = null;
            }, 350);

        } else {
            setVienThe(firstCard, Color.RED);
            setVienThe(card, Color.RED);

            handler.postDelayed(() -> {
                clearVienThe(firstCard);
                clearVienThe(card);
                firstCard = null;
            }, 600);
        }
    }
    private boolean checkMatch(String w1, String w2) {
        return (pairs.containsKey(w1) && pairs.get(w1).equals(w2))
                || (pairs.containsKey(w2) && pairs.get(w2).equals(w1));
    }
    private void setVienThe(MaterialCardView card, int color) {
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(8f);
        card.setStrokeColor(color);
        card.setStrokeWidth(6);
    }
    private void clearVienThe(MaterialCardView card) {
        card.setStrokeColor(Color.TRANSPARENT);
        card.setStrokeWidth(0);
    }

    private void batDauThoiGian() {
        runnable = new Runnable() {
            @Override
            public void run() {

                int minutes = timeElapsed / 60;
                int seconds = timeElapsed % 60;
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));

                timeElapsed++;

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable); // bắt đầu
    }





}
