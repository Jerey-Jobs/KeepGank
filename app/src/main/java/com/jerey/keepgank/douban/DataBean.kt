package com.jerey.keepgank.douban

/**
 * Created by xiamin on 8/16/17.
 */

data class DataBean{
    var name = ""
    var age = 0

    fun setName(str : String){
        if (str.equals(name)){
            return
        }
        name = str
    }
}