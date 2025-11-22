package com.example.gheptu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gheptu.Adapter.TuVungGTAdapter;
import com.example.gheptu.Database.SQLiteConnect;
import com.example.gheptu.Model.TuVungGhepTu;
import com.example.gheptu.activities.ThemMoiTuActivity;
import com.example.minivocabularywithp2l.R;


import java.util.ArrayList;

public class QuanLiTuActivity extends AppCompatActivity {
    ImageButton imbtnHome, imbtnHoc, imbtnCheckList, imbtnGame;
    ListView lvTuVugGhepTu;
    ArrayList<TuVungGhepTu> listTu;
    TuVungGTAdapter tuVungGTAdapter;
    SQLiteConnect sqLiteConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.quan_li_tu_gheptu);
        imbtnHome = findViewById(R.id.imbtnHome);
        imbtnHoc = findViewById(R.id.imbtnHoc);
        imbtnCheckList = findViewById(R.id.imbtnCheckList);
        imbtnGame = findViewById(R.id.imbtnGame);

        imbtnGame.setOnClickListener(v -> {
            startActivity(new Intent(QuanLiTuActivity.this, GhepTuActivity.class));
            finish();

        });


        lvTuVugGhepTu = findViewById(R.id.lvTuVugGhepTu);
        listTu = new ArrayList<>();
        sqLiteConnect = new SQLiteConnect(getBaseContext(), getString(R.string.db_name), null, 1);
        String query = "CREATE TABLE IF NOT EXISTS tuvungGT_v2 (" +
                " id       INTEGER PRIMARY KEY AUTOINCREMENT," +
                " maTu      TEXT," +
                " tiengAnh  TEXT," +
                " tiengViet TEXT)";
        sqLiteConnect.queryData(query);





        tuVungGTAdapter = new TuVungGTAdapter(QuanLiTuActivity.this, R.layout.item_quan_li_gheptu, listTu);
        lvTuVugGhepTu.setAdapter(tuVungGTAdapter);

        loadDataTuVung();

        lvTuVugGhepTu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(QuanLiTuActivity.this, listTu.get(position).getTiengAnh(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void loadDataTuVung(){
        String query = "SELECT * FROM tuvungGT_v2";
        Cursor cursor = sqLiteConnect.getData(query);
        listTu.clear();

        while (cursor.moveToNext()){
            int key = cursor.getInt(0);
            String maTu = cursor.getString(1);
            String tiengAnh = cursor.getString(2);
            String tiengViet = cursor.getString(3);
            TuVungGhepTu tu = new TuVungGhepTu(key, maTu, tiengAnh, tiengViet);
            listTu.add(tu);


        }
        tuVungGTAdapter.notifyDataSetChanged();
    }
    ActivityResultLauncher themMoiTuLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        loadDataTuVung();
                    }
                }

            }
    );
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_gheptu, menu);
        MenuItem menuThemMoiTu = menu.findItem(R.id.menuThemMoiTu);

        menuThemMoiTu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent themMoiTuIntent = new Intent(QuanLiTuActivity.this, ThemMoiTuActivity.class);
                themMoiTuLauncher.launch(themMoiTuIntent);
                return false;

            }
        });
        MenuItem menuSearch = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                tuVungGTAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tuVungGTAdapter.getFilter().filter(newText);
                return false;


            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == 123){
            loadDataTuVung();
        }

    }





}