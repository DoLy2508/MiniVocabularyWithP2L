package com.example.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.dangKi.DangKiActivity;
import com.example.minivocabularywithp2l.R;

public class SettingActivity extends AppCompatActivity {

    private TextView tvUserEmail, tvTitle, tvLabelUser, tvDarkModeLabel;
    private Switch switchDarkMode;
    private Button btnLogout;
    private LinearLayout layout_setting;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvTitle = findViewById(R.id.tvTitle);
        tvLabelUser = findViewById(R.id.tvLabelUser);
        tvDarkModeLabel = findViewById(R.id.tvDarkModeLabel);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        btnLogout = findViewById(R.id.btnLogout);
        layout_setting = findViewById(R.id.layout_setting);


        userEmail = getIntent().getStringExtra("email");
        if (userEmail != null && !userEmail.isEmpty()) {
            tvUserEmail.setText(userEmail);
        } else {
            tvUserEmail.setText("Guest / Chưa đăng nhập");
        }


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this, DangKiActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    layout_setting.setBackgroundColor(Color.parseColor("#121212")); // Màu nền đen
                    tvTitle.setTextColor(Color.WHITE);
                    tvLabelUser.setTextColor(Color.LTGRAY);
                    tvUserEmail.setTextColor(Color.WHITE);
                    tvDarkModeLabel.setTextColor(Color.WHITE);
                } else {

                    layout_setting.setBackgroundColor(Color.parseColor("#FFF0F5")); // Màu hồng nhạt
                    tvTitle.setTextColor(Color.parseColor("#D81B60"));
                    tvLabelUser.setTextColor(Color.parseColor("#888888"));
                    tvUserEmail.setTextColor(Color.parseColor("#D81B60"));
                    tvDarkModeLabel.setTextColor(Color.parseColor("#333333"));
                }
            }
        });
    }
}