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

import com.example.LuyenPA.LuyenPhatAmActivity;
import com.example.NhiemVu.NhiemVuActivity;
import com.example.gheptu.Database.SQLiteConnect;
import com.example.gheptu.GhepTuActivity;
import com.example.minivocabularywithp2l.MainActivity;
import com.example.minivocabularywithp2l.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FlashCardActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    TextView text_flashcard_front,text_flashcard_back, text_hint, text_flashcard_hint;
    Button btn_next, btn_flip, btnHoc,btnTrangChu,btnTienDo,btnLuyenNghe, btnNhiemVu;

    ImageButton btn_sound,btn_star;
    ImageView image_flashcard;

    private int currentId = -1;

    private SQLiteConnect dbflashcard;

    private Button btnGhepThe;
    private boolean isAdmin;

    private TextToSpeech tts;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chude_tuvung);
        btnGhepThe = findViewById(R.id.btnGhepThe);
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        btnGhepThe.setOnClickListener(v -> {
            Intent intentGhepTu = new Intent(FlashCardActivity.this, GhepTuActivity.class);
            intentGhepTu.putExtra("isAdmin", isAdmin);
            startActivity(intentGhepTu);

        });

        text_flashcard_front = findViewById(R.id.text_flashcard_front);
        text_flashcard_back = findViewById(R.id.text_flashcard_back);
        text_flashcard_hint = findViewById(R.id.text_flashcard_hint);
        text_hint = findViewById(R.id.text_hint);
        btn_next = findViewById(R.id.btn_next);
        btn_flip = findViewById(R.id.btn_flip);
        btn_sound = findViewById(R.id.btn_sound);
        btn_star = findViewById(R.id.btn_star);


        btnHoc = findViewById(R.id.btnHoc);
        btnNhiemVu = findViewById(R.id.btnNhiemVu);
        btnTrangChu = findViewById(R.id.btnTrangChu);
        btnTienDo = findViewById(R.id.btnTienDo);
        btnLuyenNghe = findViewById(R.id.btnLuyenNghe);
        image_flashcard = findViewById(R.id.image_flashcard);



            dbflashcard = new SQLiteConnect(this);


        loadRandomFlashcard();


        tts = new TextToSpeech(this, this);

        btn_next.setOnClickListener(v -> loadRandomFlashcard());


        text_hint.setOnClickListener(v -> {
            if (text_flashcard_hint.getVisibility() == View.VISIBLE) {
                text_flashcard_hint.setVisibility(View.GONE);
                text_hint.setText("Hiển thị gợi ý");
            } else {

                String hint = text_flashcard_hint.getText().toString().trim();
                if (!hint.isEmpty()) {
                    text_flashcard_hint.setVisibility(View.VISIBLE);
                    text_hint.setText("Ẩn gợi ý");
                } else {
                    Toast.makeText(FlashCardActivity.this, "Không có gợi ý cho từ này", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_sound.setOnClickListener(v -> {
            String word = getCurrentWord();
            if (!word.isEmpty()) {
                tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(this, "Không có từ để phát âm", Toast.LENGTH_SHORT).show();
            }
        });



        btn_flip.setOnClickListener(v -> {
            if (text_flashcard_front.getVisibility() == View.VISIBLE) {
                text_flashcard_front.setVisibility(View.GONE);
                text_flashcard_back.setVisibility(View.VISIBLE);
            } else {
                text_flashcard_front.setVisibility(View.VISIBLE);
                text_flashcard_back.setVisibility(View.GONE);
            }

        });



        btnHoc.setOnClickListener(v -> {

            Toast.makeText(FlashCardActivity.this, "Đang ở chế độ học", Toast.LENGTH_SHORT).show();
        });


        btnTienDo.setOnClickListener(v -> {
            Intent intent = new Intent(FlashCardActivity.this, NhiemVuActivity.class);
            intent.putExtra("isAdmin", isAdmin);
            startActivity(intent);
        });

        btnLuyenNghe.setOnClickListener(v -> {
            Intent intent = new Intent(FlashCardActivity.this, LuyenPhatAmActivity.class);
            intent.putExtra("isAdmin", isAdmin);
            startActivity(intent);
        });

        btnNhiemVu.setOnClickListener(v -> {
            Intent intent = new Intent(FlashCardActivity.this, NhiemVuActivity.class);
            intent.putExtra("isAdmin", isAdmin);
            startActivity(intent);
        });

        btnTrangChu.setOnClickListener(v -> {
            Intent intent = new Intent(FlashCardActivity.this, MainActivity.class);
            intent.putExtra("isAdmin", isAdmin);
            startActivity(intent);
        });


    }


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


    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();

    }


    private void loadRandomFlashcard() {
        Cursor cursor = dbflashcard.getRandomFlashcard();
        if (cursor.moveToFirst()) {
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

            String imageName = "";
            if (imageIndex != -1) {
                imageName = cursor.getString(imageIndex);
                if (imageName == null) imageName = "";
            }


            text_flashcard_front.setText(word);
            text_flashcard_back.setText(meaning);
            text_flashcard_hint.setText(hint.trim());


            if (!imageName.isEmpty()) {
                int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                if (resId != 0) {
                    image_flashcard.setImageResource(resId);
                    image_flashcard.setVisibility(View.VISIBLE);
                } else {
                    image_flashcard.setVisibility(View.GONE);
                }
            } else {
                image_flashcard.setVisibility(View.GONE);
            }



            text_flashcard_front.setVisibility(View.VISIBLE);
            text_flashcard_back.setVisibility(View.GONE);


        } else {

            text_flashcard_front.setText("Không có flashcard!");
            text_flashcard_back.setText("");
            text_flashcard_hint.setText("");
            image_flashcard.setVisibility(View.GONE);
            currentId = -1;
        }
        cursor.close();



        btn_star.setOnClickListener(v -> {
            if (currentId == -1) return;

            boolean isCurrentlyStarred = isCurrentStarred();
            boolean newStarredState = !isCurrentlyStarred;


            dbflashcard.updateStarred(currentId, newStarredState);


            if (newStarredState) {
                btn_star.setImageResource(R.drawable.sao_day);
            } else {
                btn_star.setImageResource(R.drawable.sao_rong);
            }
        });


        boolean isStarred = isCurrentStarred();
        if (isStarred) {
            btn_star.setImageResource(R.drawable.sao_day);
        } else {
            btn_star.setImageResource(R.drawable.sao_rong);
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
}





