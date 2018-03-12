package com.xch.im.definetest.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.xch.im.definetest.R
import com.xch.im.definetest.utils.UiUtils

import java.util.*

/**
 *  Author:  ForrestHope
 *  Date:  2018/3/12 0012
 *  FileName:  ProgressView.java
 *  History:
 *
 **/
class ProgressView : View {

    private val TAG = "HopeLoadingView"
    // 淡白色
    private val WHITE_COLOR = -0x21c67
    // 橙色
    private val ORANGE_COLOR = -0x5800

    lateinit var mBitmapPaint: Paint
    lateinit var mWhitePaint: Paint
    lateinit var mOrangePaint: Paint

    var mProgress: Int = 0   //进度
    private val TOTAL_PROGRESS = 100  //总进度
    // 叶子飘动一个周期所花的时间
    private val LEAF_FLOAT_TIME: Long = 3000
    // 叶子旋转一周需要的时间
    private val LEAF_ROTATE_TIME: Long = 2000
    private var mProgress_width: Int = 0  //进度条宽度
    private var mCurrent_progress_position: Int = 0 //进度条当前位置
    private var mArcRadius: Int = 0  //进度条最左边的圆弧的半径

    private var mTotalWidth: Int = 0  //宽度
    private var mTotalHeight: Int = 0  //高度

    private lateinit var mWhiteRectf: RectF  //白色的矩形
    private lateinit var mOrangeRectf: RectF //橙色的矩形
    private lateinit var mArcRectf: RectF  //圆弧的矩形

    private lateinit var mOutSrcRect: Rect //原
    private lateinit var mOutDestRect: Rect  //目标

    private lateinit var mResources: Resources
    private lateinit var mLeafBitmap: Bitmap//叶子的bitmap
    private var mLeafWidth: Int = 0 //叶子的宽度
    private var mLeafHeight: Int = 0 //叶子的高度
    private lateinit var mOutBitmap: Bitmap//进度条背景的bitmap
    private var mOutWidth: Int = 0 //进度条背景的宽度
    private var mOutHeight: Int = 0 //进度条背景的高度


    private val LEFT_MARGIN = 9// 用于控制绘制的进度条距离左／上／下的距离

    private val RIGHT_MARGIN = 25// 用于控制绘制的进度条距离右的距离
    private var mLeftMargin: Int = 0  //进度条离左边的距离
    private var mRightMargin: Int = 0  //进度条离右边边的距离

    // arc的右上角的x坐标，也是矩形x坐标的起始点
    private var mArcRightLocation: Int = 0

    // 叶子飘动一个周期所花的时间
    private var mLeafFloatTime: Long = LEAF_FLOAT_TIME
    // 叶子旋转一周需要的时间
    private var mLeafRotateTime: Long = LEAF_ROTATE_TIME

    // 产生出的叶子信息
    private lateinit var mLeafInfos: List<Leaf>
    // 用于产生叶子信息
    private lateinit var mLeafFactory: LeafFactory
    // 用于控制随机增加的时间不抱团
    private var mAddTime: Int = 0
    // 中等振幅大小
    private val MIDDLE_AMPLITUDE = 13
    // 不同类型之间的振幅差距
    private val AMPLITUDE_DISPARITY = 5
    // 中等振幅大小
    private var mMiddleAmplitude = MIDDLE_AMPLITUDE
    // 振幅差
    private var mAmplitudeDisparity = AMPLITUDE_DISPARITY


    /**
     * 叶子对象，用来记录叶子主要数据
     *
     * @author Ajian_Studio
     */
    private inner class Leaf {

        // 在绘制部分的位置
        internal var x: Float = 0.toFloat()
        internal var y: Float = 0.toFloat()
        // 控制叶子飘动的幅度
        internal var type: StartType? = null
        // 旋转角度
        internal var rotateAngle: Int = 0
        // 旋转方向--0代表顺时针，1代表逆时针
        internal var rotateDirection: Int = 0
        // 起始时间(ms)
        internal var startTime: Long = 0
    }

    private enum class StartType {
        LITTLE, MIDDLE, BIG
    }

