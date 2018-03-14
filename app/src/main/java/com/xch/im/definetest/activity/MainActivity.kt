package com.xch.im.definetest.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.xch.im.definetest.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.main_btn_button -> startActivity(Intent(this, ButtonViewActivity::class.java))
            R.id.main_btn_test -> startActivity(Intent(this, DrawTestActivity::class.java))
            R.id.main_btn_percent -> startActivity(Intent(this, PercentActivity::class.java))
            R.id.main_btn_canvas -> startActivity(Intent(this, CanvasTestActivity::class.java))
            R.id.main_btn_picture -> startActivity(Intent(this, PictureActivity::class.java))
            R.id.main_btn_progress -> startActivity(Intent(this, ProgressActivity::class.java))
            R.id.main_btn_radar -> startActivity(Intent(this, RadarActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()

    }


    private fun initListener() {
        main_btn_button.setOnClickListener(this)
        main_btn_test.setOnClickListener(this)
        main_btn_percent.setOnClickListener(this)
        main_btn_canvas.setOnClickListener(this)
        main_btn_picture.setOnClickListener(this)
        main_btn_progress.setOnClickListener(this)
        main_btn_radar.setOnClickListener(this)
    }
}


