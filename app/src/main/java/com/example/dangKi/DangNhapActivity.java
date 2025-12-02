package com.example.dangKi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

        databaseHelper = new SQLiteConnect(this);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgot = findViewById(R.id.tvForgot);


        // Login
        btnLogin.setOnClickListener(v -> handleLogin());

        tvForgot.setOnClickListener(v -> {
            showForgotPasswordDialog();
        });

    
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quên mật khẩu");


        EditText input = new EditText(this);
        input.setHint("Nhập email của bạn");
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        builder.setPositiveButton("Gửi", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteConnect db = new SQLiteConnect(this);
            if (db.checkEmail(email)) {

                showResetPasswordDialog(email);
            } else {
                Toast.makeText(this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
            }
            db.close();
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void showResetPasswordDialog(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đặt lại mật khẩu");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText etNewPass = new EditText(this);
        etNewPass.setHint("Mật khẩu mới");
        etNewPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(etNewPass);

        EditText etConfirmPass = new EditText(this);
        etConfirmPass.setHint("Nhập lại mật khẩu");
        etConfirmPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(etConfirmPass);

        builder.setView(layout);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newPass = etNewPass.getText().toString().trim();
            String confirmPass = etConfirmPass.getText().toString().trim();

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }


            SQLiteConnect db = new SQLiteConnect(this);

            boolean updated = db.updatePassword(email, newPass);
            if (updated) {
                Toast.makeText(this, "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
            }
            db.close();
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
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

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Email không hợp lệ (Ví dụ: abc@gmail.com)");
            editEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editPassword.setError("Vui lòng nhập mật khẩu");
            editPassword.requestFocus();
            return;
        }


        boolean loginSuccess = databaseHelper.checkUser(email, password);

        if(loginSuccess){
            boolean isAdmin = databaseHelper.isAdmin(email); // kiểm tra quyền admin

            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

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