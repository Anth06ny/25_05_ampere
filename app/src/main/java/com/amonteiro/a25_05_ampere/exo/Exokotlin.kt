package com.amonteiro.a25_05_ampere.exo

import kotlin.random.Random

var v2: String? = "toto"


fun main() {

    repeat(5) {

    }

    println("helloworld")

    var v1 = "toto"
    println(v1.uppercase())

    val finalV2 = v2
    if(finalV2 != null) {
        println(finalV2.uppercase())
    }
    else {
        println(null)
    }

    var v3:String? = null
    println(v3!!.uppercase())

    var v4 : Int? = null
    if(Random.nextBoolean()){
        v4 = Random.nextInt(10)
    }
    println(v4 ?: "Pas de valuer")

    var v5 = v3 + v3

    if(v3.isNullOrBlank() ) {
        println("v3 est null")
    }

    println(boulangerie(nbSand = 5, nbCroi = 2))
    println(boulangerie(2, 0, 5))

}

fun boulangerie(nbCroi:Int = 0, nbBag:Int =0, nbSand:Int =0): Double
    = nbCroi * PRICE_CROISSANT + nbBag * PRICE_BAGUETTE + nbSand * PRICE_SANDWITCH


fun String?.myIsNullOrBlank() : Boolean {
    return this == null || this.isNotBlank()
}