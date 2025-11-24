package com.example.minivocabularywithp2l;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.example.thongBao.ThongBaoActivity;

import java.util.Calendar;


//Cac trang activ se lien ket voi Mainactivity (trang chu) bang intent o day
public class MainActivity extends AppCompatActivity {
    private Button btnGame, btnNhiemVu, btnAnimail, btnFood, btnTravel;
    private ImageButton imbtnHome;
    private ImageButton imbtnHoc;
    private ImageButton imbtnCheckList;
    private ImageButton imbtnGame;
    private TextView tvluyenphatam;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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

        //  Gắn sự kiện là các setOnclickListener (b2)

        // Lien ket trang Phat am
        tvluyenphatam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLuyenPA = new Intent(MainActivity.this, LuyenPhatAmActivity.class);
                startActivity(intentLuyenPA);
            }
        });

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
                Intent intentNhiemVu = new Intent(MainActivity.this, ChuDeActivity.class);
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
                startActivity(intentGhepTu);
            }
        };
        btnGame.setOnClickListener(gameClickListener);
        imbtnGame.setOnClickListener(gameClickListener);

        //Xin quyền & nhắc nhở (b3)
        requestNotificationPermission();  // xin quyền nếu cần
        scheduleDailyReminder();          // đặt thông báo hằng ngày

        // Lien ket trang tro choi ghep tu (b4 : setOnApplyWindowInsetsListener nên để sau k để lên trước nó sẽ bị lỗi )
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        // Lien ket trang tien do hoc tap
            // .....

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }

    private void scheduleDailyReminder() {
    }

    private void requestNotificationPermission() {
    }


    // su kien thong bao
//    private void requestNotificationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
//                    != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
//            }
//        }
//    }


//    private void scheduleDailyReminder() {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        // 👇 SỬA DÒNG NÀY: Dùng ReminderReceiver.class thay vì ThongBaoActivity.class
//        Intent intent = new Intent(this,ThongBaoActivity.class);
//
//        // 👇 DÙNG getBroadcast() — đúng với BroadcastReceiver
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                this,
//                0,
//                intent,
//                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
//        );
//
//        // Thiết lập thời gian (16:32 mỗi ngày)
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 16);
//        calendar.set(Calendar.MINUTE, 32);
//        calendar.set(Calendar.SECOND, 0);
//
//        // Nếu thời gian đã qua trong ngày hôm nay → dời sang ngày mai
//        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//        }
//
//        // Đặt nhắc lại mỗi ngày
//        alarmManager.setRepeating(
//                AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY,
//                pendingIntent
//        );
//   }
}