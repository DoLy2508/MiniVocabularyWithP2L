package com.example.chuDe;

import android.annotation.SuppressLint;
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

import com.example.gheptu.Database.SQLiteConnect;
import com.example.gheptu.GhepTuActivity;
import com.example.minivocabularywithp2l.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FlashCardActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    TextView text_flashcard_front,text_flashcard_back, text_hint, text_flashcard_hint;
    Button btn_next, btn_flip;
    ImageButton btn_sound,btn_star;
    ImageView image_flashcard;

    private int currentId = -1;
    private String currentTopic = "Animals"; // Chủ đề mặc định

    private SQLiteConnect dbflashcard;

    private Button btnGhepThe;
    private TextToSpeech tts; // <-- THÊM DÒNG NÀY

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chude_tuvung);

        // NHẬN DỮ LIỆU CHỦ ĐỀ TỪ CHUDEACTIVITY
        String topic = getIntent().getStringExtra("TOPIC_NAME");
        if (topic != null && !topic.isEmpty()) {
            currentTopic = topic;
        }

        btnGhepThe = findViewById(R.id.btnGhepThe);
        btnGhepThe.setOnClickListener(v -> {
            startActivity(new Intent(FlashCardActivity.this, GhepTuActivity.class));
            finish();

        });
        // ánh xạ view
        text_flashcard_front = findViewById(R.id.text_flashcard_front);
        text_flashcard_back = findViewById(R.id.text_flashcard_back);
        text_flashcard_hint = findViewById(R.id.text_flashcard_hint);
        text_hint = findViewById(R.id.text_hint);
        btn_next = findViewById(R.id.btn_next);
        btn_flip = findViewById(R.id.btn_flip);
        btn_sound = findViewById(R.id.btn_sound);
        btn_star = findViewById(R.id.btn_star);
        image_flashcard = findViewById(R.id.image_flashcard);


        // Khởi tạo DatabaseHelper
            dbflashcard = new SQLiteConnect(this);


        // Tải flashcard đầu tiên theo chủ đề
        loadRandomFlashcard();

        // KHỞI TẠO TEXT-TO-SPEECH
        tts = new TextToSpeech(this, this); // this = OnInitListener

        // Nút Tiếp tục
        btn_next.setOnClickListener(v -> loadRandomFlashcard());

        // thêm sự kiện click cho text_hint ( hển th gợi  )
        text_hint.setOnClickListener(v -> {
            if (text_flashcard_hint.getVisibility() == View.VISIBLE) {
                text_flashcard_hint.setVisibility(View.GONE);
                text_hint.setText("Hiển thị gợi ý");
            } else {
                // Chỉ hiện nếu có nội dung hint
                String hint = text_flashcard_hint.getText().toString().trim();
                if (!hint.isEmpty()) {
                    text_flashcard_hint.setVisibility(View.VISIBLE);
                    text_hint.setText("Ẩn gợi ý");
                } else {
                    Toast.makeText(FlashCardActivity.this, "Không có gợi ý cho từ này", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // NÚT LOA - DÙNG TTS ĐỂ PHÁT ÂM
        btn_sound.setOnClickListener(v -> {
            String word = getCurrentWord();
            if (!word.isEmpty()) {
                tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(this, "Không có từ để phát âm", Toast.LENGTH_SHORT).show();
            }
        });


        //Xử lý nút lật thẻ
        btn_flip.setOnClickListener(v -> {
            if (text_flashcard_front.getVisibility() == View.VISIBLE) {
                text_flashcard_front.setVisibility(View.GONE);
                text_flashcard_back.setVisibility(View.VISIBLE);
            } else {
                text_flashcard_front.setVisibility(View.VISIBLE);
                text_flashcard_back.setVisibility(View.GONE);
            }
//            updateHintVisibility(); // <-- thêm dòng này
        });

    }

    //  TRIỂN KHAI OnInitListener
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US); // Giọng Anh-Mỹ
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Giọng nói không hỗ trợ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Lỗi khởi tạo TTS", Toast.LENGTH_SHORT).show();
        }
    }

    //  GIẢI PHÓNG TTS KHI THOÁT ACTIVITY
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
        // Đóng DB nếu cần (tùy SQLiteConnect của bạn có tự đóng không)
    }


    private void loadRandomFlashcard() {
        // Thay đổi: Lấy từ vựng theo chủ đề
        Cursor cursor = dbflashcard.getRandomFlashcardByTopic(currentTopic);
        
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int wordIndex = cursor.getColumnIndex("word");
            int meaningIndex = cursor.getColumnIndex("meaning");
            int hintIndex = cursor.getColumnIndex("hint");
            int imageIndex = cursor.getColumnIndex("image");

            if (idIndex == -1 || wordIndex == -1 || meaningIndex == -1) {
                Toast.makeText(this, "Lỗi cơ sở dữ liệu!", Toast.LENGTH_LONG).show();
                cursor.close();
                return;
            }

            currentId = cursor.getInt(idIndex);
            String word = cursor.getString(wordIndex);
            String meaning = cursor.getString(meaningIndex);
            String hint = "";
            if (hintIndex != -1) {
                hint = cursor.getString(hintIndex);
                if (hint == null) hint = "";
            }
            // XỬ LÝ ẢNH
            String imageName = "";
            if (imageIndex != -1) {
                imageName = cursor.getString(imageIndex);
                if (imageName == null) imageName = "";
            }

            // gán text
            text_flashcard_front.setText(word);
            text_flashcard_back.setText(meaning);
            text_flashcard_hint.setText(hint.trim()); // setText("") nếu rỗng

            //  HIỂN THỊ ẢNH NẾU CÓ
            if (!imageName.isEmpty()) {
                int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                if (resId != 0) {
                    image_flashcard.setImageResource(resId);
                    image_flashcard.setVisibility(View.VISIBLE);
                } else {
                    image_flashcard.setVisibility(View.GONE); // không tìm thấy ảnh
                }
            } else {
                image_flashcard.setVisibility(View.GONE);
            }


            // Luôn bắt đầu ở mặt trước, ẩn mặt sau
            text_flashcard_front.setVisibility(View.VISIBLE);
            text_flashcard_back.setVisibility(View.GONE);

            // KHÔNG tự động hiển thị hint nữa → để người dùng tự nhấn
        } else {
            // Không có flashcard cho chủ đề này
            text_flashcard_front.setText("Không có từ vựng\ncho chủ đề: " + currentTopic);
            text_flashcard_back.setText("");
            text_flashcard_hint.setText(""); // rõ ràng: không có hint
            image_flashcard.setVisibility(View.GONE); // ẩn ảnh
            currentId = -1;
        }
        if(cursor != null) cursor.close();
    }


    private boolean isCurrentStarred() {
        if (currentId == -1) return false;
        Cursor cursor = dbflashcard.getFlashcardById(currentId);
        if (cursor != null && cursor.moveToFirst()) {
            boolean starred = cursor.getInt(cursor.getColumnIndexOrThrow("is_starred")) == 1;
            cursor.close();
            return starred;
        }
        if(cursor != null) cursor.close();
        return false;
    }

    private String getCurrentWord() {
        if (currentId == -1) return "";
        Cursor cursor = dbflashcard.getFlashcardById(currentId);
        if (cursor != null && cursor.moveToFirst()) {
            String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
            cursor.close();
            return word;
        }
        if(cursor != null) cursor.close();
        return "";
    }
}
