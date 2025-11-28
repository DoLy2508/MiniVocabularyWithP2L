package com.example.LuyenPA;

import android.content.Intent;
import android.database.Cursor;
import android.speech.tts.TextToSpeech; // <-- Import TextToSpeech
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.LuyenPA.Model.LuyenPhatAm;
import com.example.gheptu.Database.SQLiteConnect;
import com.example.minivocabularywithp2l.MainActivity;
import com.example.minivocabularywithp2l.R;

import java.util.ArrayList;
import java.util.Locale; // <-- Import Locale


// activ cua trang luyen phat am

public class LuyenPhatAmActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private ImageButton imbtnQuanLiTuPA;
    private ArrayList<LuyenPhatAm> listTuPA;
    private TextView tvNguAm, tvNghiaTV, tvTiengAnh;
    private ImageView imvNghe;
    private Button btnContinue;

    private int currentIndex = 0;
    private ImageView imvTroVe;
    private SQLiteConnect db;

    //Khai báo biến TextToSpeech
    private TextToSpeech tts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_luyenphatam);
        imvTroVe = findViewById(R.id.imvTroVe);
        imbtnQuanLiTuPA = findViewById(R.id.imbtnQuanLiTuPA);
        btnContinue = findViewById(R.id.btnContinue);

        tvTiengAnh = findViewById(R.id.tvTiengAnh);
        tvNguAm = findViewById(R.id.tvNguAm);
        tvNghiaTV = findViewById(R.id.tvNghiaTV);
        imvNghe = findViewById(R.id.imvNghe);
        // Khởi tạo TextToSpeech
        tts = new TextToSpeech(this, this);


        imvTroVe.setOnClickListener(v -> {
            startActivity(new Intent(LuyenPhatAmActivity.this, MainActivity.class));
            finish();


        });
        imbtnQuanLiTuPA.setOnClickListener(v -> {
            Intent intentPA = new Intent(LuyenPhatAmActivity.this, QuanLiTuPaActivity.class);
            startActivity(intentPA);
        });


        db = new SQLiteConnect(this, getString(R.string.db_name), null, SQLiteConnect.DATABASE_VERSION);


        listTuPA = new ArrayList<>();

        Cursor cursor = db.getData("SELECT * FROM tuvungPAm");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String maTu = cursor.getString(1);
            String tiengAnh = cursor.getString(2);
            String nguAm = cursor.getString(3);
            String tiengViet = cursor.getString(4);

            listTuPA.add(new LuyenPhatAm(id, maTu, tiengAnh, nguAm, tiengViet));
        }
        cursor.close();

        // Hiển thị từ đầu tiên nếu có
        if (!listTuPA.isEmpty()) {
            displayCurrentWord();
        } else {
            Toast.makeText(this, "Không có dữ liệu từ vựng.", Toast.LENGTH_LONG).show();
        }



        imvNghe.setOnClickListener(v -> {
            if (listTuPA.isEmpty() || currentIndex >= listTuPA.size()) {
                Toast.makeText(this, "Không có từ để phát âm", Toast.LENGTH_SHORT).show();
                return;
            }
            // Lấy từ tiếng Anh hiện tại
            String wordToSpeak = listTuPA.get(currentIndex).getTiengAnh();

            if (tts != null && !wordToSpeak.isEmpty()) {
                tts.speak(wordToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(this, "Không thể phát âm từ này", Toast.LENGTH_SHORT).show();
            }

        });


        btnContinue.setOnClickListener(v -> {

            currentIndex++;
            if (currentIndex < listTuPA.size()) {
                displayCurrentWord();
            } else {
                // Đặt lại từ đầu hoặc thông báo đã hết
                currentIndex = 0; // Quay về từ đầu tiên
                displayCurrentWord();
                Toast.makeText(this, "Đã hết danh sách! Quay về từ đầu.", Toast.LENGTH_SHORT).show();
            }

        });

    }

    //phương thức quản lý TextToSpeech
    @Override
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
        // Giải phóng TTS khi activity bị hủy
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();

    }

   // tai lai du lieu tu database
    private void loadDataFromDatabase() {
        // Luôn xóa danh sách cũ trước khi tải mới để tránh trùng lặp
        if (listTuPA != null) {
            listTuPA.clear();
        } else {
            listTuPA = new ArrayList<>();
        }

        // Mở kết nối và truy vấn dữ liệu
        db = new SQLiteConnect(this, getString(R.string.db_name), null, SQLiteConnect.DATABASE_VERSION);
        Cursor cursor = db.getData("SELECT * FROM tuvungPAm");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String maTu = cursor.getString(1);
            String tiengAnh = cursor.getString(2);
            String nguAm = cursor.getString(3);
            String tiengViet = cursor.getString(4);
            listTuPA.add(new LuyenPhatAm(id, maTu, tiengAnh, nguAm, tiengViet));
        }
        cursor.close();
        db.close(); // Đóng kết nối sau khi dùng xong
    }
    // Thêm phương thức onResume() de reset lai vi tri va hien thi lai tu vung
    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại dữ liệu mỗi khi Activity được hiển thị
        loadDataFromDatabase();


        // Kiểm tra xem currentIndex có còn hợp lệ không
        if (currentIndex >= listTuPA.size()) {
            currentIndex = 0; // Reset về từ đầu tiên nếu vị trí cũ không còn tồn tại
        }

        if (!listTuPA.isEmpty()) {
            displayCurrentWord();
        } else {

            tvTiengAnh.setText("Trống");
            tvNguAm.setText("");
            tvNghiaTV.setText("Không có từ vựng");
            Toast.makeText(this, "Không có dữ liệu từ vựng.", Toast.LENGTH_SHORT).show();
        }
    }


    private void displayCurrentWord() {
        if (currentIndex >= listTuPA.size()) {
            Toast.makeText(this, "Đã hết từ vựng!", Toast.LENGTH_SHORT).show();
            return;
        }

        LuyenPhatAm tu = listTuPA.get(currentIndex);
        tvTiengAnh.setText(tu.getTiengAnh());
        tvNguAm.setText("/" + tu.getNguAm() + "/");
        tvNghiaTV.setText(tu.getTiengViet());


    }
}