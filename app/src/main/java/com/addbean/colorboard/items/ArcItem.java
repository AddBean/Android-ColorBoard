package com.addbean.colorboard.items;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.addbean.color_selector.ColorBoard;

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
        return false;
    }

    private void drawColorBg(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mItemMate.getColor());
        paint.setAlpha(mItemMate.getAlpha());
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
        paint.setTextSize(getWidth() / 4);
        paint.setStrokeWidth(1);
        canvas.drawText(mItemMate.getText(), getItemRect().right - getWidth() / 3, getItemRect().centerY() + getWidth() / 3, paint);
    }

    private void drawSelected(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(1*DP);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setAlpha(255);
        if (mItemMate.getSelectType() == ItemMate.SELECTED_TRUE) {
            paint.setColor(Color.RED);
            RectF oval = getItemRect();
            canvas.drawArc(oval, 0, mItemMate.getRadian(), false, paint);
        }
    }
}
