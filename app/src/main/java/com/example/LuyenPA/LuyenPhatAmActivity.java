package com.example.LuyenPA;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
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
import java.util.Locale;

public class LuyenPhatAmActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private ImageButton imbtnQuanLiTuPA;
    private ArrayList<LuyenPhatAm> listTuPA;
    private TextView tvNguAm, tvNghiaTV, tvTiengAnh;
    private ImageView imvNghe;
    private Button btnContinue;

    private int currentIndex = 0;
    private ImageView imvTroVe;
    private SQLiteConnect db;
    private TextToSpeech tts;

    // Biến thành viên để lưu trạng thái admin, tránh bị mất khi quay lại Activity
    private boolean isAdmin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_luyenphatam);

        // Đọc trạng thái isAdmin MỘT LẦN và lưu vào biến thành viên
        this.isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        // Ánh xạ View
        imvTroVe = findViewById(R.id.imvTroVe);
        imbtnQuanLiTuPA = findViewById(R.id.imbtnQuanLiTuPA);
        btnContinue = findViewById(R.id.btnContinue);
        tvTiengAnh = findViewById(R.id.tvTiengAnh);
        tvNguAm = findViewById(R.id.tvNguAm);
        tvNghiaTV = findViewById(R.id.tvNghiaTV);
        imvNghe = findViewById(R.id.imvNghe);

        // Khởi tạo các đối tượng cần thiết
        tts = new TextToSpeech(this, this);
        listTuPA = new ArrayList<>();

        // Thiết lập các sự kiện click
        imvTroVe.setOnClickListener(v -> {
            startActivity(new Intent(LuyenPhatAmActivity.this, MainActivity.class));
            finish();
        });

        imbtnQuanLiTuPA.setOnClickListener(v -> {
            startActivity(new Intent(LuyenPhatAmActivity.this, QuanLiTuPaActivity.class));
        });

        imvNghe.setOnClickListener(v -> {
            if (listTuPA.isEmpty() || currentIndex >= listTuPA.size()) {
                Toast.makeText(this, "Không có từ để phát âm", Toast.LENGTH_SHORT).show();
                return;
            }
            String wordToSpeak = listTuPA.get(currentIndex).getTiengAnh();
            if (tts != null && !wordToSpeak.isEmpty()) {
                tts.speak(wordToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(this, "Không thể phát âm từ này", Toast.LENGTH_SHORT).show();
            }
        });

        btnContinue.setOnClickListener(v -> {
            currentIndex++;
            if (currentIndex >= listTuPA.size()) {
                currentIndex = 0; // Quay về từ đầu tiên
                Toast.makeText(this, "Đã hết danh sách! Quay về từ đầu.", Toast.LENGTH_SHORT).show();
            }

            // Chỉ hiển thị từ nếu danh sách không rỗng
            if (!listTuPA.isEmpty()) {
                displayCurrentWord();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 1. Kiểm tra quyền admin và cập nhật giao diện
        if (this.isAdmin) {
            imbtnQuanLiTuPA.setVisibility(View.VISIBLE);
        } else {
            imbtnQuanLiTuPA.setVisibility(View.GONE);
        }

        // 2. Tải lại dữ liệu từ CSDL
        loadDataFromDatabase();

        // 3. Cập nhật lại giao diện hiển thị từ vựng
        if (currentIndex >= listTuPA.size()) {
            currentIndex = 0; // Reset về từ đầu tiên nếu vị trí cũ không còn tồn tại
        }

        if (!listTuPA.isEmpty()) {
            displayCurrentWord();
        } else {
            // Xử lý trường hợp không có dữ liệu
            tvTiengAnh.setText("Trống");
            tvNguAm.setText("");
            tvNghiaTV.setText("Không có từ vựng");
            Toast.makeText(this, "Chưa có từ vựng nào.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDataFromDatabase() {
        if (listTuPA == null) {
            listTuPA = new ArrayList<>();
        }
        listTuPA.clear();

        // Mở, truy vấn và đóng kết nối trong cùng một phương thức
        SQLiteConnect localDb = new SQLiteConnect(this, getString(R.string.db_name), null, SQLiteConnect.DATABASE_VERSION);
        Cursor cursor = localDb.getData("SELECT * FROM tuvungPAm");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String maTu = cursor.getString(1);
            String tiengAnh = cursor.getString(2);
            String nguAm = cursor.getString(3);
            String tiengViet = cursor.getString(4);
            listTuPA.add(new LuyenPhatAm(id, maTu, tiengAnh, nguAm, tiengViet));
        }
        cursor.close();
        localDb.close();
    }

    private void displayCurrentWord() {
        if (listTuPA.isEmpty() || currentIndex >= listTuPA.size()) {
            return; // Không làm gì nếu không có dữ liệu
        }
        LuyenPhatAm tu = listTuPA.get(currentIndex);
        tvTiengAnh.setText(tu.getTiengAnh());
        tvNguAm.setText("/" + tu.getNguAm() + "/");
        tvNghiaTV.setText(tu.getTiengViet());
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Ngôn ngữ này không được hỗ trợ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Khởi tạo TextToSpeech thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
