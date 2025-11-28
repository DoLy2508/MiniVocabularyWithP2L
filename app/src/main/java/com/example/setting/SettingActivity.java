package com.example.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dangKi.DangKiActivity;
import com.example.minivocabularywithp2l.R;

public class SettingActivity extends AppCompatActivity {

    private TextView tvUserEmail, tvTitle, tvLabelUser, tvDarkModeLabel;
    private Switch switchDarkMode;
    private Button btnLogout;
    private LinearLayout mainLayout;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Ánh xạ view
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvTitle = findViewById(R.id.tvTitle);
        tvLabelUser = findViewById(R.id.tvLabelUser);
        tvDarkModeLabel = findViewById(R.id.tvDarkModeLabel);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        btnLogout = findViewById(R.id.btnLogout);
        mainLayout = findViewById(R.id.main_layout);

        // Nhận dữ liệu email từ Intent
        userEmail = getIntent().getStringExtra("email");
        if (userEmail != null && !userEmail.isEmpty()) {
            tvUserEmail.setText(userEmail);
        } else {
            tvUserEmail.setText("Guest / Chưa đăng nhập");
        }

        // Xử lý sự kiện Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về màn hình Đăng ký (hoặc Đăng nhập tùy ý)
                Intent intent = new Intent(SettingActivity.this, DangKiActivity.class);
                // Xóa cờ activity stack để người dùng không thể back lại màn hình Setting/Main
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý sự kiện Dark Mode
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Chế độ tối (Dark Mode giả lập)
                    mainLayout.setBackgroundColor(Color.parseColor("#121212")); // Màu nền đen
                    tvTitle.setTextColor(Color.WHITE);
                    tvLabelUser.setTextColor(Color.LTGRAY);
                    tvUserEmail.setTextColor(Color.WHITE);
                    tvDarkModeLabel.setTextColor(Color.WHITE);
                } else {
                    // Chế độ sáng (Light Mode - Mặc định hồng)
                    mainLayout.setBackgroundColor(Color.parseColor("#FFF0F5")); // Màu hồng nhạt
                    tvTitle.setTextColor(Color.parseColor("#D81B60"));
                    tvLabelUser.setTextColor(Color.parseColor("#888888"));
                    tvUserEmail.setTextColor(Color.parseColor("#D81B60"));
                    tvDarkModeLabel.setTextColor(Color.parseColor("#333333"));
                }
            }
        });
    }
}
