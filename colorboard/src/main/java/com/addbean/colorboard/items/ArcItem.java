package com.addbean.colorboard.items;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.addbean.colorboard.ColorBoard;

/**
 * Created by AddBean on 2016/7/28.
 */
public class ArcItem extends BaseItem {

    public ArcItem(Context context, ColorBoard colorBoard, int lap, int column) {
        super(context, colorBoard, lap, column);
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
//        paint.setAlpha(mItemMate.getAlpha());
        paint.setStrokeWidth(getWidth());
        Path path = new Path();
        paint.setStyle(Paint.Style.STROKE);
        path.addArc(getItemRect(), mCurrentRadian, mItemMate.getRadian());
        RectF oval = getItemRect();
        canvas.drawArc(oval, 0, mItemMate.getRadian(), false, paint);
    }

    private void drawTextMsg(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(getWidth() / 5);
        paint.setStrokeWidth(1);
        canvas.drawText(mItemMate.getText(), getItemRect().right - getWidth() / 3, getItemRect().centerY() + getWidth() / 3, paint);
    }

    private void drawSelected(Canvas canvas) {

        if (mItemMate.getSelectType() == ItemMate.SELECTED_TRUE) {
            Paint paint = new Paint();
            float paintWidth = 2 * DP;
            paint.setStrokeWidth(paintWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setAlpha(50);
            paint.setAlpha(100);
            paint.setColor(Color.WHITE);
            canvas.drawPath(getArcPath((int) (paintWidth / 2), 0.5f), paint);
        }
    }

    private Path getArcPath(int inset, float offsetAngle) {
        RectF oval = new RectF(getItemRect());
        oval.inset(getWidth() / 2 - inset, getWidth() / 2 - inset);
        Path path = new Path();
        path.arcTo(oval, offsetAngle, mItemMate.getRadian() - 2 * offsetAngle);
        RectF oval2 = new RectF(getItemRect());
        oval2.inset(-getWidth() / 2 + inset, -getWidth() / 2 + inset);
        path.arcTo(oval2, mItemMate.getRadian() - offsetAngle, -mItemMate.getRadian() + 2 * offsetAngle);
        path.close();
        return path;
    }
}
