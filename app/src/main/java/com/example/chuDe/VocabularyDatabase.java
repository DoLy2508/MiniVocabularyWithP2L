package com.example.chuDe;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Vocabulary.class}, version = 1, exportSchema = false)
public abstract class VocabularyDatabase extends RoomDatabase {
    public abstract VocabularyDao vocabularyDao();

    private static volatile VocabularyDatabase INSTANCE;

    public static VocabularyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VocabularyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VocabularyDatabase.class, "vocabulary_database")
                            .allowMainThreadQueries() // ⚠️ Chỉ dùng để học, không dùng trong app thật
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}