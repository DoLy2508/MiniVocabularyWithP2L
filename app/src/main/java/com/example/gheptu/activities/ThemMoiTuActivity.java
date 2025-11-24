package com.example.gheptu.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gheptu.Database.SQLiteConnect;
import com.example.minivocabularywithp2l.R;


public class ThemMoiTuActivity extends AppCompatActivity {
    EditText edtThemMaTu, edtThemTiengAnh, edtThemTiengViet;
    Button btnHuyThemMoi, btnThemMoiTu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.them_moi_tu_gheptu);
        edtThemMaTu = findViewById(R.id.edtThemMaTu);
        edtThemTiengAnh = findViewById(R.id.edtThemTiengAnh);
        edtThemTiengViet = findViewById(R.id.edtThemTiengViet);
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
                try{
                    String maTu = edtThemMaTu.getText().toString().trim();
                    String tiengAnh = edtThemTiengAnh.getText().toString().trim();
                    String tiengViet = edtThemTiengViet.getText().toString().trim();

                    String query = "INSERT INTO tuvungGT_v2 (maTu, tiengAnh, tiengViet) "
                            + "VALUES ('" + maTu + "', '" + tiengAnh + "', '" + tiengViet + "')";

                    SQLiteConnect sqLiteConnect = new SQLiteConnect(getBaseContext(), getString(R.string.db_name), null,  SQLiteConnect.DATABASE_VERSION);
                    sqLiteConnect.queryData(query); // thay doi du lieu
                    Toast.makeText(ThemMoiTuActivity.this, "Them moi" + maTu + "- "+ tiengAnh +" thanh cong",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();

                } catch (Exception e){
                    Log.d("Loi INSERT CSDL", e.toString());
                    Toast.makeText(ThemMoiTuActivity.this, "Loi INSERT CSDL",
                            Toast.LENGTH_SHORT).show();

                }





            }
        });
    }
}