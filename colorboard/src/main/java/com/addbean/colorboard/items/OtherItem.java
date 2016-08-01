package com.addbean.colorboard.items;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.addbean.colorboard.ColorBoard;

/**
 * Created by AddBean on 2016/7/31.
 */
public class OtherItem extends BaseItem {
    private int mInset = 0;

    public OtherItem(Context context, ColorBoard colorBoard, int lap, int column, int size) {
        super(context, colorBoard, lap, column);
        mInset = 6 * DP;
        mItemMate.setRadian(360f / size);
        mCurrentRadian = mItemMate.getColumn() * mItemMate.getRadian();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawColorBg(canvas);
        drawTextMsg(canvas);
        drawSelected(canvas);
    }

    @Override
    protected boolean onClick() {
        mColorBoard.show();//点击收缩;
        return true;
    }

    private void drawColorBg(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mItemMate.getColor() | 0xff000000);
        paint.setStrokeWidth(getWidth());
        paint.setStyle(Paint.Style.STROKE);
        RectF oval = getItemRect();
        oval.inset(mInset, mInset);
        canvas.drawArc(oval, 0, mItemMate.getRadian(), false, paint);
    }

    private void drawTextMsg(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(getWidth() / 5);
        paint.setStrokeWidth(1);
        Path path = new Path();
        RectF oval = new RectF(getItemRect());
        oval.inset(mInset,mInset);
        path.addArc(oval, 0, mItemMate.getRadian());
        canvas.drawTextOnPath(mItemMate.getText(), path,3f*DP,0, paint);
    }

    private void drawSelected(Canvas canvas) {
        if (mItemMate.getSelectType() == ItemMate.SELECTED_TRUE) {
            Paint paint = new Paint();
            float paintWidth=3*DP;
            paint.setStrokeWidth(3 * DP);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setAlpha(50);
            paint.setColor(Color.WHITE);
            canvas.drawPath(getArcPath((int) (paintWidth/2),0.5f), paint);
        }
    }

    private Path getArcPath(int inset,float offsetAngle) {
        RectF oval = new RectF(getItemRect());
        oval.inset(getWidth() / 2 - inset+mInset, getWidth() / 2 - inset+mInset);
        Path path = new Path();
        path.arcTo(oval, offsetAngle, mItemMate.getRadian()-2*offsetAngle);
        RectF oval2 = new RectF(getItemRect());
        oval2.inset(-getWidth() / 2+ inset+mInset, -getWidth() / 2 + inset+mInset);
        path.arcTo(oval2, mItemMate.getRadian()-offsetAngle, -mItemMate.getRadian()+2*offsetAngle);
        path.close();
        return path;
    }
}
