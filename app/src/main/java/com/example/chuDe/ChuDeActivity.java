package com.example.chuDe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.LuyenPA.LuyenPhatAmActivity;
import com.example.NhiemVu.NhiemVuActivity;
import com.example.gheptu.Database.SQLiteConnect;
import com.example.gheptu.GhepTuActivity;
import com.example.minivocabularywithp2l.MainActivity;
import com.example.minivocabularywithp2l.R;


public class ChuDeActivity extends AppCompatActivity {

    private LinearLayout layoutAnimals, layoutFood, layoutTravel, layoutJobs;
    private ImageButton btnBack;







    private ImageButton imbtnHome, imbtnHoc, imbtnCheckList, imbtnGame;

    private SQLiteConnect db;
    private boolean isAdmin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_tiendo);


        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        // Ánh xạ view
        layoutAnimals = findViewById(R.id.layout_animals);
        layoutFood = findViewById(R.id.layout_food);
        layoutTravel = findViewById(R.id.layout_travel);
        layoutJobs = findViewById(R.id.layout_jobs);
        btnBack = findViewById(R.id.btn_back);


        imbtnHome = findViewById(R.id.imbtnHome);
        imbtnHoc = findViewById(R.id.imbtnHoc);
        imbtnCheckList = findViewById(R.id.imbtnCheckList);
        imbtnGame = findViewById(R.id.imbtnGame);


        btnBack.setOnClickListener(v -> finish());


        layoutAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChuDeActivity.this, FlashCardActivity.class);
                intent.putExtra("isAdmin", isAdmin);
                startActivity(intent);
                finish();
            }
        });

        layoutFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentAnimal = new Intent(ChuDeActivity.this, FlashCardActivity.class);
                        intentAnimal.putExtra("isAdmin", isAdmin);
                        startActivity(intentAnimal);
                        finish();

                    }
                });
        layoutTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChuDeActivity.this, FlashCardActivity.class);
                intent.putExtra("isAdmin", isAdmin);
                startActivity(intent);
                finish();
            }
        });
        layoutJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChuDeActivity.this, FlashCardActivity.class);
                intent.putExtra("isAdmin", isAdmin);
                startActivity(intent);
                finish();
            }
        });



        setupBottomMenu();
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

}
