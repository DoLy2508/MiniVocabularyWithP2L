package com.example.chuDe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vocabulary")
public class Vocabulary {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String word;        // Từ tiếng Anh
    public String meaning;     // Nghĩa tiếng Việt
    public String category;    // Chủ đề (Animals, Food, ...)

    // Constructor không tham số (bắt buộc khi dùng Room với Java)
    public Vocabulary() {}

    // Constructor có tham số
    public Vocabulary(String word, String meaning, String category) {
        this.word = word;
        this.meaning = meaning;
        this.category = category;
    }
}