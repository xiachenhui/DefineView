package com.xch.im.definetest.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation

import com.xch.im.definetest.R
import com.xch.im.definetest.utils.AnimationUtils
import kotlinx.android.synthetic.main.activity_progress.*

class ProgressActivity : AppCompatActivity(), View.OnClickListener {
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                0 -> {
                    progress_defineview.setProgress(mProgress)
                }
                1 -> {
                    mProgress = 0
                }
            }

        }
    }

    var mProgress: Int = 0
    lateinit var rotateAnimation: RotateAnimation
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.progress_clear -> {
                mProgress = 0
                progress_defineview.setProgress(mProgress)
            }
            R.id.progress_add -> {
                startProgress()
            }

        }
    }

    private fun startProgress() {
        progress_feng.startAnimation(rotateAnimation)
        Thread(Runnable {
            kotlin.run {
                while (mProgress < 100) {
                    SystemClock.sleep(300)
                    mProgress += 5
                    mHandler.sendEmptyMessage(0)
                    if (mProgress >= 100) {
                       mProgress=0
                    }
                }


            }
        }).start()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        initView()
        initListener()
    }

    private fun initListener() {
        progress_clear.setOnClickListener(this)
        progress_add.setOnClickListener(this)
    }

    private fun initView() {

        rotateAnimation = AnimationUtils.initRotateAnimation(false, 1500, true,
                Animation.INFINITE)

    }
}
