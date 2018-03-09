package com.xch.im.imtest.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.xch.im.imtest.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mBtn_button: Button? = null


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.main_btn_button -> startActivity(Intent(this, ButtonViewActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()

    }


    private fun initListener() {
        main_btn_button.setOnClickListener(this)
    }
}


