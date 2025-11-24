package com.example.gheptu.activities;


import android.content.Intent;
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
        TuVungGhepTu tu = (TuVungGhepTu) data.get("tu");

        edtSuaMaTu.setText(tu.getMaTu());
        edtSuaTiengAnh.setText(tu.getTiengAnh());
        edtSuaTiengViet.setText(tu.getTiengViet());

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
                    String query = "UPDATE tuvungGT_v2 SET maTu = '" + maTu +
                            "', tiengAnh = '" + tiengAnh + "', tiengViet = '"
                            + tiengViet + "' WHERE id = '" + tu.getId() + "'";
                    SQLiteConnect sqLiteConnect = new SQLiteConnect(getBaseContext(), getString(R.string.db_name), null,  SQLiteConnect.DATABASE_VERSION);
                    sqLiteConnect.queryData(query);
                    Toast.makeText(SuaTuActivity.this, "Sua tu " + maTu + " - " + tiengAnh + " thanh cong",
                            Toast.LENGTH_SHORT).show();
                    setResult(123);
                    finish();


                } catch (Exception e) {
                    Log.d("Loi UPDATE CSDL", e.toString());
                    Toast.makeText(SuaTuActivity.this, "Loi UPDATE CSDL",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}