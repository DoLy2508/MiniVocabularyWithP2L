package com.example.chuDe;

import androidx.room.Dao;
import androidx.room.Query;
import java.util.List;

@Dao
public interface VocabularyDao {
    @Query("SELECT * FROM vocabulary WHERE category = :category")
    List<Vocabulary> getWordsByCategory(String category);

    @Query("SELECT * FROM vocabulary")
    List<Vocabulary> getAllWords();

    void insert(Vocabulary vocabulary);
}