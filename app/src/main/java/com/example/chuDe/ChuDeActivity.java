package com.example.chuDe;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.NhiemVu.NhiemVuActivity;
import com.example.gheptu.Database.SQLiteConnect;
import com.example.gheptu.GhepTuActivity;
import com.example.minivocabularywithp2l.MainActivity;
import com.example.minivocabularywithp2l.R;


public class ChuDeActivity extends AppCompatActivity {
    // Khai báo các khối layout
    private LinearLayout layoutAnimals, layoutFood, layoutTravel, layoutJobs;
    private ImageButton btnBack;

    // Khai báo các view hiển thị phần trăm
    private TextView tvPercentAnimals, tvPercentFood, tvPercentTravel, tvPercentJobs;
    private ImageView imgAnimals, imgFood, imgTravel, imgJobs;
    private ProgressBar pbAnimals, pbFood, pbTravel, pbJobs;

    // Tổng quan
    private ProgressBar progressBarTotal;
    private TextView tvProgressPercentTotal;

    // Menu dưới
    private ImageButton imbtnHome, imbtnHoc, imbtnCheckList, imbtnGame;

    private SQLiteConnect db;
    private boolean isAdmin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_tiendo);

        db = new SQLiteConnect(this);
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        // Ánh xạ view
        layoutAnimals = findViewById(R.id.layout_animals);
        layoutFood = findViewById(R.id.layout_food);
        layoutTravel = findViewById(R.id.layout_travel);
        layoutJobs = findViewById(R.id.layout_jobs);
        btnBack = findViewById(R.id.btn_back);

        tvPercentAnimals = findViewById(R.id.tv_percent_animals);
        tvPercentFood = findViewById(R.id.tv_percent_food);
        tvPercentTravel = findViewById(R.id.tv_percent_travel);
        tvPercentJobs = findViewById(R.id.tv_percent_jobs);

        imgAnimals = findViewById(R.id.img_animals);
        imgFood = findViewById(R.id.img_food);
        imgTravel = findViewById(R.id.img_travel);
        imgJobs = findViewById(R.id.img_jobs);

        pbAnimals = findViewById(R.id.pb_animals);
        pbFood = findViewById(R.id.pb_food);
        pbTravel = findViewById(R.id.pb_travel);
        pbJobs = findViewById(R.id.pb_jobs);

        progressBarTotal = findViewById(R.id.progress_bar_total);
        tvProgressPercentTotal = findViewById(R.id.tv_progress_percent);

        // Ánh xạ Menu dưới
        imbtnHome = findViewById(R.id.imbtnHome);
        imbtnHoc = findViewById(R.id.imbtnHoc);
        imbtnCheckList = findViewById(R.id.imbtnCheckList);
        imbtnGame = findViewById(R.id.imbtnGame);

        // Xử lý sự kiện nút back
        btnBack.setOnClickListener(v -> finish());

        // Xử lý sự kiện click cho từng chủ đề
        layoutAnimals.setOnClickListener(v -> openFlashCard("Animals"));
        layoutFood.setOnClickListener(v -> openFlashCard("Food"));
        layoutTravel.setOnClickListener(v -> openFlashCard("Travel"));
        layoutJobs.setOnClickListener(v -> openFlashCard("Jobs"));

        // Xử lý sự kiện Menu dưới
        setupBottomMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật tiến độ mỗi khi quay lại màn hình này
        if (db != null) {
            updateProgress();
        }
    }

    private void setupBottomMenu() {
        imbtnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ChuDeActivity.this, MainActivity.class);
            intent.putExtra("isAdmin", this.isAdmin);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();

        });

        imbtnHoc.setOnClickListener(v -> {
            // Đang ở trang học (Chủ đề), có thể reload hoặc thông báo
            Toast.makeText(ChuDeActivity.this, "Bạn đang ở trang Học tập", Toast.LENGTH_SHORT).show();
        });

        imbtnCheckList.setOnClickListener(v -> {
            Intent intent = new Intent(ChuDeActivity.this, NhiemVuActivity.class);
            intent.putExtra("isAdmin", this.isAdmin);
            startActivity(intent);
        });

        imbtnGame.setOnClickListener(v -> {
            Intent intent = new Intent(ChuDeActivity.this, GhepTuActivity.class);

            intent.putExtra("isAdmin", this.isAdmin);
            startActivity(intent);
        });
    }

    private void updateProgress() {
        // 1. Tính phần trăm cho từng chủ đề
        int pAnimals = calculatePercent("Animals");
        int pFood = calculatePercent("Food");
        int pTravel = calculatePercent("Travel");
        int pJobs = calculatePercent("Jobs");

        // 2. Cập nhật giao diện từng dòng
        updateRowUI(tvPercentAnimals, imgAnimals, pbAnimals, pAnimals);
        updateRowUI(tvPercentFood, imgFood, pbFood, pFood);
        updateRowUI(tvPercentTravel, imgTravel, pbTravel, pTravel);
        updateRowUI(tvPercentJobs, imgJobs, pbJobs, pJobs);

        // 3. Tính tổng tiến độ chung
        int totalLearned = db.getLearnedCountByTopic("Animals") +
                db.getLearnedCountByTopic("Food") +
                db.getLearnedCountByTopic("Travel") +
                db.getLearnedCountByTopic("Jobs");

        int totalWords = db.getTotalCountByTopic("Animals") +
                db.getTotalCountByTopic("Food") +
                db.getTotalCountByTopic("Travel") +
                db.getTotalCountByTopic("Jobs");

        int totalPercent = 0;
        if (totalWords > 0) {
            totalPercent = (int) ((totalLearned / (float) totalWords) * 100);
        }

        progressBarTotal.setProgress(totalPercent);
        tvProgressPercentTotal.setText(totalPercent + "%");

        // Màu cho vòng tròn tổng
        setProgressBarColor(progressBarTotal, tvProgressPercentTotal, totalPercent);
    }

    private int calculatePercent(String topic) {
        int total = db.getTotalCountByTopic(topic);
        if (total == 0) return 0;
        int learned = db.getLearnedCountByTopic(topic);
        return (int) ((learned / (float) total) * 100);
    }

    private void updateRowUI(TextView tvPercent, ImageView imgIcon, ProgressBar pb, int percent) {
        tvPercent.setText(percent + "%");
        pb.setProgress(percent);
        
        // Cập nhật màu sắc cho ProgressBar nhỏ và text, icon
        int color = getColorForPercent(percent);
        
        tvPercent.setTextColor(color);
        imgIcon.setColorFilter(color);
        
        // Set màu cho ProgressBar
        Drawable progressDrawable = pb.getProgressDrawable().mutate();
        // Layer thứ 2 trong layer-list thường là progress (hoặc dùng tint)
        progressDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        pb.setProgressDrawable(progressDrawable);
    }
    
    private void setProgressBarColor(ProgressBar pb, TextView tvPercent, int percent) {
        int color = getColorForPercent(percent);
        tvPercent.setTextColor(color);
        
        Drawable progressDrawable = pb.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        pb.setProgressDrawable(progressDrawable);
    }

    private int getColorForPercent(int percent) {
        if (percent <= 30) {
            return Color.parseColor("#FF0000"); // Đỏ
        } else if (percent <= 50) {
            return Color.parseColor("#FFD700"); // Vàng
        } else if (percent <= 70) {
            return Color.parseColor("#00008B"); // Xanh lam đậm
        } else {
            return Color.parseColor("#008000"); // Xanh lá (từ 71 đến 100)
        }
    }

    // Hàm dùng chung để chuyển activity
    private void openFlashCard(String topicName) {
        Intent intent = new Intent(ChuDeActivity.this, FlashCardActivity.class);
        intent.putExtra("TOPIC_NAME", topicName); // Gửi tên chủ đề sang FlashCardActivity
        intent.putExtra("isAdmin", this.isAdmin);
        startActivity(intent);
    }
}
