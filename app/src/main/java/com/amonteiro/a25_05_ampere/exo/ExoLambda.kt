package com.amonteiro.a25_05_ampere.exo

import android.R.attr.action
import com.amonteiro.a25_05_ampere.model.ThermometerBean
import kotlin.math.abs


class MyLiveData<T>(value :T? = null) {

    var value  = value
        set(newValue) {
            field = newValue
            actions.forEach { it(newValue) }
        }

    var actions = ArrayList<(T?)->Unit>()
}

fun main() {
    var toto = MyLiveData(ThermometerBean.getCelsiusThermometer())

    toto.actions.add{
        println(it)
    }

    toto.actions.add{
        println(it)
    }

    toto.value = ThermometerBean.getFahrenheitThermometer()

}

fun exo1() {
    val lower: (String) -> Unit = { it: String -> println(it.lowercase()) }
    val lower2 = { it: String -> println(it.lowercase()) }
    val lower3: (String) -> Unit = { it -> println(it.lowercase()) }
    val lower4: (String) -> Unit = { println(it.lowercase()) }

    var minToMinHour: ((Int?) -> Pair<Int, Int>?)? = { if (it != null) Pair(it / 60, it % 60) else null }
    var minToMinHourv2: (Int?) -> Pair<Int, Int>? = { it?.let { Pair(it / 60, it % 60) } }

    println(minToMinHour?.invoke(123))
    println(minToMinHour?.invoke(null))
    minToMinHour = null
    println(minToMinHour?.invoke(null))

    val compareUsersByName = { u1: User, u2: User -> if (u1.name.lowercase() <= u2.name.lowercase()) u1 else u2 }
    val compareUserByOld = { u1: User, u2: User -> if (u1.old >= u2.old) u1 else u2 }

    val u1 = User("Bob", 19)
    val u2 = User("Toto", 45)
    val u3 = User("Charles", 26)
    compareUsers(u1, u2, u3, compareUsersByName)
    compareUsers(u1, u2, u3, { a, b -> if (abs(30 - a.old) < abs(30 - b.old)) a else b })

    arrayOf(u1).joinToString()
}

data class User(var name: String, var old: Int)


fun compareUsers(u1: User, u2: User, u3: User, comparator: (User, User) -> User) =
    comparator(comparator(u1, u2), u3)

data class PersonBean(var name: String, var note: Int)

fun exo3() {
    val list = arrayListOf(
        PersonBean("toto", 16),
        PersonBean("Tata", 20),
        PersonBean("Toto", 8),
        PersonBean("Charles", 14)
    )

    var list2 = list
    var list3 = ArrayList(list)
    var list4 = list.map { it.copy() }



    println("Afficher la sous liste de personne ayant 10 et +")
    //println(list.filter { it.note >=10 })
    //Pour un affichage de notre choix
    println(list.filter { it.note >= 10 }.joinToString("\n") { "-${it.name} : ${it.note}" })

    //TODO
    println("\n\nAfficher combien il y a de Toto dans la classe ?")
    println("\n\nAfficher combien de Toto ayant la moyenne (10 et +)")
    println("\n\nAfficher combien de Toto ont plus que la moyenne de la classe")
    val lambdaNomToto = { it: PersonBean -> it.name.equals("toto", true) }
    val moyenne = list.map { it.note }.average()
    println(list.count { lambdaNomToto(it) && it.note >= moyenne })

    val nb = list.map { it.note }.average().let { average ->
        list.count { lambdaNomToto(it) && it.note >= average }
    }


    println("\n\nAfficher la list triée par nom sans doublon")
    println("\n\nAjouter un point a ceux n’ayant pas la moyenne (<10)")
    println("\n\nAjouter un point à tous les Toto")
    println("\n\nRetirer de la liste ceux ayant la note la plus petite. (Il peut y en avoir plusieurs)")
    println("\n\nAfficher les noms de ceux ayant la moyenne(10et+) par ordre alphabétique")

    //TODO Créer une variable isToto contenant une lambda qui teste si un PersonBean s'appelle Toto

    println("\n\nDupliquer la liste ainsi que tous les utilisateurs (nouvelle instance) qu'elle contient")
    println("\n\nAfficher par notes croissantes les élèves ayant eu cette note comme sur l'exemple")
}
