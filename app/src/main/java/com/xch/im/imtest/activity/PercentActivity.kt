package com.xch.im.imtest.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.xch.im.imtest.R
import com.xch.im.imtest.bean.PercentBean
import kotlinx.android.synthetic.main.activity_percent.*

class PercentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_percent)
        initData()
    }

    private fun initData() {
        var per1 = PercentBean("第一", 10f)
        var per2 = PercentBean("第二", 20f)
        var per3 = PercentBean("第三", 30f)
        var per4 = PercentBean("第四", 40f)
        var per5 = PercentBean("第五", 50f)
        var perData= ArrayList<PercentBean>()
        perData.add(per1)
        perData.add(per2)
        perData.add(per3)
        perData.add(per4)
        perData.add(per5)

        percent_view.setStartAngle(-90f)
        percent_view.setData(perData)
    }
}
