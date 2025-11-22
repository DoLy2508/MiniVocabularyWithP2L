package com.example.chuDe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gheptu.GhepTuActivity;
import com.example.minivocabularywithp2l.R;

public class FlashCardActivity extends AppCompatActivity {


    private Button btnGhepThe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chude_tuvung);
        btnGhepThe = findViewById(R.id.btnGhepThe);
        btnGhepThe.setOnClickListener(v -> {
            startActivity(new Intent(FlashCardActivity.this, GhepTuActivity.class));
            finish();

        });


    }
}