    private inner class LeafFactory {
        private val MAX_LEAFS = 8
        internal var random = Random()
         fun generateLeaf(): Leaf {
            var leaf=Leaf()
            val randomType = random.nextInt(3)
            // 随时类型－ 随机振幅
            var type = StartType.MIDDLE
            when (randomType) {
                0 -> { }
                1 -> type = StartType.LITTLE
                2 -> type = StartType.BIG
                else -> { }
            }
            leaf!!.type = type
            // 随机起始的旋转角度
            leaf!!.rotateAngle = random.nextInt(360)
            // 随机旋转方向（顺时针或逆时针）
            leaf!!.rotateDirection = random.nextInt(2)
            // 为了产生交错的感觉，让开始的时间有一定的随机性

            mLeafFloatTime = if (mLeafFloatTime <= 0) LEAF_FLOAT_TIME else mLeafFloatTime
            mAddTime += random.nextInt((mLeafFloatTime * 2).toInt())
            leaf.startTime = System.currentTimeMillis() + mAddTime
            return leaf
        }

        fun generateLeafs(): List<Leaf> {
            return generateLeafs(MAX_LEAFS)
        }

        fun generateLeafs(leafSize: Int): List<Leaf> {
            val leafs = LinkedList<Leaf>()
            for (i in 0 until leafSize) {
                leafs.add(generateLeaf())
            }
            return leafs
        }

    }


    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mResources = resources
        mLeftMargin = UiUtils.dipToPx(context, LEFT_MARGIN)  //dp转为px
        mRightMargin = UiUtils.dipToPx(context, RIGHT_MARGIN)

        mLeafFloatTime = LEAF_FLOAT_TIME
        mLeafRotateTime = LEAF_ROTATE_TIME

        initPaint()
        initBitmap()

