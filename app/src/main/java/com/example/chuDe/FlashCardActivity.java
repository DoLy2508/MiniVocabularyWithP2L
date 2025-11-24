package com.example.chuDe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gheptu.GhepTuActivity;
import com.example.minivocabularywithp2l.R;

import java.util.List;

public class FlashCardActivity extends AppCompatActivity {
    TextView text_flashcard;
    Button btn_next;
    ImageButton btn_sound,btn_star;
    private List<Vocabulary> wordList;
    private int currentIndex = 0;
    private boolean isShowingMeaning = false;

    private Button btnGhepThe;

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
        text_flashcard = findViewById(R.id.text_flashcard);
        btn_next = findViewById(R.id.btn_next);
        btn_sound = findViewById(R.id.btn_sound);
        btn_star = findViewById(R.id.btn_star);

        // LẤY DỮ LIỆU TỪ CSDL (chủ đề: "Animals")
        VocabularyDatabase db = VocabularyDatabase.getDatabase(this);
        wordList = db.vocabularyDao().getWordsByCategory("Animals");

        // 💡 Nếu chưa có dữ liệu → chèn dữ liệu mẫu (chỉ chạy 1 lần)
        if (wordList.isEmpty()) {
            // Chèn dữ liệu mẫu
            db.vocabularyDao().insert(new Vocabulary("dog", "con chó", "Animals"));
            db.vocabularyDao().insert(new Vocabulary("cat", "con mèo", "Animals"));
            db.vocabularyDao().insert(new Vocabulary("elephant", "con voi", "Animals"));
            // Lấy lại sau khi chèn
            wordList = db.vocabularyDao().getWordsByCategory("Animals");
        }

        // Hiển thị từ đầu tiên
        showCurrentWord();

        // Xử lý lật thẻ
        text_flashcard.setOnClickListener(v -> {
            isShowingMeaning = !isShowingMeaning;
            showCurrentWord();
        });

        // Xử lý nút "Tiếp tục"
        btn_next.setOnClickListener(v -> {
            isShowingMeaning = false;
            currentIndex = (currentIndex + 1) % wordList.size();
            showCurrentWord();
        });

        // (Tùy chọn) Xử lý nút loa, sao...
        btn_sound.setOnClickListener(v -> {
            // Bạn có thể thêm phát âm sau (dùng TextToSpeech)
        });


    }

    private void showCurrentWord() {
        if (wordList.isEmpty()) {
            text_flashcard.setText("Không có từ nào!");
            return;
        }
        Vocabulary current = wordList.get(currentIndex);
        text_flashcard.setText(isShowingMeaning ? current.meaning : current.word);

    }
}
