package com.xch.im.definetest.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Picture
import android.util.AttributeSet
import android.view.View

/**
 *@作者 ForrestHope
 *
 *@创建日期 2018/3/12 0012 21:24
 *
 *@描述 画Picture
 *
 */
class DrawPicture : View {

    //step1 创建Picture
    var mPicture: Picture = Picture()
    lateinit var mPaint: Paint

    constructor(context: Context?) : super(context) {
        startRecord()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        startRecord()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        startRecord()
    }


    //setp2 开始录制
    private fun startRecord() {
        //返回值为一个Canvas
        val canvas = mPicture.beginRecording(700, 700)


        mPaint = Paint()
        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.FILL

        //在Canvas中进行操作

        canvas.translate(400f, 200f)

        canvas.drawCircle(0f, 0f, 100f, mPaint)

        mPicture.endRecording()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
       canvas!!.drawPicture(mPicture)
    }

}