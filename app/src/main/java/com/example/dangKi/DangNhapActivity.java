package com.example.dangKi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chuDe.ChuDeActivity;
import com.example.gheptu.Database.SQLiteConnect;
import com.example.minivocabularywithp2l.MainActivity;
import com.example.minivocabularywithp2l.R;

public class DangNhapActivity extends AppCompatActivity {
    EditText editEmail,editPassword;
    Button btnLogin;
    TextView tvForgot;

    private SQLiteConnect databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.trang_dangnhap);

        databaseHelper = new SQLiteConnect(this); // ← KHỞI TẠO databaseHelper
        
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgot = findViewById(R.id.tvForgot);


        // Login
        btnLogin.setOnClickListener(v -> handleLogin());

    
    }

    // kiem tra user hay admin
    private void handleLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if(email.isEmpty()){
            editEmail.setError("Vui lòng nhập email");
            editEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editPassword.setError("Vui lòng nhập mật khẩu");
            editPassword.requestFocus();
            return;
        }

        // kiểm tra đăng nhập
        boolean loginSuccess = databaseHelper.checkUser(email, password);

        if(loginSuccess){
            boolean isAdmin = databaseHelper.isAdmin(email); // kiểm tra quyền admin

            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            // Gửi thông tin sang MainActivity
            Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("isAdmin", isAdmin);
            startActivity(intent);

            finish();
        } else {
            Toast.makeText(this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
        }
    }

}