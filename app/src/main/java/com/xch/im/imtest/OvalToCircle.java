package com.xch.im.imtest;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import static android.R.attr.duration;

/**
 * Created by Administrator on 2018/3/8 0008.
 */

public class OvalToCircle extends View {

    RectF mOvalRectF = new RectF();
    private Paint mOvalPaint;
    private int two_circle_distance;
    private int default_two_circle_distance;//默认两个圆圆心之间的距离==需要移动的距离
    private int width;
    private int height;
    private int circleAngle = 0;//矩形的角度
    private int bg_color = 0xffbc7d53;//背景颜色


    Rect mTextRect = new Rect();
    private Paint mTextPaint;
    private ValueAnimator animator_oval_to_angle;//矩形变成圆角矩形的动画
    private int duration_voal_to_circle = 1000;//动画时间
    private ValueAnimator animator_angle_to_circle;//圆角矩形到圆的动画
    private int mText_color;//字体变化的颜色

    private int move_distance = 300;//view上移的距离
    private ObjectAnimator animator_move_to_up;//view上移的动画

    private AnimatorSet animatorSet = new AnimatorSet();//动画集

    /**
     * 路径--用来获取对勾的路径
     */
    private Path path = new Path();
    /**
     * 取路径的长度
     */
    private PathMeasure pathMeasure;
    /**
     * 对路径处理实现绘制动画效果
     */
    private PathEffect effect;
    private ValueAnimator animator_draw_ok;

    /**
     * 对勾（√）画笔
     */
    private Paint okPaint;

    private boolean startDrawOk=false;//是否开始画对勾

    public OvalToCircle(Context context) {
        this(context, null);

    }

    public OvalToCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public OvalToCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animationButtonListener != null) {
                    animationButtonListener.onClickListener();
                }
            }
        });

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationButtonListener != null) {
                    animationButtonListener.animationFinish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        width = w;
        height = h;
        default_two_circle_distance = (w - h) / 2;

        initOk();

        initAnimation();

    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        set_oval_to_angle_animation();
        set_angle_to_circle_animation();
        set_move_to_up_animation();
        set_draw_ok_animation();

        animatorSet
                .play(animator_move_to_up)
                .before(animator_draw_ok)
                .after(animator_angle_to_circle)
                .after(animator_oval_to_angle);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mOvalPaint = new Paint();
        mOvalPaint.setStrokeWidth(4);
        mOvalPaint.setStyle(Paint.Style.FILL);
        mOvalPaint.setAntiAlias(true);
        mOvalPaint.setColor(bg_color);


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(40);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);

        okPaint = new Paint();
        okPaint.setStrokeWidth(10);
        okPaint.setStyle(Paint.Style.STROKE);
        okPaint.setAntiAlias(true);
        okPaint.setColor(Color.WHITE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        draw_oval_to_circle(canvas);

        draw_text(canvas);

        if (startDrawOk){
            canvas.drawPath(path,okPaint);
        }
    }

    /**
     * 绘制带圆角的矩形
     *
     * @param canvas
     */
    private void draw_oval_to_circle(Canvas canvas) {
        mOvalRectF.left = two_circle_distance;
        mOvalRectF.top = 0;
        mOvalRectF.right = width - two_circle_distance;
        mOvalRectF.bottom = height;
        canvas.drawRoundRect(mOvalRectF, circleAngle, circleAngle, mOvalPaint);
    }

    private void draw_text(Canvas canvas) {

        mTextRect.left = 0;
        mTextRect.top = 0;
        mTextRect.right = width;
        mTextRect.bottom = height;

        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int baseline = (mTextRect.bottom - mTextRect.top - fontMetricsInt.bottom - fontMetricsInt.top) / 2;

        canvas.drawText("确认完成", mTextRect.centerX(), baseline, mTextPaint);

    }

    /**
     * 绘制对勾
     */
    private void initOk() {
        path.moveTo(default_two_circle_distance + height / 8 * 3, height / 2);
        path.lineTo(default_two_circle_distance + height / 2, height / 5 * 3);
        path.lineTo(default_two_circle_distance + height / 3 * 2, height / 5 * 2);

        pathMeasure = new PathMeasure(path, true);

    }

    /**
     * 设置矩形到圆角矩形的动画
     */
    private void set_oval_to_angle_animation() {

        animator_oval_to_angle = ValueAnimator.ofInt(0, height / 2);
        animator_oval_to_angle.setDuration(duration_voal_to_circle);
        animator_oval_to_angle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    /**
     * 圆角矩形到圆的动画
     */
    private void set_angle_to_circle_animation() {
        animator_angle_to_circle = ValueAnimator.ofInt(0, default_two_circle_distance);
        animator_angle_to_circle.setDuration(duration_voal_to_circle);
        animator_angle_to_circle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                two_circle_distance = (int) animation.getAnimatedValue();
                mText_color = 255 - (two_circle_distance * 255) / default_two_circle_distance;
                mTextPaint.setColor(mText_color);
                invalidate();
            }

        });

    }

    /**
     * 设置view上移的动画
     */
    private void set_move_to_up_animation() {
        float translationY = getTranslationY();//当前坐标
        animator_move_to_up = ObjectAnimator.ofFloat(this, "translationY", translationY, translationY - move_distance);
        animator_move_to_up.setDuration(duration_voal_to_circle);
        animator_move_to_up.setInterpolator(new AccelerateDecelerateInterpolator());

    }


    /**
     * 画√
     */
    private void set_draw_ok_animation() {
        animator_draw_ok = ValueAnimator.ofFloat(1, 0);
        animator_draw_ok.setDuration(duration_voal_to_circle);
        animator_draw_ok.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startDrawOk = true;
                float value = (Float) animation.getAnimatedValue();

                effect = new DashPathEffect(new float[]{pathMeasure.getLength(), pathMeasure.getLength()}, value * pathMeasure.getLength());
                okPaint.setPathEffect(effect);
                invalidate();
            }

        });
    }


    /**
     * 启动动画
     */
    public void start() {
        animatorSet.start();
    }

    /**
     * 动画还原
     */
    public void reset() {
        startDrawOk = false;
        circleAngle = 0;
        two_circle_distance = 0;
        default_two_circle_distance = (width - height) / 2;
        mTextPaint.setAlpha(255);
        setTranslationY(getTranslationY() + move_distance);
        invalidate();
    }

    /**
     * 接口口回调
     */
    public interface AnimationButtonListener {
        /**
         * 按钮点击事件
         */
        void onClickListener();

        /**
         * 动画完成回调
         */
        void animationFinish();
    }

    /**
     * 点击事件及动画事件2完成回调
     */
    private AnimationButtonListener animationButtonListener;

    public void setAnimationButtonListener(AnimationButtonListener listener) {
        animationButtonListener = listener;
    }

}
