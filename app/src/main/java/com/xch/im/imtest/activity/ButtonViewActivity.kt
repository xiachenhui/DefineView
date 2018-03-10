package com.xch.im.imtest.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xch.im.imtest.R
import com.xch.im.imtest.view.RectToCircle
import kotlinx.android.synthetic.main.activity_button_view.*

class ButtonViewActivity : AppCompatActivity(), RectToCircle.AnimationButtonListener {
    override fun onClickListener() {
        start_view.start()
    }

    override fun animationFinish() {
        start_view.reset()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button_view)
        initListener()
    }

    private fun initListener() {
        start_view.setAnimationButtonListener(this)
    }
}
