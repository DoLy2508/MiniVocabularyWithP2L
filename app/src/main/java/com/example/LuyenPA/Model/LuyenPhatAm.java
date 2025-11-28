package com.example.LuyenPA.Model;

import java.io.Serializable;

public class LuyenPhatAm implements Serializable {
    private int id;
    private String maTu;
    private String nguAm;
    private String tiengAnh;
    private String tiengViet;



    public LuyenPhatAm(){ }


    public LuyenPhatAm(int id, String maTu, String tiengAnh, String nguAm,
                       String tiengViet) {
        this.id = id;
        this.maTu = maTu;
        this.tiengAnh = tiengAnh;
        this.nguAm = nguAm;
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
    public String getNguAm() {
        return nguAm;
    }
    public void setNguAm(String nguAm) {
        this.nguAm = nguAm;
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
