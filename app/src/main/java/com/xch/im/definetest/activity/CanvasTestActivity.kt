package com.xch.im.definetest.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.xch.im.definetest.R
import kotlinx.android.synthetic.main.activity_canvas_test.*

class CanvasTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_test)
        initView()
    }

    private fun initView() {
        canvas_test.setHourCount(6)
        canvas_test.setMintueCount(30)
    }
}
