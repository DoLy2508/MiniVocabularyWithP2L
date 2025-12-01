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

        // Lấy email từ SharedPref
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
        boolean isDarkMode = pref.isDarkMode();
        switchDarkMode.setChecked(isDarkMode);
        
        // Cập nhật giao diện ngay lập tức
        updateUIForDarkMode(isDarkMode);
        
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
            // CHẾ ĐỘ TỐI (DARK MODE)
            // Nền: Xám than sáng hơn một chút để dễ nhìn
            mainLayout.setBackgroundColor(Color.parseColor("#303030"));
            
            // Chữ tiêu đề: Trắng
            tvTitle.setTextColor(Color.WHITE);
            
            // Chữ nhãn phụ (Tài khoản hiện tại): Xám nhạt
            tvLabelUser.setTextColor(Color.LTGRAY);
            
            // Chữ Email: Màu hồng đặc trưng (hoặc trắng/vàng nếu muốn nổi hơn) - Giữ màu hồng để nhận diện thương hiệu
            tvUserEmail.setTextColor(Color.parseColor("#FF80AB")); // Hồng nhạt hơn để nổi trên nền tối
            
            // Chữ nhãn Dark Mode: Trắng
            tvDarkModeLabel.setTextColor(Color.WHITE);
            
            // Nút Back: Trắng để nổi bật trên nền tối
            btnExitSetting.setColorFilter(Color.WHITE);
            
        } else {
            // CHẾ ĐỘ SÁNG (LIGHT MODE)
            // Nền: Hồng nhạt
            mainLayout.setBackgroundColor(Color.parseColor("#FFF0F5"));
            
            // Chữ tiêu đề: Hồng đậm
            tvTitle.setTextColor(Color.parseColor("#D81B60"));
            
            // Chữ nhãn phụ: Xám
            tvLabelUser.setTextColor(Color.parseColor("#888888"));
            
            // Chữ Email: Hồng đậm
            tvUserEmail.setTextColor(Color.parseColor("#D81B60"));
            
            // Chữ nhãn Dark Mode: Đen/Xám đậm
            tvDarkModeLabel.setTextColor(Color.parseColor("#333333"));
            
            // Nút Back: Xám đậm để nhìn rõ trên nền hồng nhạt
            btnExitSetting.setColorFilter(Color.parseColor("#808080"));
        }
    }
}
