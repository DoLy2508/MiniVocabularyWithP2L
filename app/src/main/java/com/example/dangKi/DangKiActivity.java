package com.example.dangKi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gheptu.Database.SQLiteConnect;
import com.example.minivocabularywithp2l.MainActivity;
import com.example.minivocabularywithp2l.R;


public class DangKiActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private TextView loginTextView;
    private SQLiteConnect databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_dangki);

        databaseHelper = new SQLiteConnect(this);

        // Ánh xạ các view từ layout
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);
        loginTextView = findViewById(R.id.login_text_view);

        // Thiết lập sự kiện click cho nút Đăng ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegistration();
            }
        });

        // Thiết lập sự kiện click cho nút chuyển sang Đăng nhập
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangKiActivity.this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void handleRegistration() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Kiểm tra dữ liệu nhập vào
        if (email.isEmpty()) {
            emailEditText.setError("Vui lòng nhập email");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Vui lòng nhập mật khẩu");
            passwordEditText.requestFocus();
            return;
        }

        // Kiểm tra độ dài mật khẩu
        if (password.length() < 6) {
            passwordEditText.setError("Mật khẩu phải có ít nhất 6 ký tự");
            passwordEditText.requestFocus();
            return;
        }

        // Kiểm tra chữ hoa
        if (!password.matches(".*[A-Z].*")) {
            passwordEditText.setError("Mật khẩu phải có ít nhất 1 chữ hoa");
            passwordEditText.requestFocus();
            return;
        }

        // Kiểm tra ký tự đặc biệt
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            passwordEditText.setError("Mật khẩu phải có ít nhất 1 ký tự đặc biệt");
            passwordEditText.requestFocus();
            return;
        }

        // Kiểm tra xem email đã tồn tại chưa
        if (databaseHelper.checkEmail(email)) {
            Toast.makeText(this, "Email này đã được đăng ký! Vui lòng sử dụng email khác.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm người dùng vào database
        boolean insertSuccess = databaseHelper.insertUser(email, password);
        if (insertSuccess) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            // Chuyển sang màn hình đăng nhập
            Intent intent = new Intent(DangKiActivity.this, DangNhapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }
}
