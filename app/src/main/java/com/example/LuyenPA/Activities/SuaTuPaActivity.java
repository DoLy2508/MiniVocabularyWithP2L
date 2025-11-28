package com.example.LuyenPA.Activities;

import android.content.Intent;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.LuyenPA.Model.LuyenPhatAm;
import com.example.gheptu.Database.SQLiteConnect;
import com.example.gheptu.activities.SuaTuActivity;
import com.example.minivocabularywithp2l.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SuaTuPaActivity extends AppCompatActivity {
    EditText edtSuaMaTu, edtSuaTiengAnh, edtSuaNguAm, edtSuaTiengViet;
    Button btnHuySuaTu, btnLuu;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sua_tu_phatam);

        edtSuaMaTu = findViewById(R.id.edtSuaMaTu);
        edtSuaTiengAnh = findViewById(R.id.edtSuaTiengAnh);
        edtSuaNguAm = findViewById(R.id.edtSuaNguAm);
        edtSuaTiengViet = findViewById(R.id.edtSuaTiengViet);

        btnHuySuaTu = findViewById(R.id.btnHuySuaTu);
        btnLuu = findViewById(R.id.btnLuu);





        Intent intent = getIntent();
        Bundle data = intent.getExtras();

        LuyenPhatAm tu = (LuyenPhatAm) data.get("tu");




        edtSuaMaTu.setText(tu.getMaTu());
        edtSuaTiengAnh.setText(tu.getTiengAnh());
        edtSuaNguAm.setText(tu.getNguAm());
        edtSuaTiengViet.setText(tu.getTiengViet());



        btnHuySuaTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String maTu = edtSuaMaTu.getText().toString().trim();
                    String tiengAnh = edtSuaTiengAnh.getText().toString().trim();
                    String nguAm = edtSuaNguAm.getText().toString().trim();
                    String tiengViet = edtSuaTiengViet.getText().toString().trim();

                    String query = "UPDATE tuvungPAm SET maTu = '" + maTu +
                            "', tiengAnh = '" + tiengAnh + "', nguAm = '" + nguAm + "', tiengViet = '"
                            + tiengViet + "' WHERE id = '" + tu.getId() + "'";
                    SQLiteConnect sqLiteConnect = new SQLiteConnect(getBaseContext(),
                            getString(R.string.db_name), null, SQLiteConnect.DATABASE_VERSION);
                    sqLiteConnect.queryData(query);
                    Toast.makeText(SuaTuPaActivity.this, "Sua tu " + maTu + " - " + tiengAnh + " thanh cong",
                            Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();


                } catch (Exception e) {
                    Log.d("Loi UPDATE CSDL", e.toString());
                    Toast.makeText(SuaTuPaActivity.this, "Loi UPDATE CSDL",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });










    }
}