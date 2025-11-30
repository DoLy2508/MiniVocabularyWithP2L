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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dangKi.DangKiActivity;
import com.example.minivocabularywithp2l.MySharedPreferences;
import com.example.minivocabularywithp2l.R;

public class SettingActivity extends AppCompatActivity {

    private TextView tvUserEmail, tvTitle, tvLabelUser, tvDarkModeLabel;
    private Switch switchDarkMode;
    private Button btnLogout;
    private ImageButton btnExitSetting;
    private LinearLayout mainLayout;
    private String userEmail;
    private MySharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Khởi tạo SharedPreferences
        pref = new MySharedPreferences(this);

        // Ánh xạ view
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvTitle = findViewById(R.id.tvTitle);
        tvLabelUser = findViewById(R.id.tvLabelUser);
        tvDarkModeLabel = findViewById(R.id.tvDarkModeLabel);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        btnLogout = findViewById(R.id.btnLogout);
        mainLayout = findViewById(R.id.main_layout);
        btnExitSetting = findViewById(R.id.btnExitSetting); // Ánh xạ nút thoát

        // Lấy email từ SharedPref (chính xác hơn là lấy từ Intent vì Intent có thể thiếu)
        // Ưu tiên lấy từ Intent (nếu có), nếu không thì lấy từ Pref
        String emailFromIntent = getIntent().getStringExtra("email");
        if (emailFromIntent != null && !emailFromIntent.isEmpty()) {
            userEmail = emailFromIntent;
        } else {
            userEmail = pref.getUserEmail();
        }

        if (userEmail != null && !userEmail.isEmpty() && !userEmail.equals("Guest")) {
            tvUserEmail.setText(userEmail);
        } else {
            tvUserEmail.setText("Chưa đăng nhập");
        }

        // Set trạng thái Dark Mode hiện tại cho Switch
        switchDarkMode.setChecked(pref.isDarkMode());
        updateUIForDarkMode(pref.isDarkMode());
        
        // Xử lý sự kiện nút Exit (Thoát trang Setting)
        btnExitSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity hiện tại để quay về màn hình trước
            }
        });


        // Xử lý sự kiện Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa session
                pref.clearUserSession();
                
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
                // Lưu trạng thái vào Pref
                pref.setDarkMode(isChecked);
                
                // Áp dụng chế độ tối toàn cục
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                
                // Cập nhật giao diện trang hiện tại
                updateUIForDarkMode(isChecked);
            }
        });
    }
    
    private void updateUIForDarkMode(boolean isDark) {
        if (isDark) {
            // Chế độ tối: Đổi sang màu xám than #303030 để sáng hơn
            mainLayout.setBackgroundColor(Color.parseColor("#303030"));
            tvTitle.setTextColor(Color.WHITE);
            tvLabelUser.setTextColor(Color.LTGRAY);
            tvUserEmail.setTextColor(Color.WHITE);
            tvDarkModeLabel.setTextColor(Color.WHITE);
            btnExitSetting.setColorFilter(Color.WHITE); // Đổi màu nút mũi tên sang trắng
        } else {
            // Chế độ sáng
            mainLayout.setBackgroundColor(Color.parseColor("#FFF0F5"));
            tvTitle.setTextColor(Color.parseColor("#D81B60"));
            tvLabelUser.setTextColor(Color.parseColor("#888888"));
            tvUserEmail.setTextColor(Color.parseColor("#D81B60"));
            tvDarkModeLabel.setTextColor(Color.parseColor("#333333"));
            btnExitSetting.setColorFilter(Color.parseColor("#808080")); // Đổi màu nút mũi tên sang xám
        }
    }
}
