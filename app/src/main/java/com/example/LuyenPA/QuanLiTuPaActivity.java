package com.example.LuyenPA;

import android.content.Intent;
import android.database.Cursor;
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
import android.speech.tts.TextToSpeech;
import java.util.Locale;

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

public class QuanLiTuPaActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    ImageButton imbtnHome, imbtnHoc, imbtnCheckList, imbtnGame;
    ImageView imvTroLai;
    ListView lvTuVugPhatAm;
    ArrayList<LuyenPhatAm> listTuPA;
    TuVungPAadapter tuVungPaAdapter;
    SQLiteConnect sqLiteConnect;
    private TextToSpeech tts; // <-- Thêm biến TextToSpeech

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
        // KHỞI TẠO TEXTTOSPEECH
        tts = new TextToSpeech(this, this); // this chính là OnInitListener

        lvTuVugPhatAm = findViewById(R.id.lvTuVugPhatAm);

        imvTroLai.setOnClickListener(v -> finish());

        listTuPA = new ArrayList<>();

        sqLiteConnect = new SQLiteConnect(
                getBaseContext(),
                getString(R.string.db_name),
                null,
                SQLiteConnect.DATABASE_VERSION);


        tuVungPaAdapter = new TuVungPAadapter(QuanLiTuPaActivity.this, R.layout.item_quan_li_phatam, listTuPA, tts);
        lvTuVugPhatAm.setAdapter(tuVungPaAdapter);

        loadDataTuVung();

    }

    // THÊM CÁC PHƯƠNG THỨC ĐỂ XỬ LÝ TEXTTOSPEECH
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US); // Giọng Anh-Mỹ
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Ngôn ngữ này không được hỗ trợ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Khởi tạo TextToSpeech thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        // Giải phóng TTS khi activity bị hủy để tránh rò rỉ bộ nhớ
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
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


            LuyenPhatAm tuPA = new LuyenPhatAm(id, maTu, tiengAnh, nguAm, tiengViet);
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