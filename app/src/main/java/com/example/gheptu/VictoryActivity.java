package com.example.gheptu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minivocabularywithp2l.R;


// Activ cua trang ghep tu

public class VictoryActivity  extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        Button btnChoiLai = findViewById(R.id.btnChoiLai);
        btnChoiLai.setOnClickListener(v -> {
            Intent intent = new Intent(VictoryActivity.this, GhepTuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }


}
