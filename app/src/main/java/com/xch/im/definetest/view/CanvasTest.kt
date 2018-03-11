package com.xch.im.definetest.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

/**
 *@作者 ForrestHope
 *
 *@创建日期 2018/3/11 0011 15:43
 *
 *@描述 画布操作
 *
 */
class CanvasTest : View {
    private var mWidth: Int = 0 //屏幕宽度
    private var mHeight: Int = 0  //屏幕高度
    private lateinit var mPaint: Paint //画笔
    private var mHourCount: Int = 12//画时钟线
    private var mMintueCount: Int = 60//画分钟线

    private var mTimeHour: Int = 0  //当前时间--小时

    private var mTimeMinute: Int = 0  //当前时间--分钟

    private lateinit var mTextPaint: TextPaint //画时钟上的数字
    private lateinit var mMinutePaint: Paint //分钟的线

    private lateinit var mHourLinePaint: Paint  //时钟指针
    private lateinit var mMinuteLinePaint: Paint  //分钟指针

    private var saveCanvas: Int = 0 //保存画布原点在正中心的状态

    constructor(context: Context?) : super(context) {
        initPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaint()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w;
        mHeight = h;

    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.BLACK
        mPaint.strokeWidth = 10f


        mTextPaint = TextPaint()
        mTextPaint.textSize = 40f;
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.isAntiAlias = true
        mTextPaint.color = Color.BLACK
        mTextPaint.textAlign = Paint.Align.CENTER

        mMinutePaint = Paint()
        mMinutePaint.style = Paint.Style.STROKE
        mMinutePaint.color = Color.GRAY
        mMinutePaint.strokeWidth = 5f



        mHourLinePaint = Paint()
        mHourLinePaint.style = Paint.Style.FILL
        mHourLinePaint.color = Color.RED
        mHourLinePaint.strokeWidth = 15f

        mMinuteLinePaint = Paint()
        mMinuteLinePaint.style = Paint.Style.FILL
        mMinuteLinePaint.color = Color.BLUE
        mMinuteLinePaint.strokeWidth = 5f

    }

    fun setHourCount(count: Int) {
        mTimeHour = count
    }

    fun setMintueCount(count: Int) {
        mTimeMinute = count
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas!!.translate((width / 2).toFloat(), (mHeight / 2).toFloat()) //把画布移到屏幕中心
        saveCanvas = canvas.save()
        if (mTimeHour <= 0 || mTimeHour > 12) {
            return
        }

        if (mTimeMinute <= 0 || mTimeMinute > 60) {
            return
        }
        drawBigCircle(canvas)

        drawSmallCircle(canvas)

        drawHourLine(canvas)

        drawMinuteLine(canvas)

        drawTime(canvas)

    }

    //画大圆
    private fun drawBigCircle(canvas: Canvas?) {
        //画个圆点
        canvas!!.drawPoint(0f, 0f, mPaint)

        canvas!!.drawCircle(0f, 0f, 300f, mPaint)
    }

    private fun drawSmallCircle(canvas: Canvas?) {
        canvas!!.drawCircle(0f, 0f, 250f, mPaint)


        for (i in 0..mHourCount) {
            if (i == 0) {
                drawNum(canvas, 30 * i, "12", mTextPaint)

            } else {
                drawNum(canvas, 30 * i, i.toString(), mTextPaint)
            }

        }
    }

    private fun drawHourLine(canvas: Canvas?) {
        //0 250 这个点是小圆的正下方那个点 0f 300f 是大圆的正下方的点


        for (i in 0..mHourCount) {
            canvas!!.drawLine(0f, 250f, 0f, 300f, mPaint)//两个点之间画一个线  时钟
            //然后把这个线旋转
            canvas.rotate((360 / mHourCount).toFloat())

        }

    }

    private fun drawMinuteLine(canvas: Canvas?) {
        for (i in 0..mMintueCount) {
            if (i % 5 != 0) {
                canvas!!.drawLine(0f, 270f, 0f, 300f, mMinutePaint)//两个点之间画一个线  分钟
            }
            //然后把这个线旋转
            canvas!!.rotate((360 / mMintueCount).toFloat())
        }

    }

    private fun drawTime(canvas: Canvas?) {

        //画时针
        canvas!!.drawLine(0f, 0f, 0f, -150f, mHourLinePaint)
        canvas!!.rotate((360 / mTimeHour).toFloat())
        //  canvas.restoreToCount(saveCanvas)
        //    saveCanvas=canvas.save()

        //画分针
        canvas.drawLine(0f, 0f, 0f, -200f, mMinuteLinePaint)
        canvas.rotate((360 / mTimeMinute).toFloat())
        //  canvas.restoreToCount(saveCanvas)
    }

    private fun drawNum(canvas: Canvas?, degree: Int, text: String, paint: Paint) {
        var textBound = Rect()
        paint.getTextBounds(text, 0, text.length, textBound);
        canvas!!.rotate(degree.toFloat())
        canvas!!.translate(0f, (150 - getWidth() / 3).toFloat());//这里的50是坐标中心距离时钟最外边框的距离，当然你可以根据需要适当调节
        canvas!!.rotate((-degree).toFloat());
        canvas!!.drawText(text, (-textBound.width() / 2).toFloat(),
                (textBound.height() / 2).toFloat(), paint)
        canvas!!.rotate(degree.toFloat())
        canvas!!.translate(0f, (getWidth() / 3 - 150).toFloat());
        canvas!!.rotate((-degree).toFloat());
    }


}