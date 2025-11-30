package com.example.minivocabularywithp2l;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.example.setting.SettingActivity;

//Cac trang activ se lien ket voi Mainactivity (trang chu) bang intent o day
public class MainActivity extends AppCompatActivity {
    private Button btnGame, btnNhiemVu, btnAnimail, btnFood, btnTravel;
    private ImageButton imbtnHome;
    private ImageButton imbtnHoc;
    private ImageButton imbtnCheckList;
    private ImageButton imbtnGame;
    private ImageButton btnSetting;
    private TextView tvluyenphatam;

    private LinearLayout layoutTiendo, layoutLuyenPhatAm;
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

        imbtnCheckList = findViewById(R.id.imbtnCheckList);
        btnSetting = findViewById(R.id.btnSetting);
        tvluyenphatam = findViewById(R.id.tvluyenphatam);
        layoutTiendo = findViewById(R.id.layoutTiendo);
        layoutLuyenPhatAm = findViewById(R.id.layoutLuyenPhatAm);


        // Xử lý sự kiện nút Setting
        if (btnSetting != null) {
            btnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentSetting = new Intent(MainActivity.this, SettingActivity.class);
                    intentSetting.putExtra("email", email); // Truyền email sang Setting
                    startActivity(intentSetting);
                }
            });
        }

        // Lien ket trang Phat am
        layoutLuyenPhatAm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLuyenPA = new Intent(MainActivity.this, LuyenPhatAmActivity.class);
                // Đính kèm thông tin quyền admin vào Intent
                intentLuyenPA.putExtra("isAdmin", isAdmin);
                startActivity(intentLuyenPA);
            }
        });


        // =================================================================

        // Lien ket trang chu de tu vung va tien do
        View.OnClickListener ChuDeClickListener = v -> {
            Intent intentChuDe = new Intent(MainActivity.this, ChuDeActivity.class);
            // BỎ CỜ FLAG_ACTIVITY_CLEAR_TOP
            // intentNhiemVu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentChuDe.putExtra("isAdmin", isAdmin);
            startActivity(intentChuDe);
        };
        imbtnHoc.setOnClickListener(ChuDeClickListener);
        if (layoutTiendo != null) {
            layoutTiendo.setOnClickListener(ChuDeClickListener);
        }


        // Liên ket trang nhiem vu hoc tap

        View.OnClickListener nhiemVuClickListener = v -> {
            Intent intent = new Intent(MainActivity.this, NhiemVuActivity.class);
            // BỎ HOÀN TOÀN CỜ FLAG_ACTIVITY_CLEAR_TOP
            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("isAdmin", isAdmin);
            startActivity(intent);
        };
        btnNhiemVu.setOnClickListener(nhiemVuClickListener);


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




        // Lien ket trang hoc flatcat
        btnAnimail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAnimal = new Intent(MainActivity.this, FlashCardActivity.class);
                intentAnimal.putExtra("isAdmin", isAdmin);
                startActivity(intentAnimal);
            }
        });
        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFood = new Intent(MainActivity.this, FlashCardActivity.class);
                intentFood.putExtra("isAdmin", isAdmin);
                startActivity(intentFood);
            }
        });
        btnTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTravel = new Intent(MainActivity.this, FlashCardActivity.class);
                intentTravel.putExtra("isAdmin", isAdmin);
                startActivity(intentTravel);
            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }


}

