package com.xch.im.imtest.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.xch.im.imtest.bean.PercentBean

/**
 * Created by Administrator on 2018/3/10 0010.
 *
 * 饼状图
 */
class PercentView : View {
    lateinit var mPaint: Paint

    var mStartAngle: Float? = 0f//初始角度

    var mWidth: Int? = 0//宽

    var mHeight: Int? = 0//高

    lateinit var mPercentData: ArrayList<PercentBean>

    // 颜色表 (注意: 此处定义颜色使用的是ARGB，带Alpha通道的)
    private val mColors = intArrayOf(-0x330100, -0x9b6a13, -0x1cd9ca, -0x800000, -0x7f8000, -0x7397, -0x7f7f80, -0x194800, -0x830400)

    lateinit var mTextPaint: Paint

    constructor(context: Context) : super(context) {
        initPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaint()
    }

    //初始化画笔
    private fun initPaint() {
        mPaint = Paint();
        mPaint.strokeWidth = 10F
        mPaint.style = Paint.Style.FILL

        mTextPaint = Paint();
        mTextPaint.color = Color.BLACK
        mTextPaint.strokeWidth = 10f
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textSize = 40f
        mTextPaint.textAlign = Paint.Align.LEFT
        mTextPaint.isAntiAlias = true


    }

    //初始化数据
    private fun initData(percentData: ArrayList<PercentBean>) {
        if (percentData == null || percentData.size == 0) {
            return
        }


        var sumValue = 0F//总共的值
        for (percent in percentData) {
            sumValue += percent.value!!
            percent.color = mColors.get(percentData.indexOf(percent))
        }


        var sumAngle = 0F//总共的角度
        for (percent in percentData) {
            val percentAngle = percent.value!! / sumValue //百分比
            var angle = percentAngle * 360  //对应的角度

            percent.percent = percentAngle//设置百分比
            percent.angle = angle//设置角度
            sumAngle += angle;


        }

    }

    //设置数据
    fun setData(percentData: ArrayList<PercentBean>) {
        mPercentData = percentData
        initData(mPercentData)

    }

    //设置初始角度
    fun setStartAngle(startAngle: Float) {
        mStartAngle = startAngle
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (mPercentData == null) {
            return
        }
        var currentStartAngle = mStartAngle//当前起始角度

        if (canvas != null) {
            canvas.translate((mWidth!! / 2).toFloat(), (mHeight!! / 2).toFloat())//把画布坐标原点移动到中心
            var banjing = Math.min(mWidth!!, mHeight!!) / 2 * 0.8.toFloat()//以宽高的较小的值的一半的0.8位半径 避免屏幕装不下
            var rectf = RectF(-banjing, -banjing, banjing, banjing)  //一个矩形 在这个矩形内画圆
            for (percent in mPercentData) {
                mPaint.color = percent.color
                //开始画圆弧
                canvas.drawArc(rectf, currentStartAngle!!, percent.angle!!, true, mPaint)
                currentStartAngle += percent.angle!!//每次开始画的开始角度为已经画了的角度

                Log.i("位置", mHeight.toString())
                canvas.drawText(percent.name, 0f, -((1 + mPercentData.size - mPercentData.indexOf(percent)) * 100).toFloat(), mTextPaint)
            }
        }

    }


}