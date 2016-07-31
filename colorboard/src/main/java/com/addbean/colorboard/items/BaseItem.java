package com.addbean.colorboard.items;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;

import com.addbean.colorboard.ColorBoard;
import com.addbean.colorboard.IItemClickListener;
import com.addbean.colorboard.ToolsUtils;

/**
 * Created by AddBean on 2016/7/28.
 */
public abstract class BaseItem {
    protected ColorBoard mColorBoard;
    protected ItemMate mItemMate;
    protected int DP = 1;
    protected float mCurrentRadian;
    private Canvas mCanvas;
    private IItemClickListener iItemClickListener;

    public BaseItem(Context context, ColorBoard colorBoard, int lap, int column) {
        DP = ToolsUtils.dp2Px(context, 1);
        mItemMate = new ItemMate();
        mItemMate.setLap(lap);
        mItemMate.setColumn(column);
        mColorBoard = colorBoard;
        mItemMate.setRadian(360f / mColorBoard.getColumnSize());
        mCurrentRadian = mItemMate.getColumn() * mItemMate.getRadian();
    }

    public void draw(Canvas canvas) {
        mCanvas = canvas;
        int count = canvas.save();
        canvas.rotate(mCurrentRadian);
        onDraw(canvas);
        canvas.restoreToCount(count);
    }

    protected abstract void onDraw(Canvas canvas);

    protected abstract boolean onClick();

    protected float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mItemMate.getItemRect().centerX());
        double y = yTouch - (mItemMate.getItemRect().centerY());
        float angle = (float) (Math.atan(y / x) * 180 / Math.PI);
        if (yTouch > 0 && xTouch < 0) angle = 180 + angle;
        if (yTouch < 0 && xTouch < 0) angle += 180;
        if (yTouch < 0 && xTouch > 0) angle = 360 + angle;
        return angle;
    }

    protected RectF getItemRect() {
        float width = getWidth();
        mItemMate.setItemRect(new RectF());
        mItemMate.getItemRect().left = (float) (-width * (mItemMate.getLap()) * Math.pow(mColorBoard.mAnimState, mItemMate.getRandomSpeed()));
        mItemMate.getItemRect().top = (float) (-width * (mItemMate.getLap()) * Math.pow(mColorBoard.mAnimState, mItemMate.getRandomSpeed()));
        mItemMate.getItemRect().right = (float) (width * (mItemMate.getLap()) * Math.pow(mColorBoard.mAnimState, mItemMate.getRandomSpeed()));
        mItemMate.getItemRect().bottom = (float) (width * (mItemMate.getLap()) * Math.pow(mColorBoard.mAnimState, mItemMate.getRandomSpeed()));
        return mItemMate.getItemRect();
    }

    protected float getWidth() {
        if (mItemMate.getWidth() > 0) return mItemMate.getWidth();
        mItemMate.setWidth(mColorBoard.mBaseRect.width() / (mColorBoard.getLapSize() * 2));
        return mItemMate.getWidth();
    }

    public void setmItemMate(ItemMate mItemMate) {
        this.mItemMate = mItemMate;
    }

    public ItemMate getItemMate() {
        return mItemMate;
    }

    public boolean handleClick(float x1, float y1) {
        Point point = new Point((int) x1, (int) y1);
        float cx =getItemRect().centerX();
        float cy = getItemRect().centerY();
        float dx = point.x - cx;
        float dy = point.y - cy;
        float dr = (float) Math.sqrt(dx * dx + dy * dy);
        float dd = dr - (mItemMate.getItemRect().width() - getWidth()) / 2;
        float da = mCurrentRadian + mItemMate.getRadian() - getAngle(point.x, point.y);
        boolean isInAngle = da > 0 && da < mItemMate.getRadian();
        if (dd < getWidth() && dd > 0 && isInAngle) {
            mItemMate.setSelectType(ItemMate.SELECTED_TRUE);
            invalidate();
            if (iItemClickListener != null) iItemClickListener.onItemClick(mItemMate);
            return onClick();
        } else {
            mItemMate.setSelectType(ItemMate.SELECTED_FALSE);
            return false;
        }

    }

    public float getCurrentRadian() {
        return mCurrentRadian;
    }

    public void invalidate() {
        mColorBoard.invalidate();
    }

    public void setIItemClickListener(IItemClickListener mIItemClickListener) {
        iItemClickListener = mIItemClickListener;
    }
}
