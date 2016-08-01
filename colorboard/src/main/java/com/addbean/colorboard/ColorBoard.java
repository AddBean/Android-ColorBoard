package com.addbean.colorboard;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.addbean.colorboard.items.ArcItem;
import com.addbean.colorboard.items.BaseItem;
import com.addbean.colorboard.items.OtherItem;
import com.addbean.colorboard.items.ItemMate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AddBean on 2016/7/28.
 */
public class ColorBoard extends View implements GestureDetector.OnGestureListener {
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
    private GestureDetector mDetector;
    private ValueAnimator mRotateAnimator;
    private int mPosition = -1;
    private int mStartLaps = -1;
    public static final int CENTER = 0, TOP = 1, DOWN = 2, LEFT = 4, RIGHT = 8;
    private int mMargin = -1;
    public static List<ItemMate> mOtherList = new ArrayList<>();

    public ColorBoard(Context context) {
        super(context);
        initView();
    }

    public ColorBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParam(attrs);
        initView();
    }

    public ColorBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParam(attrs);
        initView();
    }

    private void initParam(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.ColorBoard, 0, 0);
        if (mPosition < 0)
            mPosition = typedArray.getInt(R.styleable.ColorBoard_colorPosition, 8);
        if (mStartLaps < 0)
            mStartLaps = typedArray.getInt(R.styleable.ColorBoard_startLap, 5);
        if (mMargin < 0)
            mMargin = (int) typedArray.getDimension(R.styleable.ColorBoard_margin, 0);
        Log.e(TAG, "mMargin" + mMargin);
        typedArray.recycle();
    }

    private void initView() {
        mDetector = new GestureDetector(this);
        DP = ToolsUtils.dp2Px(getContext(), 1);
        Const.initColorList();
        Const.initOtherColorList();
        for (int i = 0; i < Const.mColorList.size(); i++) {
            ColorMate mate = Const.mColorList.get(i);
            if (mate.getmLap() % 2 != 0) {
                ArcItem item = new ArcItem(getContext(), this, mStartLaps + mate.getmLap() / 2, mate.getmColumn());
                item.getItemMate().setColor(mate.getmColor());
                item.getItemMate().setRandomSpeed(2 + mate.getmColumn() % 3);
                item.getItemMate().setText(mate.getmName());
                item.setIItemClickListener(iItemClickListener);
                mItems.add(item);
            }
        }
        for (int i = 0; i < Const.mOtherColorList.size(); i++) {
            ColorMate mate = Const.mOtherColorList.get(i);
            OtherItem item = new OtherItem(getContext(), this, mStartLaps - 1, i, Const.mOtherColorList.size()+5);
            item.getItemMate().setColor(mate.getmColor());
            item.getItemMate().setRandomSpeed(2 + mate.getmColumn() % 3);
            item.getItemMate().setText(mate.getmName());
            item.setIItemClickListener(iItemClickListener);
            mItems.add(item);
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
//        Paint paint = new Paint();
//        paint.setStrokeWidth(2 * DP);
//        paint.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(0, 0, mSize / 2, paint);
//        canvas.drawCircle(mStartPoint.x, mStartPoint.y, 10 * DP, paint);
    }

    private boolean itemVisible(BaseItem item) {
        float degress = (item.getCurrentRadian() - mDegress) % 360;
        if (degress < 0) degress = 360 + degress;
        switch (mPosition) {
            case CENTER://中间；
                return true;
            case TOP://上
                return (degress <= 180 && degress >= 0);
            case DOWN://下
                return (degress >= 180 && degress <= 360);
            case LEFT://左
                return (degress <= 90 && degress >= 0) || (degress >= 270 && degress <= 360);
            case RIGHT://右
                return (degress >= 90 && degress <= 270);
            case TOP | LEFT://左上
                return (degress >= 0 && degress <= 90);
            case DOWN | RIGHT://右下；
                return (degress >= 180 && degress <= 270);
            case TOP | RIGHT://右上；
                return (degress >= 90 && degress <= 180);
            case DOWN | LEFT://左下；
                return (degress >= 270 && degress <= 360);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
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
        switch (mPosition) {
            case CENTER://中间；
                return new Point(-getWidth() / 2, (-getHeight() / 2));
            case TOP://上
                return new Point(-getWidth() / 2, mMargin);
            case DOWN://下
                return new Point(-getWidth() / 2, -getHeight() - mMargin);
            case LEFT://左
                return new Point(mMargin, (-getHeight() / 2));
            case RIGHT://右
                return new Point(-getWidth() - mMargin, (-getHeight() / 2));
            case TOP | LEFT://左上
                return new Point(mMargin, mMargin);
            case DOWN | RIGHT://右下；
                return new Point(-getWidth() - mMargin, -getHeight() - mMargin);
            case TOP | RIGHT://右上；
                return new Point(-getWidth() - mMargin, mMargin);
            case DOWN | LEFT://左下；
                return new Point(mMargin, -getHeight() - mMargin);
            default:
                return new Point(mSize / 6, (-getHeight() / 2));
        }
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
        ColorBoard.this.setVisibility(mAnimState != 0 ? VISIBLE : GONE);
        ValueAnimator animator = ValueAnimator.ofFloat(mIsShow ? 1f : 0f, mIsShow ? 0f : 1f).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimState = (float) animation.getAnimatedValue();
                mIsShow = mAnimState == 1.0f;
                postInvalidate();
                ColorBoard.this.setVisibility(mAnimState != 0 ? VISIBLE : GONE);
            }
        });
        animator.start();
    }

    public void setIItemClickListener(IItemClickListener mIItemClickListener) {
        iItemClickListener = mIItemClickListener;
        for (BaseItem item : mItems) {
            item.setIItemClickListener(iItemClickListener);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Point mPoint = getReversePoint(e);
        mStartDownX = mPoint.x;
        mStartDownY = mPoint.y;
        mStartPoint = mPoint;
        mClickEnable = true;
        invalidate();
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Point mPoint = getReversePoint(e);
        handleClick(mPoint.x, mPoint.y);
        invalidate();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Point mPoint = getReversePoint(e2);
        float dx = Math.abs(mPoint.x - mStartDownX);
        float dy = Math.abs(mPoint.y - mStartDownY);
        float dd = (float) Math.sqrt(dx * dx + dy * dy);
        if (dd < 20 && mClickEnable) {
            mClickEnable = true;
            return true;
        }
        float start = getAngle(mStartPoint.x, mStartPoint.y);//获得开始的角度
        float end = getAngle(mPoint.x, mPoint.y);//获得当前的角度
        mDegress += end - start;
        mDegress = mDegress % 360;
        mDegress = (mDegress < 0) ? 360 + mDegress : mDegress;
        mStartPoint = getReversePoint(e2);
        mClickEnable = false;
        invalidate();
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Point mPoint1 = getReversePoint(e1);
        Point mPoint2 = getReversePoint(e2);
        float start = getAngle(mPoint1.x, mPoint1.y);//获得开始的角度
        float end = getAngle(mPoint2.x, mPoint2.y);//获得当前的角度;
        float angleDelta = end - start;
        if (Math.abs(angleDelta) > 5) {
            float velocity = (float) Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            float degress = (angleDelta > 0 ? 1 : -1) * ((angleDelta > 0) ? +velocity / 30f : velocity / 30f);
            startRotateAnim(degress % 360, velocity);
            return true;
        } else {
            return false;
        }
    }

    private void startRotateAnim(final float degress, float velocity) {

        if (Math.abs(degress) < 5) return;
        if (mRotateAnimator != null)
            mRotateAnimator.cancel();
        mRotateAnimator = ValueAnimator.ofFloat(0, degress).setDuration(600);
        final float angle = mDegress % 360;
        mRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mDegress = angle + value;
                mDegress = mDegress % 360;
                mDegress = (mDegress < 0) ? 360 + mDegress : mDegress;
                Log.e(TAG, "mDegress:" + mDegress);
                postInvalidate();
            }
        });
        mRotateAnimator.start();
    }


    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    public void setMargin(int mMargin) {
        this.mMargin = mMargin;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public void setStartLaps(int mStartLaps) {
        this.mStartLaps = mStartLaps;
    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Point mPoint = getReversePoint(event);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mStartDownX = mPoint.x;
//                mStartDownY = mPoint.y;
//                mStartPoint = mPoint;
//                mClickEnable = true;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float dx = Math.abs(mPoint.x - mStartDownX);
//                float dy = Math.abs(mPoint.y - mStartDownY);
//                float dd = (float) Math.sqrt(dx * dx + dy * dy);
//                if (dd < 20 && mClickEnable) {
//                    mClickEnable = true;
//                    return true;
//                }
//                float start = getAngle(mStartPoint.x, mStartPoint.y);//获得开始的角度
//                float end = getAngle(mPoint.x, mPoint.y);//获得当前的角度
//                mDegress += end - start;
//                mDegress = mDegress % 360;
//                Log.e(TAG, "mDegress:" + mDegress);
//                mStartPoint = getReversePoint(event);
//                mClickEnable = false;
//                break;
//            case MotionEvent.ACTION_UP:
//                if (mClickEnable)
//                    handleClick(mStartPoint.x, mStartPoint.y);
//                break;
//        }
//        invalidate();
//        return true;
//    }
}
