package com.addbean.colorboard.items;

import android.graphics.RectF;

/**
 * Created by AddBean on 2016/7/29.
 */
public class ItemMate {
    public static final int SELECTED_FALSE = 0;
    public static final int SELECTED_TRUE = 1;
    public static final int SELECTED_MARKED = 2;
    private int mLap;//第几圈;
    private int mColumn;//第几列;
    private float mWidth = -1;//宽度
    private RectF mItemRect;//对应矩形；
    private float mRadian;//弧度；
    private int mAlpha;
    private int mColor;
    private float mRandomSpeed;
    private String mText;
    private int mSelectType = 0;


    public int getSelectType() {
        return mSelectType;
    }

    public void setSelectType(int selectType) {
        mSelectType = selectType;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public float getRandomSpeed() {
        return mRandomSpeed;
    }

    public void setRandomSpeed(float randomSpeed) {
        mRandomSpeed = randomSpeed;
    }

    public int getAlpha() {

        return mAlpha;
    }

    public void setAlpha(int alpha) {
        mAlpha = alpha;
    }

    public int getLap() {
        return mLap;
    }

    public void setLap(int lap) {
        mLap = lap;
    }

    public int getColumn() {
        return mColumn;
    }

    public void setColumn(int column) {
        mColumn = column;
    }

    public float getWidth() {
        return mWidth;
    }

    public void setWidth(float width) {
        mWidth = width;
    }

    public RectF getItemRect() {
        return mItemRect;
    }

    public void setItemRect(RectF itemRect) {
        mItemRect = itemRect;
    }

    public float getRadian() {
        return mRadian;
    }

    public void setRadian(float radian) {
        mRadian = radian;
    }
}
