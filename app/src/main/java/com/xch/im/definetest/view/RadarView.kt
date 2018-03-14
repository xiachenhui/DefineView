package com.xch.im.definetest.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.R.attr.angle
import android.R.attr.radius
import android.R.attr.centerY
import android.R.attr.centerX
import android.R.attr.angle
import android.R.attr.radius
import android.R.attr.centerY
import android.R.attr.centerX
import android.support.v4.view.ViewCompat.setAlpha
import android.R.attr.centerY
import android.R.attr.angle
import android.R.attr.radius
import android.R.attr.centerX
import android.R.attr.data
import android.R.attr.data
import android.graphics.Color


/**
 *  Author:  ForrestHope
 *  Date:  2018/3/14 0014
 *  FileName:  RadarView.java
 *  History: 雷达图
 *
 **/
class RadarView : View {
    constructor(context: Context?) : super(context) {
        initPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaint()
    }


    private var mCount: Int = 6  //数据最大个数 --多边形的边的条数
    private var angle = (Math.PI * 2 / mCount)  // 多边形的内角和为360度 这可以计算出正多边形每个角的角度

    private var radius: Float = 0.toFloat()                   //网格最大半径
    private var centerX: Int = 0                  //中心X
    private var centerY: Int = 0                  //中心Y
    private var titles = arrayOf("a", "b", "c", "d", "e", "f")
    private var data = doubleArrayOf(100.0, 60.0, 60.0, 60.0, 100.0, 50.0, 10.0, 20.0) //各维度分值
    private var maxValue = 100f             //数据最大值
    private lateinit var mainPaint: Paint               //雷达区画笔
    private lateinit var valuePaint: Paint             //数据区画笔
    private lateinit var textPaint: Paint              //文本画笔

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = Math.min(h, w) / 2 * 0.9f;
        //中心坐标
        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();
    }

    private fun initPaint() {
        mainPaint = Paint()
        mainPaint!!.style = Paint.Style.STROKE
        mainPaint!!.isAntiAlias = true
        mainPaint!!.strokeWidth = 5f
        mainPaint!!.color = Color.DKGRAY

        valuePaint = Paint()
        valuePaint!!.style = Paint.Style.STROKE
        valuePaint!!.color = Color.BLUE
        valuePaint!!.isAntiAlias = true
        valuePaint!!.strokeWidth = 5f

        textPaint = Paint()
        textPaint!!.style = Paint.Style.STROKE
        textPaint!!.color = Color.DKGRAY
        textPaint!!.isAntiAlias = true
        textPaint!!.strokeWidth = 5f
        textPaint.textSize = 50f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawPolygon(canvas!!)
        drawLines(canvas)
        drawText(canvas)
        drawRegion(canvas)
    }

    //画正多边形  雷达
    private fun drawPolygon(canvas: Canvas) {
        var path = Path()
        val r = radius / (mCount - 1) //蜘蛛丝之间的距离

        for (i in 1..mCount) { //中心点不用绘制
            var currR = r * i  //当前半径
            path.reset()
            for (j in 0..mCount) {
                if (j == 0) {
                    path.moveTo(centerX + currR, centerY.toFloat())
                } else {
                    //根据半径 计算每个点的坐标
                    var x = centerX + currR * Math.cos((angle * j).toDouble())
                    var y = centerY + currR * Math.sin((angle * j).toDouble())
                    path.lineTo(x.toFloat(), y.toFloat())
                }
            }
            path.close()
            canvas.drawPath(path, mainPaint)
        }

    }

    //画直线  中心到最外面的末端的直线
    private fun drawLines(canvas: Canvas) {
        val path = Path()
        for (i in 0..mCount) {
            path.reset()
            path.moveTo(centerX.toFloat(), centerY.toFloat())
            val x = (centerX + radius * Math.cos((angle * i).toDouble())).toFloat()
            val y = (centerY + radius * Math.sin((angle * i).toDouble())).toFloat()
            path.lineTo(x, y)
            canvas.drawPath(path, mainPaint)
        }
    }

    /**
     * 绘制文字
     * @param canvas
     */
    private fun drawText(canvas: Canvas) {
        val fontMetrics = textPaint!!.getFontMetrics()
        val fontHeight = fontMetrics.descent - fontMetrics.ascent
        for (i in 0 until mCount) {
            val x = (centerX + (radius + fontHeight / 2) * Math.cos((angle * i))).toFloat()
            val y = (centerY + (radius + fontHeight / 2) * Math.sin((angle * i))).toFloat()
            if (angle * i >= 0 && angle * i <= Math.PI / 2) {//第4象限
                canvas.drawText(titles[i], x, y, textPaint)
            } else if (angle * i >= 3 * Math.PI / 2 && angle * i <= Math.PI * 2) {//第3象限
                canvas.drawText(titles[i], x, y, textPaint)
            } else if (angle * i > Math.PI / 2 && angle * i <= Math.PI) {//第2象限
                val dis = textPaint.measureText(titles[i])//文本长度
                canvas.drawText(titles[i], x - dis, y, textPaint)
            } else if (angle * i >= Math.PI && angle * i < 3 * Math.PI / 2) {//第1象限
                val dis = textPaint.measureText(titles[i])//文本长度
                canvas.drawText(titles[i], x - dis, y, textPaint)
            }
        }
    }

    /**
     * 绘制区域
     * @param canvas
     */
    private fun drawRegion(canvas: Canvas) {
        val path = Path()
        valuePaint!!.setAlpha(255)
        for (i in 0 until mCount) {
            val percent = data[i] / maxValue
            val x = (centerX + radius.toDouble() * Math.cos((angle * i)) * percent).toFloat()
            val y = (centerY + radius.toDouble() * Math.sin((angle * i)) * percent).toFloat()
            if (i == 0) {
                path.moveTo(x, centerY.toFloat())
            } else {
                path.lineTo(x, y)
            }
            //绘制小圆点
            canvas.drawCircle(x, y, 10f, valuePaint)
        }
        valuePaint!!.setStyle(Paint.Style.STROKE)
        canvas.drawPath(path, valuePaint)
        valuePaint!!.setAlpha(127)
        //绘制填充区域
        valuePaint!!.setStyle(Paint.Style.FILL_AND_STROKE)
        canvas.drawPath(path, valuePaint)
        path.close()
    }

    //设置标题
    fun setTitles(titles: Array<String>) {
        this.titles = titles
    }

    //设置数值
    fun setData(data: DoubleArray) {
        this.data = data
    }

    //设置最大数值
    fun setMaxValue(maxValue: Float) {
        this.maxValue = maxValue
    }

    //设置蜘蛛网颜色
    fun setMainPaintColor(color: Int) {
        mainPaint!!.setColor(color)
    }

    //设置标题颜色
    fun setTextPaintColor(color: Int) {
        textPaint!!.setColor(color)
    }

    //设置覆盖局域颜色
    fun setValuePaintColor(color: Int) {
        valuePaint!!.setColor(color)
    }
}