package com.example.gheptu.Model;

import java.io.Serializable;

public class TuVungGhepTu implements Serializable {
    int id;
    String maTu;
    String tiengAnh;
    String tiengViet;
    public TuVungGhepTu(){
    }

    public TuVungGhepTu(int id, String maTu, String tiengAnh, String tiengViet) {
        this.id = id;
        this.maTu = maTu;
        this.tiengAnh = tiengAnh;
        this.tiengViet = tiengViet;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaTu() {
        return maTu;
    }

    public void setMaTu(String maTu) {

        this.maTu = maTu;
    }

    public String getTiengAnh() {
        return tiengAnh;
    }

    public void setTiengAnh(String tiengAnh) {
        this.tiengAnh = tiengAnh;
    }

    public String getTiengViet() {
        return tiengViet;
    }

    public void setTiengViet(String tiengViet) {
        this.tiengViet = tiengViet;
    }
}
