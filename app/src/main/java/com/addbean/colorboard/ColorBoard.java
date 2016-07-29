package com.addbean.colorboard;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.addbean.autils.tools.ToolsUtils;
import com.addbean.colorboard.items.ArcItem;
import com.addbean.colorboard.items.BaseItem;
import com.addbean.colorboard.items.ItemMate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by AddBean on 2016/7/28.
 */
public class ColorBoard extends View {
    private float mDegress = 0;
    private int DP = 1;
    private String TAG = "ColorBoard";
    private int mSize = 0;
    private List<BaseItem> mItems = new ArrayList<>();
    public RectF mBaseRect = new RectF();
    public float mAnimState = 0;
    private boolean mIsShow;
    private float mStartDownY;
    private float mStartDownX;
    private Point mStartPoint = new Point();
    private boolean mClickEnable;
    private IItemClickListener iItemClickListener;

    public ColorBoard(Context context) {
        super(context);
        initView();
    }

    public ColorBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ColorBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        DP = ToolsUtils.dpConvertToPx(getContext(), 1);
        Const.initColorList();
        for (int i = 0; i < Const.mColorList.size(); i++) {
            ColorMate mate = Const.mColorList.get(i);
            if (mate.getmLap() % 2 != 0) {
                ArcItem item = new ArcItem(getContext(), this, 8 + mate.getmLap() / 2, mate.getmColumn());
                item.getItemMate().setColor(mate.getmColor());
                item.getItemMate().setRandomSpeed(2 + mate.getmColumn() % 3);
                item.getItemMate().setText(mate.getmName());
                item.setIItemClickListener(iItemClickListener);
                mItems.add(item);
            }
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mSize = (int) (1.8f * Math.max(width, height));
        mBaseRect.set(-mSize / 2, -mSize / 2, mSize / 2, mSize / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = canvas.save();
        canvas.translate(-getTranslate().x, -getTranslate().y);
        canvas.rotate(-mDegress, mBaseRect.centerX(), mBaseRect.centerY());
        dispatchItemDraw(canvas);
        canvas.restoreToCount(count);
    }

    private void dispatchItemDraw(Canvas canvas) {
        for (BaseItem item : mItems) {
            if (itemVisible(item))
                item.draw(canvas);
        }
        Paint paint = new Paint();
        paint.setStrokeWidth(2 * DP);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(0, 0, mSize / 2, paint);
        canvas.drawCircle(mStartPoint.x, mStartPoint.y, 10 * DP, paint);
    }

    private boolean itemVisible(BaseItem item) {
        float degress = (item.getCurrentRadian() - mDegress) % 360;
        if (degress < 0) degress = 360 + degress;
        return (degress < 60 && degress > 0) || (degress > 290 && degress < 360);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point mPoint = getReversePoint(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartDownX = mPoint.x;
                mStartDownY = mPoint.y;
                mStartPoint = mPoint;
                mClickEnable = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(mPoint.x - mStartDownX);
                float dy = Math.abs(mPoint.y - mStartDownY);
                float dd = (float) Math.sqrt(dx * dx + dy * dy);
                if (dd < 20&&mClickEnable) {
                    mClickEnable = true;
                    return true;
                }
                float start = getAngle(mStartPoint.x, mStartPoint.y);//获得开始的角度
                float end = getAngle(mPoint.x, mPoint.y);//获得当前的角度
                mDegress += end - start;
                mDegress = mDegress % 360;
                Log.e(TAG, "mDegress:" + mDegress);
                mStartPoint = getReversePoint(event);
                mClickEnable = false;
                break;
            case MotionEvent.ACTION_UP:
                if (mClickEnable)
                    handleClick(mStartPoint.x, mStartPoint.y);
                break;
        }
        invalidate();
        return true;
    }

    private Point getReversePoint(MotionEvent event) {
        float[] p = new float[2];
        p[0] = event.getX();
        p[1] = event.getY();
        Matrix m1 = new Matrix();
        m1.postTranslate(getTranslate().x, getTranslate().y);
        m1.postRotate(mDegress, mBaseRect.centerX(), mBaseRect.centerY());
        m1.mapPoints(p);
        return new Point((int) p[0], (int) p[1]);
    }

    private Point getTranslate() {
        return new Point(mSize / 6, (int) (-getHeight() / 2));
    }

    private void handleClick(float x, float y) {
        Log.e(TAG, "x,y " + x + " " + y);
        for (BaseItem item : mItems) {
            item.handleClick(x, y);
        }
    }

    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mBaseRect.centerX());
        double y = yTouch - (mBaseRect.centerY());
        return (float) (Math.atan2(x, y) * 180 / Math.PI);
    }

    public int getLapSize() {
        return Const.mColorLop;
    }

    public int getColumnSize() {
        return Const.mColorColumn;
    }

    public void show() {
        ColorBoard.this.setVisibility(mAnimState!=0?VISIBLE:GONE);
        ValueAnimator animator = ValueAnimator.ofFloat(mIsShow ? 1f : 0f, mIsShow ? 0f : 1f).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimState = (float) animation.getAnimatedValue();
                mIsShow = mAnimState == 1.0f;
                postInvalidate();
                ColorBoard.this.setVisibility(mAnimState!=0?VISIBLE:GONE);
            }
        });
        animator.start();
    }

    public void setIItemClickListener(IItemClickListener mIItemClickListener) {
        iItemClickListener = mIItemClickListener;
        for(BaseItem item:mItems){
            item.setIItemClickListener(iItemClickListener);
        }
    }

}
