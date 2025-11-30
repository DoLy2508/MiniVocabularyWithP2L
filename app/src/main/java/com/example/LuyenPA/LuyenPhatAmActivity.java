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

// Thêm các import cần thiết cho Speech Recognition
import android.Manifest;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.LuyenPA.Model.LuyenPhatAm;
import com.example.NhiemVu.NhiemVuActivity;
import com.example.chuDe.ChuDeActivity;
import com.example.gheptu.Database.SQLiteConnect;
import com.example.gheptu.GhepTuActivity;
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
    
    // Menu dưới
    private ImageButton imbtnHome, imbtnHoc, imbtnCheckList, imbtnGame;

    // Biến thành viên để lưu trạng thái admin, tránh bị mất khi quay lại Activity
    private boolean isAdmin = false;
    private Button btnStart; // Thêm nút Start
    private TextView tvHint; // Thêm TextView Hint để hiển thị kết quả
    // === Biến cho Speech Recognition ===
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_luyenphatam);

        // Đọc trạng thái isAdmin MỘT LẦN và lưu vào biến thành viên
        this.isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        // --- Ánh xạ các View mới ---
        btnStart = findViewById(R.id.btnStart);
        tvHint = findViewById(R.id.tvHint);
        // Ánh xạ View
        imvTroVe = findViewById(R.id.imvTroVe);
        imbtnQuanLiTuPA = findViewById(R.id.imbtnQuanLiTuPA);
        btnContinue = findViewById(R.id.btnContinue);
        tvTiengAnh = findViewById(R.id.tvTiengAnh);
        tvNguAm = findViewById(R.id.tvNguAm);
        tvNghiaTV = findViewById(R.id.tvNghiaTV);
        imvNghe = findViewById(R.id.imvNghe);

        // Ánh xạ Menu dưới
        imbtnHome = findViewById(R.id.imbtnHome);
        imbtnHoc = findViewById(R.id.imbtnHoc);
        imbtnCheckList = findViewById(R.id.imbtnCheckList);
        imbtnGame = findViewById(R.id.imbtnGame);

        // Khởi tạo các đối tượng cần thiết
        tts = new TextToSpeech(this, this);
        listTuPA = new ArrayList<>();



        // --- Thiết lập sự kiện cho btnStart ---
        btnStart.setOnClickListener(v -> {
            // 1. Kiểm tra quyền ghi âm
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // Nếu chưa có quyền, yêu cầu người dùng cấp quyền
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            } else {
                // Nếu đã có quyền, bắt đầu ghi âm
                startListening();
            }
        });

        // --- Khởi tạo Speech Recognizer ---
        setupSpeechRecognizer();

        // Thiết lập các sự kiện click
        imvTroVe.setOnClickListener(v -> {
            finish();
        });

        imbtnQuanLiTuPA.setOnClickListener(v -> {
            // Chuyển sang QuanLiTuPaActivity và truyền quyền admin đi
            Intent intentQuanLi = new Intent(LuyenPhatAmActivity.this, QuanLiTuPaActivity.class);
            intentQuanLi.putExtra("isAdmin", this.isAdmin); // Luôn truyền trạng thái đi tiếp
            startActivity(intentQuanLi);
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
        
        // Xử lý sự kiện Menu dưới
        setupBottomMenu();
    }
    
    private void setupBottomMenu() {
        imbtnHome.setOnClickListener(v -> {
            Intent intent = new Intent(LuyenPhatAmActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        imbtnHoc.setOnClickListener(v -> {
            Intent intent = new Intent(LuyenPhatAmActivity.this, ChuDeActivity.class);
            startActivity(intent);
        });

        imbtnCheckList.setOnClickListener(v -> {
            Intent intent = new Intent(LuyenPhatAmActivity.this, NhiemVuActivity.class);
            startActivity(intent);
        });

        imbtnGame.setOnClickListener(v -> {
            Intent intent = new Intent(LuyenPhatAmActivity.this, GhepTuActivity.class);
            startActivity(intent);
        });
    }


    // --- CÁC PHƯƠNG THỨC MỚI CHO SPEECH RECOGNITION ---


     //Khởi tạo SpeechRecognizer và Intent

    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString()); // Nhận dạng tiếng Anh-Mỹ

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                // Sẵn sàng ghi âm, cập nhật giao diện
                tvHint.setText("Đang lắng nghe...");
                tvHint.setTextColor(Color.BLUE);
            }

            @Override
            public void onResults(Bundle results) {
                // Nhận kết quả
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String spokenText = matches.get(0); // Lấy kết quả có độ chính xác cao nhất
                    checkPronunciation(spokenText);
                } else {
                    tvHint.setText("Không nhận dạng được. Thử lại!");
                    tvHint.setTextColor(Color.RED);
                }
            }

            @Override
            public void onError(int error) {
                // Xử lý lỗi
                tvHint.setText("Lỗi ghi âm, hãy thử lại!");
                tvHint.setTextColor(Color.RED);
            }
            // Các phương thức khác có thể để trống
            @Override
            public void onBeginningOfSpeech() {}
            @Override
            public void onEndOfSpeech() {}
            @Override
            public void onRmsChanged(float rmsdB) {}
            @Override
            public void onBufferReceived(byte[] buffer) {}
            @Override
            public void onPartialResults(Bundle partialResults) {}
            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
    }



      //Bắt đầu quá trình lắng nghe giọng nói

    private void startListening() {
        if (speechRecognizer != null) {
            speechRecognizer.startListening(speechRecognizerIntent);
        }
    }

    //So sánh từ được nói và từ gốc, sau đó cập nhật giao diện
     //@param spokenText Từ mà người dùng đã nói

    private void checkPronunciation(String spokenText) {
        String correctWord = tvTiengAnh.getText().toString();

        // So sánh không phân biệt chữ hoa/thường và bỏ qua khoảng trắng thừa
        if (spokenText.trim().equalsIgnoreCase(correctWord.trim())) {
            tvHint.setText("Chính xác! Bạn đã đọc đúng: " + spokenText);
            tvHint.setTextColor(Color.parseColor("#4CAF50")); // Màu xanh lá
        } else {
            tvHint.setText("Chưa đúng. Bạn đã đọc: \"" + spokenText + "\". Hãy thử lại!");
            tvHint.setTextColor(Color.RED);
        }
    }
    //Xử lý kết quả sau khi người dùng cấp (hoặc từ chối) quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền, bắt đầu ghi âm
                startListening();
            } else {
                // Người dùng từ chối, thông báo cho họ
                Toast.makeText(this, "Cần quyền ghi âm để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
            }
        }
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
        // --- Giải phóng SpeechRecognizer khi Activity bị hủy ---
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
