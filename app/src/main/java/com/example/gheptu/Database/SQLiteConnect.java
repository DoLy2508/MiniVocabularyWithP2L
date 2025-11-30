package com.example.gheptu.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.NhiemVu.NhiemVu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SQLiteConnect extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HocTiengAnh.db";
    public static final int DATABASE_VERSION = 8; // Tăng version để cập nhật bảng mới (thêm cột topic cho flashcards)

    // Bảng Users (Đăng Ký)
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Bảng Tasks (Nhiệm vụ)
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_TITLE = "title";
    public static final String COLUMN_TASK_DESC = "description";
    public static final String COLUMN_TAaSK_COMPLETED = "is_completed";
    
    // Bảng Từ vựng Ghép từ (giữ lại code cũ của bạn)
    public static final String TABLE_TUVUNG_GT = "tuvungGT_v2";

    // Bang tu vung phat am
    public static final String TABLE_TUVUNG_PA = "tuvungPAm";
    private static final String COLUMN_TASK_COMPLETED = "is_completed";

    public SQLiteConnect(@Nullable Context context) {
        // Constructor mặc định cho việc sử dụng chung
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    // Giữ lại constructor cũ nếu có chỗ nào đang dùng (để tránh lỗi code cũ)
    public SQLiteConnect(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name != null ? name : DATABASE_NAME, factory, version >= 1 ? version : DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Tạo bảng Users
        String createTableUsers = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                "role TEXT DEFAULT 'user')";
        db.execSQL(createTableUsers);

        //Chèn admin (chỉ 1 lần)
        db.execSQL("INSERT OR IGNORE INTO users (email, password, role) VALUES ('adminp2l@gmail.com', '123456A@', 'admin')");

        // 2. Tạo bảng Tasks
        String createTableTasks = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_TITLE + " TEXT, " +
                COLUMN_TASK_DESC + " TEXT, " +
                COLUMN_TASK_COMPLETED + " INTEGER DEFAULT 0)";
        db.execSQL(createTableTasks);
        
        // 3. Tạo bảng Từ Vựng Ghép Từ
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_TUVUNG_GT + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "maTu TEXT NOT NULL," +
                        "tiengAnh TEXT NOT NULL," +
                        "tiengViet TEXT NOT NULL)"
        );

        // 3. Tạo bảng Từ cung phat am
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_TUVUNG_PA + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "maTu TEXT," +
                        "tiengAnh TEXT," +
                        "nguAm TEXT," +
                        "tiengViet TEXT)"
        );
        // 4.Phương : Tạo bảng flashcard (Đã thêm cột topic)
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS flashcards (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "word TEXT NOT NULL," +
                        "meaning TEXT NOT NULL," +
                        "hint TEXT," +
                        "audio_path TEXT," +
                        "is_starred INTEGER DEFAULT 0," +
                        "last_reviewed TEXT," +
                        "image TEXT," +
                        "topic TEXT DEFAULT 'General')" // Thêm cột topic
        );
        //  CHÈN DỮ LIỆU MẪU VÀO BẢNG flashcards
        insertSampleFlashcards(db);

    }

    private void insertSampleFlashcards(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // Animals
        values.put("word", "Cat");
        values.put("meaning", "Con mèo");
        values.put("hint", "Meow meow");
        values.put("image", "animals"); 
        values.put("topic", "Animals");
        db.insert("flashcards", null, values);
        
        values.put("word", "Dog");
        values.put("meaning", "Con chó");
        values.put("hint", "Gâu gâu");
        values.put("image", "animals");
        values.put("topic", "Animals");
        db.insert("flashcards", null, values);

        // Food
        values.put("word", "Apple");
        values.put("meaning", "Quả táo");
        values.put("hint", "Red fruit");
        values.put("image", "food");
        values.put("topic", "Food");
        db.insert("flashcards", null, values);
        
        values.put("word", "Bread");
        values.put("meaning", "Bánh mì");
        values.put("hint", "Ăn sáng");
        values.put("image", "food");
        values.put("topic", "Food");
        db.insert("flashcards", null, values);
        
        // Travel
        values.put("word", "Passport");
        values.put("meaning", "Hộ chiếu");
        values.put("hint", "Cần để đi nước ngoài");
        values.put("image", "travel");
        values.put("topic", "Travel");
        db.insert("flashcards", null, values);
        
        // Jobs
        values.put("word", "Doctor");
        values.put("meaning", "Bác sĩ");
        values.put("hint", "Chữa bệnh");
        values.put("image", "jobs");
        values.put("topic", "Jobs");
        db.insert("flashcards", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp version (xóa bảng cũ tạo lại hoặc alter table)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUVUNG_GT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUVUNG_PA);
        db.execSQL("DROP TABLE IF EXISTS flashcards");
        onCreate(db);
    }

    // --- CÁC HÀM HỖ TRỢ CHUNG ---
    // truy vấn không trả kết quả: CREATE, INSERT, UPDATE,...
    public void queryData(String query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    // truy vấn trả về kết quả: SELECT,...
    public Cursor getData(String query) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query, null);
    }


    // --- QUẢN LÝ USER (ĐĂNG KÝ/ĐĂNG NHẬP) ---

    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, password);
        
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // --- QUẢN LÝ TASKS (NHIỆM VỤ) ---

    // Thêm nhiệm vụ mới
    public boolean addTask(NhiemVu task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TASK_TITLE, task.getTitle());
        cv.put(COLUMN_TASK_DESC, task.getDescription());
        cv.put(COLUMN_TASK_COMPLETED, task.isCompleted() ? 1 : 0);
        long result = db.insert(TABLE_TASKS, null, cv);
        return result != -1;
    }

    // Lấy danh sách tất cả nhiệm vụ
    public ArrayList<NhiemVu> getAllTasks() {
        ArrayList<NhiemVu> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TITLE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESC));
                boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_COMPLETED)) == 1;
                taskList.add(new NhiemVu(id, title, desc, isCompleted));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return taskList;
    }

    // Cập nhật nhiệm vụ
    public boolean updateTask(NhiemVu task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TASK_TITLE, task.getTitle());
        cv.put(COLUMN_TASK_DESC, task.getDescription());
        cv.put(COLUMN_TASK_COMPLETED, task.isCompleted() ? 1 : 0);
        int rowsAffected = db.update(TABLE_TASKS, cv, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(task.getId())});
        return rowsAffected > 0;
    }

    // Xóa nhiệm vụ
    public boolean deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_TASKS, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    // quản lý flashcard

    public void updateStarred(int currentId, boolean isStarred) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("is_starred", isStarred ? 1 : 0);
        db.update("flashcards", cv, "id = ?", new String[]{String.valueOf(currentId)});
    }


    // --- TIẾN ĐỘ HỌC TẬP ---

    // Đánh dấu từ đã học (cập nhật last_reviewed)
    public void markAsLearned(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        cv.put("last_reviewed", currentDate);
        db.update("flashcards", cv, "id = ?", new String[]{String.valueOf(id)});
    }

    public int getTotalCountByTopic(String topic) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM flashcards WHERE topic = ?", new String[]{topic});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // Lấy số từ đã học theo chủ đề (last_reviewed IS NOT NULL)
    public int getLearnedCountByTopic(String topic) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM flashcards WHERE topic = ? AND last_reviewed IS NOT NULL", new String[]{topic});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    public Cursor getRandomFlashcard() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM flashcards ORDER BY RANDOM() LIMIT 1", null);
    }
    public Cursor getRandomFlashcardByTopic(String topic) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM flashcards WHERE topic = ? ORDER BY RANDOM() LIMIT 1", new String[]{topic});
    }

    public Cursor getFlashcardById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM flashcards WHERE id = ?", new String[]{String.valueOf(id)});
    }

    public boolean isAdmin(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT role FROM users WHERE email = ?",
                new String[]{email}
        );

        boolean isAdmin = false;
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            isAdmin = "admin".equalsIgnoreCase(role);
        }
        cursor.close();
        return isAdmin;
    }

    public boolean updatePassword(String email, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PASSWORD, newPass);
        int rows = db.update(TABLE_USERS, cv, COLUMN_EMAIL + " = ?", new String[]{email});
        return rows > 0;
    }
}
