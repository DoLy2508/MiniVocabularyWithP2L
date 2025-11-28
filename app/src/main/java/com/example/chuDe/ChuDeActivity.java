package com.example.chuDe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minivocabularywithp2l.R;


public class ChuDeActivity extends AppCompatActivity {
    // Khai báo các khối layout
    private LinearLayout layoutAnimals, layoutFood, layoutTravel, layoutJobs;
    private ImageButton btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_tiendo);

        // Ánh xạ view
        layoutAnimals = findViewById(R.id.layout_animals);
        layoutFood = findViewById(R.id.layout_food);
        layoutTravel = findViewById(R.id.layout_travel);
        layoutJobs = findViewById(R.id.layout_jobs);
        btnBack = findViewById(R.id.btn_back);

        // Xử lý sự kiện nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity hiện tại để quay lại màn hình trước
            }
        });

        // Xử lý sự kiện click cho từng chủ đề -> Chuyển sang FlashCardActivity và gửi kèm dữ liệu chủ đề
        layoutAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFlashCard("Animals");
            }
        });

        layoutFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFlashCard("Food");
            }
        });

        layoutTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFlashCard("Travel");
            }
        });

        layoutJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFlashCard("Jobs");
            }
        });
    }

    // Hàm dùng chung để chuyển activity
    private void openFlashCard(String topicName) {
        Intent intent = new Intent(ChuDeActivity.this, FlashCardActivity.class);
        intent.putExtra("TOPIC_NAME", topicName); // Gửi tên chủ đề sang FlashCardActivity
        startActivity(intent);
    }
}
