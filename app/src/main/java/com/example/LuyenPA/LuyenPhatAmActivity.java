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
    private boolean isAdmin = false;
    private Button btnStart;
    private TextView tvHint;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_luyenphatam);


        this.isAdmin = getIntent().getBooleanExtra("isAdmin", false);


        btnStart = findViewById(R.id.btnStart);
        tvHint = findViewById(R.id.tvHint);

        imvTroVe = findViewById(R.id.imvTroVe);
        imbtnQuanLiTuPA = findViewById(R.id.imbtnQuanLiTuPA);
        btnContinue = findViewById(R.id.btnContinue);
        tvTiengAnh = findViewById(R.id.tvTiengAnh);
        tvNguAm = findViewById(R.id.tvNguAm);
        tvNghiaTV = findViewById(R.id.tvNghiaTV);
        imvNghe = findViewById(R.id.imvNghe);


        tts = new TextToSpeech(this, this);
        listTuPA = new ArrayList<>();




        btnStart.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            } else {

                startListening();
            }
        });


        setupSpeechRecognizer();


        imvTroVe.setOnClickListener(v -> {
            finish();
        });

        imbtnQuanLiTuPA.setOnClickListener(v -> {

            Intent intentQuanLi = new Intent(LuyenPhatAmActivity.this, QuanLiTuPaActivity.class);
            intentQuanLi.putExtra("isAdmin", this.isAdmin);
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
                currentIndex = 0;
                Toast.makeText(this, "Đã hết danh sách! Quay về từ đầu.", Toast.LENGTH_SHORT).show();
            }


            if (!listTuPA.isEmpty()) {
                displayCurrentWord();
            }
        });
    }







    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

                tvHint.setText("Đang lắng nghe...");
                tvHint.setTextColor(Color.BLUE);
            }

            @Override
            public void onResults(Bundle results) {
                // Nhận kết quả
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String spokenText = matches.get(0);
                    checkPronunciation(spokenText);
                } else {
                    tvHint.setText("Không nhận dạng được. Thử lại!");
                    tvHint.setTextColor(Color.RED);
                }
            }

            @Override
            public void onError(int error) {

                tvHint.setText("Lỗi ghi âm, hãy thử lại!");
                tvHint.setTextColor(Color.RED);
            }

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





    private void startListening() {
        if (speechRecognizer != null) {
            speechRecognizer.startListening(speechRecognizerIntent);
        }
    }


    private void checkPronunciation(String spokenText) {
        String correctWord = tvTiengAnh.getText().toString();


        if (spokenText.trim().equalsIgnoreCase(correctWord.trim())) {
            tvHint.setText("Chính xác. Bạn đã đọc đúng: " + spokenText);
            tvHint.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            tvHint.setText("Chưa đúng. Bạn đã đọc: \"" + spokenText + "\". Hãy thử lại");
            tvHint.setTextColor(Color.RED);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startListening();
            } else {

                Toast.makeText(this, "Cần quyền ghi âm để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (this.isAdmin) {
            imbtnQuanLiTuPA.setVisibility(View.VISIBLE);
        } else {
            imbtnQuanLiTuPA.setVisibility(View.GONE);
        }


        loadDataFromDatabase();


        if (currentIndex >= listTuPA.size()) {
            currentIndex = 0;
        }

        if (!listTuPA.isEmpty()) {
            displayCurrentWord();
        } else {

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
            return;
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

        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
