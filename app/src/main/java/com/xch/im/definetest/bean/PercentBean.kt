package com.xch.im.definetest.bean

/**
 * Created by Administrator on 2018/3/10 0010.
 * 百分比的Bean
 */
class PercentBean {

    var name: String? = null
    var value: Float?

    var percent: Float? = 0F//百分比
    var color = 0; //颜色
    var angle: Float? = 0F//角度


    constructor(name: String?, value: Float?) {
        this.name = name
        this.value = value
    }


}