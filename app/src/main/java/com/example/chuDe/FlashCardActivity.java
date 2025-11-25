package com.example.chuDe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gheptu.Database.SQLiteConnect;
import com.example.gheptu.GhepTuActivity;
import com.example.minivocabularywithp2l.R;

import java.io.IOException;
import java.util.List;

public class FlashCardActivity extends AppCompatActivity {
    TextView text_flashcard_front,text_flashcard_back, text_hint, text_flashcard_hint;
    Button btn_next, btn_flip;
    ImageButton btn_sound,btn_star;

    private int currentId = -1;

    private SQLiteConnect dbflashcard;

    private Button btnGhepThe;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chude_tuvung);
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

        // Khởi tạo DatabaseHelper
            dbflashcard = new SQLiteConnect(this);


        // Tải flashcard đầu tiên
        loadRandomFlashcard();

        // Nút Tiếp tục
        btn_next.setOnClickListener(v -> loadRandomFlashcard());

        // Nút sao
//        btn_star.setOnClickListener(v -> {
//            if (currentId != -1) {
//                boolean isStarred = !isCurrentStarred();
//                dbflashcard.updateStarred(currentId, isStarred);
//                Toast.makeText(this, isStarred ? "Đã đánh dấu yêu thích" : "Bỏ đánh dấu", Toast.LENGTH_SHORT).show();
//                loadRandomFlashcard(); // Tải lại để cập nhật icon sao
//            }
//        });

        // Nút loa (giả lập)
        btn_sound.setOnClickListener(v -> {
            if (currentId != -1) {
                Toast.makeText(this, "Phát âm: " + getCurrentWord(), Toast.LENGTH_SHORT).show();
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
            updateHintVisibility(); // <-- thêm dòng này
        });
    }

    private void loadRandomFlashcard() {
        Cursor cursor = dbflashcard.getRandomFlashcard();
        String hint = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int wordIndex = cursor.getColumnIndex("word");
            int meaningIndex = cursor.getColumnIndex("meaning");
            int hintIndex = cursor.getColumnIndex("hint");

            if (idIndex == -1 || wordIndex == -1 || meaningIndex == -1) {
                // Bảng bị thiếu cột → không nên xảy ra nếu schema đúng
                Toast.makeText(this, "Lỗi cơ sở dữ liệu!", Toast.LENGTH_LONG).show();
                cursor.close();
                return;
            }

            currentId = cursor.getInt(idIndex);
            String word = cursor.getString(wordIndex);
            String meaning = cursor.getString(meaningIndex);
            hint = hintIndex != -1 ? cursor.getString(hintIndex) : "Hiển thị gợi ý";

            text_flashcard_front.setText(word);
            text_flashcard_back.setText(meaning);
            text_flashcard_hint.setText(hint != null && !hint.trim().isEmpty() ? hint : "");

            text_flashcard_front.setVisibility(View.VISIBLE);
            text_flashcard_back.setVisibility(View.GONE);

            // Cập nhật visibility của hint
            updateHintVisibility();
        } else {
            text_flashcard_front.setText("Không có flashcard!");
            text_flashcard_back.setText("");
            text_flashcard_hint.setText(hint != null && !hint.trim().isEmpty() ? hint : "");
            currentId = -1;
            // Dù không có dữ liệu, vẫn cập nhật visibility
            updateHintVisibility();
        }
        cursor.close();
    }

    private void updateHintVisibility() {
        boolean isFrontVisible = (text_flashcard_front.getVisibility() == View.VISIBLE);
        String hint = text_flashcard_hint.getText().toString().trim();

        // Chỉ hiển thị hint nếu:
        // - Đang ở mặt trước
        // - Và hint không rỗng
        if (isFrontVisible && !hint.isEmpty()) {
            text_flashcard_hint.setVisibility(View.VISIBLE);
        } else {
            text_flashcard_hint.setVisibility(View.GONE);
        }
    }

    private boolean isCurrentStarred() {
        if (currentId == -1) return false;
        Cursor cursor = dbflashcard.getFlashcardById(currentId);
        if (cursor.moveToFirst()) {
            boolean starred = cursor.getInt(cursor.getColumnIndexOrThrow("is_starred")) == 1;
            cursor.close();
            return starred;
        }
        cursor.close();
        return false;
    }

    private String getCurrentWord() {
        if (currentId == -1) return "";
        Cursor cursor = dbflashcard.getFlashcardById(currentId);
        if (cursor.moveToFirst()) {
            String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
            cursor.close();
            return word;
        }
        cursor.close();
        return "";
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        dbflashcard.closeDatabase();
//    }
}



