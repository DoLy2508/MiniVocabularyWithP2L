package com.example.LuyenPA.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.gheptu.Database.SQLiteConnect;
import com.example.minivocabularywithp2l.R;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class ThemMoiTuPaActivity extends AppCompatActivity {
    EditText edtThemMaTu, edtThemTiengAnh, edtThemNguAm, edtThemTiengViet;
    Spinner spinnerAudio;
    Button btnHuyThemMoi, btnThemMoiTu;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.them_moi_tu_phatam);

        edtThemMaTu = findViewById(R.id.edtThemMaTu);
        edtThemTiengAnh = findViewById(R.id.edtThemTiengAnh);
        edtThemNguAm = findViewById(R.id.edtThemNguAm);
        edtThemTiengViet = findViewById(R.id.edtThemTiengViet);
        spinnerAudio = findViewById(R.id.spinnerAudio);

        // Lấy danh sách file trong thư mục raw
        Field[] fields = R.raw.class.getFields();
        ArrayList<String> listAudio = new ArrayList<>();

        listAudio.add("Chọn audio"); // item mặc định

        for (Field field : fields) {
            listAudio.add(field.getName());   // lấy tên file (không có .mp3)
        }

// Đưa vào spinner bằng ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                listAudio
        );
        spinnerAudio.setAdapter(adapter);

        btnHuyThemMoi = findViewById(R.id.btnHuyThemMoi);
        btnThemMoiTu = findViewById(R.id.btnThemMoiTu);

        btnHuyThemMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnThemMoiTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maTu = edtThemMaTu.getText().toString().trim();
                String tiengAnh = edtThemTiengAnh.getText().toString().trim();
                String nguAm = edtThemNguAm.getText().toString().trim();
                String tiengViet = edtThemTiengViet.getText().toString().trim();
                String tenAudio = spinnerAudio.getSelectedItem().toString().trim();

                // 1. Kiểm tra đầu vào
                if (maTu.isEmpty() || tiengAnh.isEmpty() || nguAm.isEmpty() || tiengViet.isEmpty()) {
                    Toast.makeText(ThemMoiTuPaActivity.this, "Vui lòng nhập đầy đủ thông tin!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tenAudio.equals("Chọn audio")) {
                    Toast.makeText(ThemMoiTuPaActivity.this, "Bạn chưa chọn audio!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Khai báo đối tượng database ở ngoài để có thể truy cập trong 'finally'
                SQLiteConnect sqLiteConnect = null;

                try {
                    // 2. Tạo kết nối
                    sqLiteConnect = new SQLiteConnect(
                            getBaseContext(),
                            getString(R.string.db_name),
                            null,
                            SQLiteConnect.DATABASE_VERSION
                    );

                    // 3. Thực hiện INSERT
                    String query = "INSERT INTO tuvungPAm (maTu, tiengAnh, nguAm, tiengViet, tenAudio) " +
                            "VALUES ('" + maTu + "', '" + tiengAnh + "', '" + nguAm + "', '" +  tiengViet + "', '" + tenAudio + "')";

                    sqLiteConnect.queryData(query);

                    Toast.makeText(ThemMoiTuPaActivity.this,
                            "Thêm mới " + maTu + " - " + tiengAnh + " thành công",
                            Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);
                    finish();

                } catch (Exception e) {
                    // Xử lý lỗi SQL, lỗi kết nối, v.v.
                    Log.e("Loi INSERT CSDL", "Ghi CSDL thất bại: " + e.getMessage());
                    Toast.makeText(ThemMoiTuPaActivity.this,
                            "Lỗi thêm từ: Vui lòng kiểm tra Logcat",
                            Toast.LENGTH_LONG).show();

                } finally {
                    // 4. Đóng kết nối database
                    if (sqLiteConnect != null) {
                        sqLiteConnect.close();
                    }
                }
            }
        });








    }
}