package com.example.NhiemVu;

public class NhiemVu {
    private int id; // ID để định danh trong Database
    private String title;
    private String description;
    private boolean isCompleted;

    // Constructor dùng khi thêm mới (chưa có ID)
    public NhiemVu(String title, String description, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    // Constructor đầy đủ dùng khi lấy từ DB ra
    public NhiemVu(int id, String title, String description, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}