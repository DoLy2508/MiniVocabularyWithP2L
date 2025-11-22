package com.example.chuDe;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minivocabularywithp2l.R;


public class ChuDeActivity extends AppCompatActivity {
    private TextView tvMayBay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_tiendo);
        tvMayBay = findViewById(R.id.tvMayBay);


        tvMayBay.setOnClickListener(v -> {
            startActivity(new Intent(ChuDeActivity.this, FlashCardActivity.class));
            finish();
        });


    }
}
