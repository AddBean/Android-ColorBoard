package com.addbean.colorboard;

/**
 * Created by AddBean on 2016/7/30.
 */
public class ColorMate {
    private int mColumn;
    private int mLap;
    private String mName;
    private int mColor;

    public ColorMate(int mLap, int mColumn, int mColor, String mName) {
        this.mColumn = mColumn;
        this.mLap = mLap;
        this.mName = mName;
        this.mColor = mColor;
    }

    public int getmColumn() {
        return mColumn;
    }

    public void setmColumn(int mColumn) {
        this.mColumn = mColumn;
    }

    public int getmLap() {
        return mLap;
    }

    public void setmLap(int mLap) {
        this.mLap = mLap;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }
}
