package com.amonteiro.a25_05_ampere.utils

import com.amonteiro.a25_05_ampere.model.PictureBean
import com.google.gson.Gson

fun main() {
    var res : PictureBean? = PictureBean(1, "toto", "", "")
    res= null
   println(res.toJson())
}

fun Any?.toJson() = this?.let { Gson().toJson(this)} ?: "{}"
