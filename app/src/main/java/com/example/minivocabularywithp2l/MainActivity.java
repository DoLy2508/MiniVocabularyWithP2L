package com.example.minivocabularywithp2l;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.LuyenPA.LuyenPhatAmActivity;
import com.example.NhiemVu.NhiemVuActivity;
import com.example.chuDe.ChuDeActivity;
import com.example.chuDe.FlashCardActivity;
import com.example.gheptu.GhepTuActivity;

//Cac trang activ se lien ket voi Mainactivity (trang chu) bang intent o day
public class MainActivity extends AppCompatActivity {
    private Button btnGame, btnNhiemVu, btnAnimail, btnFood, btnTravel;
    private ImageButton imbtnHome;
    private ImageButton imbtnHoc;
    private ImageButton imbtnCheckList;
    private ImageButton imbtnGame;
    private TextView tvluyenphatam;
    private boolean isAdmin;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        email = getIntent().getStringExtra("email");
        // ánh xạ view (b1)
        btnGame = findViewById(R.id.btnGame);
        btnNhiemVu = findViewById(R.id.btnNhiemVu);
        btnAnimail = findViewById(R.id.btnAnimail);
        btnFood = findViewById(R.id.btnFood);
        btnTravel = findViewById(R.id.btnTravel);
        imbtnHome = findViewById(R.id.imbtnHome);
        imbtnGame = findViewById(R.id.imbtnGame);
        imbtnHoc = findViewById(R.id.imbtnHoc);
        tvluyenphatam = findViewById(R.id.tvluyenphatam);




        // Lien ket trang Phat am
        tvluyenphatam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLuyenPA = new Intent(MainActivity.this, LuyenPhatAmActivity.class);
                // Đính kèm thông tin quyền admin vào Intent
                intentLuyenPA.putExtra("isAdmin", isAdmin);
                startActivity(intentLuyenPA);
            }
        });
        // =================================================================

        // Lien ket trang chu de tu vung
        imbtnHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentChuDe = new Intent(MainActivity.this, ChuDeActivity.class);
                startActivity(intentChuDe);
            }
        });

        // Liên ket trang nhiem vu hoc tap
        btnNhiemVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNhiemVu = new Intent(MainActivity.this, NhiemVuActivity.class);
                startActivity(intentNhiemVu);
            }
        });

        // Lien ket trang hoc flatcat
        btnAnimail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAnimal = new Intent(MainActivity.this, FlashCardActivity.class);
                startActivity(intentAnimal);
            }
        });
        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFood = new Intent(MainActivity.this, FlashCardActivity.class);
                startActivity(intentFood);
            }
        });
        btnTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTravel = new Intent(MainActivity.this, FlashCardActivity.class);
                startActivity(intentTravel);
            }
        });

        // trò chơi ghép từ
        View.OnClickListener gameClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intentGhepTu = new Intent(MainActivity.this, GhepTuActivity.class);
                intentGhepTu.putExtra("isAdmin", isAdmin);
                startActivity(intentGhepTu);
            }
        };
        btnGame.setOnClickListener(gameClickListener);
        imbtnGame.setOnClickListener(gameClickListener);




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }


}

