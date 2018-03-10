package com.xch.im.imtest.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View

/**
 * Created by Administrator on 2018/3/10 0010.
 */

class DrawTestView : View {

    internal lateinit var mPaint: Paint

    constructor(context: Context) : super(context) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaint()
    }

    //初始化画笔
    private fun initPaint() {
        mPaint = Paint()

        mPaint.color = Color.BLACK
        //    mPaint.setStyle(Paint.Style.FILL);//设置画笔为填充

        //    mPaint.setStyle(Paint.Style.STROKE);//设置画笔为描边

        mPaint.style = Paint.Style.FILL_AND_STROKE//设置画笔为填充且描边
        mPaint.strokeWidth = 10f//画笔宽度

    }

    //画点
    private fun drawPoint(canvas: Canvas) {
        canvas.drawPoint(200f, 200f, mPaint)//在坐标为200 200的地方画一个点


        canvas.drawPoints(floatArrayOf(300f, 300f, 300f, 320f, 300f, 340f), mPaint)//每两个数字为一个坐标点
    }

    //画线
    private fun drawLine(canvas: Canvas) {
        canvas.drawLine(300f, 400f, 350f, 400f, mPaint)
        canvas.drawLine(300f, 450f, 400f, 450f, mPaint)

        canvas.drawLines(floatArrayOf(300f, 500f, 400f, 600f), mPaint)//开始坐标 结束坐标确定即可
    }

    //画矩形
    private fun drawRect(canvas: Canvas) {
        canvas.drawRect(300f, 650f, 500f, 800f, mPaint)//第一种画法  左上角 右下角左边确定即可

        val rect = Rect(300, 850, 500, 900)//左上右下
        canvas.drawRect(rect, mPaint)//第二种画法  rect是整型的

        val rectF = RectF(600f, 850f, 800f, 900f)
        canvas.drawRect(rectF, mPaint)//第三种画法  rectf是浮点型的

    }

    //画圆角矩形
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun drawRoundRect(canvas: Canvas) {
        canvas.drawRoundRect(300f, 950f, 500f, 1000f, 20f, 20f, mPaint)//第一种画法 需要api21,因此一般使用第二种方法

        val rectF = RectF(600f, 950f, 800f, 1000f)
        canvas.drawRoundRect(rectF, 30f, 30f, mPaint) //圆角矩形不是正规的圆 而是椭圆 这两个参数 rx  ry是椭圆的两个半径,如果rx是宽度一半 ry是高度一半的时候， 这个圆角矩形就是一个椭圆
    }

    //画椭圆
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun drawOval(canvas: Canvas) {
        //画椭圆只需要一个矩形就可以了
        val rectF = RectF(300f, 1050f, 500f, 1100f) //第一种
        canvas.drawOval(rectF, mPaint)

        canvas.drawOval(600f, 1050f, 800f, 1100f, mPaint)//第二种 需要api21  建议使用第一种
    }

    //画圆
    private fun drawCircle(canvas: Canvas) {
        canvas.drawCircle(300f, 1200f, 100f, mPaint)//坐标和半径
    }

    //画圆弧
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun drawArc(canvas: Canvas) {
        //第一种  需要使用api21  左上右下 开始角度 扫过角度 是否使用中心 画笔
        canvas.drawArc(300f, 1300f, 500f, 1500f, 0f, 90f, true, mPaint)


        //第二种

        val rectF = RectF(600f, 1300f, 800f, 1500f)
        canvas.drawArc(rectF, 0f, 90f, false, mPaint)

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.GREEN)

        drawPoint(canvas)//画点

        drawLine(canvas)//画直线

        drawRect(canvas)//画矩形

        drawRoundRect(canvas)//画圆角矩形

        drawOval(canvas)//画椭圆

        drawCircle(canvas)//画圆

        drawArc(canvas)//画圆弧

    }
}
