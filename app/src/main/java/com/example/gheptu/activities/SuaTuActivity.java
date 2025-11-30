package com.example.gheptu.activities;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gheptu.Database.SQLiteConnect;
import com.example.gheptu.Model.TuVungGhepTu;
import com.example.minivocabularywithp2l.R;


public class SuaTuActivity extends AppCompatActivity {
    EditText edtSuaMaTu, edtSuaTiengAnh, edtSuaTiengViet;
    Button btnHuySuaTu, btnSuaTu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sua_tu_gheptu);

        edtSuaMaTu = findViewById(R.id.edtSuaMaTu);
        edtSuaTiengAnh = findViewById(R.id.edtSuaTiengAnh);
        edtSuaTiengViet = findViewById(R.id.edtSuaTiengViet);
        btnHuySuaTu = findViewById(R.id.btnHuySuaTu);
        btnSuaTu = findViewById(R.id.btnSuaTu);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        
        if (data == null || data.getSerializable("tu") == null) {
            Toast.makeText(this, "Lỗi dữ liệu từ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Ép kiểu cẩn thận hơn (nếu TuVungGhepTu implement Serializable)
        TuVungGhepTu tu = (TuVungGhepTu) data.get("tu");

        if (tu != null) {
            edtSuaMaTu.setText(tu.getMaTu());
            edtSuaTiengAnh.setText(tu.getTiengAnh());
            edtSuaTiengViet.setText(tu.getTiengViet());
        }

        btnHuySuaTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSuaTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String maTu = edtSuaMaTu.getText().toString().trim();
                    String tiengAnh = edtSuaTiengAnh.getText().toString().trim();
                    String tiengViet = edtSuaTiengViet.getText().toString().trim();
                    
                    if (maTu.isEmpty() || tiengAnh.isEmpty() || tiengViet.isEmpty()) {
                        Toast.makeText(SuaTuActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // CÁCH MỚI: Dùng ContentValues (An toàn hơn, dễ đọc hơn)
                    SQLiteConnect dbHelper = new SQLiteConnect(SuaTuActivity.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put("maTu", maTu);
                    values.put("tiengAnh", tiengAnh);
                    values.put("tiengViet", tiengViet);

                    // Cập nhật vào bảng tuvungGT_v2
                    int rows = db.update(SQLiteConnect.TABLE_TUVUNG_GT, values, "id = ?", new String[]{String.valueOf(tu.getId())});
                    db.close();

                    if (rows > 0) {
                        Toast.makeText(SuaTuActivity.this, "Sửa từ thành công!", Toast.LENGTH_SHORT).show();
                        setResult(123); // Trả về kết quả thành công
                        finish();
                    } else {
                        Toast.makeText(SuaTuActivity.this, "Sửa thất bại (Không tìm thấy ID)", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("Loi UPDATE CSDL", e.toString());
                    Toast.makeText(SuaTuActivity.this, "Lỗi cập nhật CSDL", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
