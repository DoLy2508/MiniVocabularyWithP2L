package com.example.gheptu.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.NhiemVu.NhiemVu;

import java.util.ArrayList;

public class SQLiteConnect extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HocTiengAnh.db";
    public static final int DATABASE_VERSION = 3; // Tăng version để cập nhật bảng mới

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
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTableUsers);

        // 2. Tạo bảng Tasks
        String createTableTasks = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_TITLE + " TEXT, " +
                COLUMN_TASK_DESC + " TEXT, " +
                COLUMN_TASK_COMPLETED + " INTEGER DEFAULT 0)";
        db.execSQL(createTableTasks);
        
        // 3. Tạo bảng Từ Vựng Ghép Từ (Code cũ của bạn)
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
                        "tiengViet TEXT," +
                        "tenAudio TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp version (xóa bảng cũ tạo lại hoặc alter table)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUVUNG_GT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUVUNG_PA);
        onCreate(db);
    }

    // --- CÁC HÀM HỖ TRỢ CHUNG (Code cũ) ---
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

                NhiemVu task = new NhiemVu(id, title, desc, isCompleted);
                taskList.add(task);
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
}