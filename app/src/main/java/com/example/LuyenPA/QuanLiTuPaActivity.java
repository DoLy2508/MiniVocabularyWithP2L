package com.example.LuyenPA;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.example.LuyenPA.Activities.ThemMoiTuPaActivity;
import com.example.LuyenPA.Adapter.TuVungPAadapter;
import com.example.LuyenPA.Model.LuyenPhatAm;
import com.example.gheptu.Database.SQLiteConnect;
import com.example.minivocabularywithp2l.R;

import java.util.ArrayList;

public class QuanLiTuPaActivity extends AppCompatActivity {
    ImageButton imbtnHome, imbtnHoc, imbtnCheckList, imbtnGame;
    ImageView imvTroLai;
    ListView lvTuVugPhatAm;
    ArrayList<LuyenPhatAm> listTuPA;
    TuVungPAadapter tuVungPaAdapter;
    SQLiteConnect sqLiteConnect;

    // launcher sua
    public ActivityResultLauncher<Intent> suaTuLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadDataTuVung();   // reload lại list
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.quan_li_tu_phatam);
        imbtnHome = findViewById(R.id.imbtnHome);
        imbtnHoc = findViewById(R.id.imbtnHoc);
        imbtnCheckList = findViewById(R.id.imbtnCheckList);
        imbtnGame = findViewById(R.id.imbtnGame);
        imvTroLai = findViewById(R.id.imvTroLai);

        lvTuVugPhatAm = findViewById(R.id.lvTuVugPhatAm);

        imvTroLai.setOnClickListener(v -> finish());

        listTuPA = new ArrayList<>();

        sqLiteConnect = new SQLiteConnect(
                getBaseContext(),
                getString(R.string.db_name),
                null,
                SQLiteConnect.DATABASE_VERSION);


        tuVungPaAdapter = new TuVungPAadapter(QuanLiTuPaActivity.this, R.layout.item_quan_li_phatam, listTuPA);
        lvTuVugPhatAm.setAdapter(tuVungPaAdapter);

        loadDataTuVung();
        lvTuVugPhatAm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LuyenPhatAm tuPA = listTuPA.get(position);
                String tenAudio = tuPA.getTenAudio();   // tên lưu trong DB (vd: rainbow)

                int audioId = getResources().getIdentifier(tenAudio, "raw", getPackageName());

                if (audioId != 0) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(QuanLiTuPaActivity.this, audioId);
                    mediaPlayer.start();
                } else {
                    Toast.makeText(QuanLiTuPaActivity.this, "Không tìm thấy audio: " + tenAudio, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void loadDataTuVung(){
        listTuPA.clear();
        String query = "SELECT * FROM tuvungPAm";
        Cursor cursor = sqLiteConnect.getData(query);


        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String maTu = cursor.getString(1);
            String tiengAnh = cursor.getString(2);
            String nguAm = cursor.getString(3);
            String tiengViet = cursor.getString(4);
            String tenAudio = cursor.getString(5);

            LuyenPhatAm tuPA = new LuyenPhatAm(id, maTu, tiengAnh, nguAm, tiengViet, tenAudio);
            listTuPA.add(tuPA);
        }
        cursor.close();

        tuVungPaAdapter.notifyDataSetChanged();
    }
    ActivityResultLauncher<Intent> themMoiTuLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadDataTuVung(); // reload danh sách mới thêm
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
                Intent themMoiTuIntent = new Intent(QuanLiTuPaActivity.this, ThemMoiTuPaActivity.class);
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
                tuVungPaAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tuVungPaAdapter.getFilter().filter(newText);
                return false;


            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 456 && resultCode == 456){
            loadDataTuVung();
        }

    }


}