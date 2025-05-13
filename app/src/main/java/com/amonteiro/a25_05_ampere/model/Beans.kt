package com.amonteiro.a25_05_ampere.model



data class PictureBean(val id:Int, val url: String, val title: String, val longText: String)

class RandomName {
    private val list = arrayListOf("Toto", "tata", "bob")
    private var oldValue = ""


    fun add(name: String?) = if (!name.isNullOrBlank() && name !in list) list.add(name) else false


    fun addAll(vararg nameList: String) {
        for (name in nameList) {
            add(name)
        }
    }

    fun addAllV2(vararg nameList: String) = nameList.forEach { add(it) }

    //version simple
    fun next() = list.random()

    //version pas 2 identique de suite
    fun nextDiff(): String {
        var newValue = next()
        while(newValue == oldValue) {
            newValue = next()
        }

        oldValue = newValue
        return oldValue
    }

    //Idem sur une ligne
    //Also retourne l'objet sur lequel il est appelé
    fun nextDiffV2() =   list.filter { it != oldValue }.random().also{ oldValue = it}

    //Retourne 2 noms différents
    fun next2() = Pair(nextDiff(), nextDiff())
}

class ThermometerBean(val min: Int = 0, val max: Int = 100, value: Int = 0) {

    var value = value.coerceIn(min, max)
        set(newValue) {
            field = newValue.coerceIn(min, max)
        }

    init {
        this.value = value
    }

    companion object {
        fun getCelsiusThermometer() = ThermometerBean(-30, 50, 0)
        fun getFahrenheitThermometer() = ThermometerBean(20, 120, 32)
    }

    override fun toString(): String {
        return "ThermometerBean(value=$value)"
    }


}

data class Toto(var name: String = "", var email: String = "blabla") {

}

fun main() {

    var t = ThermometerBean.getCelsiusThermometer()

    var t1 = ThermometerBean(min = -20, max = 50, value = 0)
    println("Température de ${t1.value}") // 0

    //Cas qui marche
    t1.value = 10
    println("Température de ${t1.value}") // 10 attendu

    //Borne minimum
    t1.value = -30
    println("Température de ${t1.value}") // -20 attendu

    //Borne maximum
    t1.value = 100
    println("Température de ${t1.value}") // 50 attendu

    //Pour les plus rapides : Cas de départ
    t1 = ThermometerBean(min = -20, max = 50, value = -100)
    println("Température de ${t1.value}") // -20 attendu
}