        mLeafFactory = LeafFactory()
        mLeafInfos = mLeafFactory.generateLeafs()

    }

    private fun initBitmap() {
        mLeafBitmap = (mResources.getDrawable(R.drawable.leaf) as BitmapDrawable).bitmap
        mLeafWidth = mLeafBitmap.getWidth()
        mLeafHeight = mLeafBitmap.getHeight()

        mOutBitmap = (mResources.getDrawable(R.drawable.leaf_kuang) as BitmapDrawable).bitmap
        mOutWidth = mOutBitmap.getWidth()
        mOutHeight = mOutBitmap.getHeight()

    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        mBitmapPaint = Paint()
        mBitmapPaint.isAntiAlias = true //抗锯齿
        mBitmapPaint.isDither = true  //防抖动
        mBitmapPaint.isFilterBitmap = true  //进行滤波处理


        mWhitePaint = Paint()
        mWhitePaint.color = WHITE_COLOR
        mWhitePaint.isAntiAlias = true

        mOrangePaint = Paint()
        mOrangePaint.color = ORANGE_COLOR
        mOrangePaint.isAntiAlias = true


    }

    //画进度条 和叶子，处理层级关系 把叶子画在进度条里
    private fun drawProgressAndLeaf(canvas: Canvas) {
        if (mProgress >= TOTAL_PROGRESS) {
            mProgress = 0
        }
        //进度条宽度（不是高度） 计算出进度条的位置
        mCurrent_progress_position = mProgress_width * mProgress / TOTAL_PROGRESS

        //当前位置小于圆弧半径的时候， 进度条处于第一个部分
        if (mCurrent_progress_position < mArcRadius) {
            Log.i(TAG, "mProgress=" + mProgress + "--mCurrent_progress_position=" + mCurrent_progress_position + "--")
            //绘制白色圆弧
            canvas.drawArc(mArcRectf, 90f, 180f, false, mWhitePaint)

            //绘制白色矩形
            mWhiteRectf.left = mArcRightLocation.toFloat()
            canvas.drawRect(mWhiteRectf, mWhitePaint)

            // 绘制叶子
            drawLeafs(canvas)

            // 3.绘制棕色 ARC
            // 单边角度
            val angle = Math.toDegrees(Math.acos(((mArcRadius - mCurrent_progress_position) / mArcRadius.toFloat()).toDouble())).toInt()
            // 起始的位置
            val startAngle = 180 - angle
            // 扫过的角度
            val sweepAngle = 2 * angle
            Log.i(TAG, "startAngle = " + startAngle)
            canvas.drawArc(mArcRectf, startAngle.toFloat(), sweepAngle.toFloat(), false, mOrangePaint)
        } else {
            Log.i(TAG, "mProgress = " + mProgress + "---transfer-----mCurrentProgressPosition = "
                    + mCurrent_progress_position
                    + "--mArcProgressWidth" + mArcRadius)
            // 1.绘制white RECT
            // 2.绘制Orange ARC
            // 3.绘制orange RECT
            // 这个层级进行绘制能让叶子感觉是融入棕色进度条中

            // 1.绘制white RECT
            mWhiteRectf.left = mCurrent_progress_position.toFloat()
            canvas.drawRect(mWhiteRectf, mWhitePaint)
            // 绘制叶子
            drawLeafs(canvas)
            // 2.绘制Orange ARC
            canvas.drawArc(mArcRectf, 90f, 180f, false, mOrangePaint)
            // 3.绘制orange RECT
            mOrangeRectf.left = mArcRightLocation.toFloat()
            mOrangeRectf.right = mCurrent_progress_position.toFloat()
            canvas.drawRect(mOrangeRectf, mOrangePaint)
        }
    }

    private fun drawLeafs(canvas: Canvas) {
        mLeafRotateTime = if (mLeafRotateTime <= 0) LEAF_ROTATE_TIME else mLeafRotateTime

        val currentTime = System.currentTimeMillis()

        for (i in mLeafInfos.indices) {
            val leaf = mLeafInfos[i]
            if (currentTime > leaf.startTime && leaf.startTime != 0L) {
                // 绘制叶子－－根据叶子的类型和当前时间得出叶子的（x，y）
                getLeafLocation(leaf, currentTime)
                // 根据时间计算旋转角度
                canvas.save()
                // 通过Matrix控制叶子旋转
                val matrix = Matrix()
                val transX = mLeftMargin + leaf.x
                val transY = mLeftMargin + leaf.y
                Log.i(TAG, "left.x = " + leaf.x + "--leaf.y=" + leaf.y)
                matrix.postTranslate(transX, transY)
                // 通过时间关联旋转角度，则可以直接通过修改LEAF_ROTATE_TIME调节叶子旋转快慢
                val rotateFraction = (currentTime - leaf.startTime) % mLeafRotateTime / mLeafRotateTime.toFloat()
                val angle = (rotateFraction * 360).toInt()
                // 根据叶子旋转方向确定叶子旋转角度
                val rotate = if (leaf.rotateDirection == 0)
                    angle + leaf.rotateAngle
                else
                    -angle + leaf.rotateAngle
                matrix.postRotate(rotate.toFloat(), transX + mLeafWidth / 2, transY + mLeafHeight / 2)
                canvas.drawBitmap(mLeafBitmap, matrix, mBitmapPaint)
                canvas.restore()
            } else {
                continue
            }
        }
    }

    private fun getLeafLocation(leaf: Leaf, currentTime: Long) {
        val intervalTime = currentTime - leaf.startTime
        mLeafFloatTime = if (mLeafFloatTime <= 0) LEAF_FLOAT_TIME else mLeafFloatTime
        if (intervalTime < 0) {
            return
        } else if (intervalTime > mLeafFloatTime) {
            leaf.startTime = System.currentTimeMillis() + Random().nextInt(mLeafFloatTime.toInt())
        }

        val fraction = intervalTime.toFloat() / mLeafFloatTime
        leaf.x = (mProgress_width - mProgress_width * fraction).toInt().toFloat()
        leaf.y = getLocationY(leaf).toFloat()
    }

    // 通过叶子信息获取当前叶子的Y值
    private fun getLocationY(leaf: Leaf): Int {
        // y = A(wx+Q)+h
        val w = (2.toFloat() * Math.PI / mProgress_width).toFloat()
        var a = mMiddleAmplitude.toFloat()
        when (leaf.type) {
            StartType.LITTLE ->
                // 小振幅 ＝ 中等振幅 － 振幅差
                a = (mMiddleAmplitude - mAmplitudeDisparity).toFloat()
            StartType.MIDDLE -> a = mMiddleAmplitude.toFloat()
            StartType.BIG ->
                // 小振幅 ＝ 中等振幅 + 振幅差
                a = (mMiddleAmplitude + mAmplitudeDisparity).toFloat()
            else -> {
            }
        }
        Log.i(TAG, "---a = " + a + "---w = " + w + "--leaf.x = " + leaf.x)
        return (a * Math.sin((w * leaf.x).toDouble())).toInt() + mArcRadius * 2 / 3
    }

    /**
     * 设置中等振幅
     *
     * @param amplitude
     */
    fun setMiddleAmplitude(amplitude: Int) {
        this.mMiddleAmplitude = amplitude
    }

    /**
     * 设置振幅差
     *
     * @param disparity
     */
    fun setMplitudeDisparity(disparity: Int) {
        this.mAmplitudeDisparity = disparity
    }

    /**
     * 获取中等振幅
     *
     * @param amplitude
     */
    fun getMiddleAmplitude(): Int {
        return mMiddleAmplitude
    }

    /**
     * 获取振幅差
     *
     * @param disparity
     */
    fun getMplitudeDisparity(): Int {
        return mAmplitudeDisparity
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    fun setProgress(progress: Int) {
        this.mProgress = progress
        postInvalidate()
    }

    /**
     * 设置叶子飘完一个周期所花的时间
     *
     * @param time
     */
    fun setLeafFloatTime(time: Long) {
        this.mLeafFloatTime = time
    }

    /**
     * 设置叶子旋转一周所花的时间
     *
     * @param time
     */
    fun setLeafRotateTime(time: Long) {
        this.mLeafRotateTime = time
    }

    /**
     * 获取叶子飘完一个周期所花的时间
     */
    fun getLeafFloatTime(): Long {
        mLeafFloatTime = if (mLeafFloatTime == 0L) LEAF_FLOAT_TIME else mLeafFloatTime
        return mLeafFloatTime
    }

    /**
     * 获取叶子旋转一周所花的时间
     */
    fun getLeafRotateTime(): Long {
        mLeafRotateTime = if (mLeafRotateTime == 0L) LEAF_ROTATE_TIME else mLeafRotateTime
        return mLeafRotateTime
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // 绘制进度条和叶子
        // 之所以把叶子放在进度条里绘制，主要是层级原因
        if (canvas != null) {
            drawProgressAndLeaf(canvas)
        }
        // drawLeafs(canvas);

        canvas!!.drawBitmap(mOutBitmap, mOutSrcRect, mOutDestRect, mBitmapPaint)

        postInvalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mTotalWidth = w
        mTotalHeight = h

        mProgress_width = mTotalWidth - mLeftMargin - mRightMargin //进度条的总宽度

        mArcRadius = (mTotalHeight - 2 * mLeftMargin) / 2  //圆弧的半径 =  （总的高度-2*圆弧到控件最左边的距离）/2

        mOutSrcRect = Rect(0, 0, mOutWidth, mOutHeight)

        mOutDestRect = Rect(0, 0, mTotalWidth, mTotalHeight)

        mWhiteRectf = RectF((mLeftMargin + mCurrent_progress_position).toFloat(), mLeftMargin.toFloat(), (mTotalWidth - mRightMargin).toFloat(), (mTotalHeight - mLeftMargin).toFloat())

        mOrangeRectf = RectF((mLeftMargin + mArcRadius).toFloat(), mLeftMargin.toFloat(), mCurrent_progress_position.toFloat(), (mTotalHeight - mLeftMargin).toFloat())

        mArcRectf = RectF(mLeftMargin.toFloat(), mLeftMargin.toFloat(), (mLeftMargin + 2 * mArcRadius).toFloat(),
                (mTotalHeight - mLeftMargin).toFloat())
        mArcRightLocation = mLeftMargin + mArcRadius
    }
}