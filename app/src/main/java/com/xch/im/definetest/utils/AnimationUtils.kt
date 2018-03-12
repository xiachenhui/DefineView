package com.xch.im.definetest.utils

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

object AnimationUtils {

    fun initRotateAnimation(duration: Long,
                            fromAngle: Int, toAngle: Int,
                            isFillAfter: Boolean, repeatCount: Int): RotateAnimation {
        val mLoadingRotateAnimation = RotateAnimation(fromAngle.toFloat(), toAngle.toFloat(),
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f)
        val lirInterpolator = LinearInterpolator()
        mLoadingRotateAnimation.interpolator = lirInterpolator
        mLoadingRotateAnimation.duration = duration
        mLoadingRotateAnimation.fillAfter = isFillAfter
        mLoadingRotateAnimation.repeatCount = repeatCount
        mLoadingRotateAnimation.repeatMode = Animation.RESTART
        return mLoadingRotateAnimation
    }

    fun initRotateAnimation(isClockWise: Boolean, duration: Long,
                            isFillAfter: Boolean, repeatCount: Int): RotateAnimation {
        val endAngle: Int
        if (isClockWise) {
            endAngle = 360
        } else {
            endAngle = -360
        }
        val mLoadingRotateAnimation = RotateAnimation(0f, endAngle.toFloat(),
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f)
        val lirInterpolator = LinearInterpolator()
        mLoadingRotateAnimation.interpolator = lirInterpolator
        mLoadingRotateAnimation.duration = duration
        mLoadingRotateAnimation.fillAfter = isFillAfter
        mLoadingRotateAnimation.repeatCount = repeatCount
        mLoadingRotateAnimation.repeatMode = Animation.RESTART
        return mLoadingRotateAnimation
    }

    fun initAnimationDrawable(context: Context, drawableIds: IntArray,
                              durationTime: Int, isOneShot: Boolean): AnimationDrawable {
        val mAnimationDrawable = AnimationDrawable()
        for (i in drawableIds.indices) {
            val id = drawableIds[i]
            mAnimationDrawable.addFrame(context.resources.getDrawable(id), durationTime)
        }
        mAnimationDrawable.isOneShot = isOneShot
        return mAnimationDrawable
    }

    fun initAlphaAnimtion(context: Context, fromAlpha: Float, toAlpha: Float,
                          duration: Long): Animation {
        val alphaAnimation = AlphaAnimation(fromAlpha, toAlpha)
        alphaAnimation.duration = duration
        return alphaAnimation
    }

}
