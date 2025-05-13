package com.amonteiro.a25_05_ampere.model

import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.InputStreamReader

//Utilisation
suspend fun main() {
    //Lance la requête et met le corps de la réponse dans html
//    var html: String = WeatherRepository.sendGet("https://www.google.fr")
//    //Affiche l'HTML
//    println("html=$html")

//    for (user in WeatherRepository.loadRandomUsers()) {
//        println(
//            """
//        Il s'appelle ${user.name} pour le contacter :
//        Phone : ${user.coord?.phone ?: "-"}
//        Mail : ${user.coord?.mail ?:"-"}
//    """.trimIndent()
//        )
//    }

//    for (weather in WeatherRepository.loadWeathers("nice")) {
//        println(
//            """
//            Il fait ${weather.main.temp}° à ${weather.name} (id=${weather.id}) avec un vent de ${weather.wind.speed} m/s
//            -Description : ${weather.weather.firstOrNull()?.description ?: "-"}
//            -Icône : ${weather.weather.firstOrNull()?.icon ?: "-"}
//        """.trimIndent()
//        )
//    }

    runBlocking {
        launch {
            WeatherRepository.getWeathers("toulouse", "nice", "fghjkl", "",  "marseille")
                .filter { it.wind.speed < 10 }
                .catch { println("Erreur : ${it.message}" ) }
                .collect {
                    println(it)
                }

        }
    }


}


object WeatherRepository {
    val client = OkHttpClient()
    val gson = Gson()

    fun loadWeathers(cityName: String): List<WeatherBean> {
        val json = sendGet("https://api.openweathermap.org/data/2.5/find?q=$cityName&appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr")

        val weatherAPIResult = gson.fromJson(json, WeatherAPIResult::class.java)
        return weatherAPIResult.list.onEach {
            it.weather.forEach {
                it.icon = "https://openweathermap.org/img/wn/${it.icon}@4x.png"
            }
        }
    }


    fun getWeathers(vararg cities: String) = flow {
        cities.forEach {
            loadWeathers(it).forEach { emit(it) }
            delay(1000)
        }
    }

    fun loadWeathersOpti(cityName: String): List<WeatherBean> =
        sendGetOpti("https://api.openweathermap.org/data/2.5/find?q=$cityName&appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr").use {
            val inputStream = InputStreamReader(it.body.byteStream())
            val weatherAPIResult = gson.fromJson(inputStream, WeatherAPIResult::class.java)
            weatherAPIResult.list
        }


    fun loadRandomUsers(): Array<UserBean> {
        val json = sendGet("https://www.amonteiro.fr/api/randomusers")
        val user = gson.fromJson(json, Array<UserBean>::class.java)
        return user
    }

    fun loadRandomUser(): UserBean {
        val json = sendGet("https://www.amonteiro.fr/api/randomuser")
        val user = gson.fromJson(json, UserBean::class.java)
        return user
    }

    //Méthode qui prend en entrée une url, execute la requête
    //Retourne le code HTML/JSON reçu
    fun sendGet(url: String): String {
        println("url : $url")
        //Création de la requête
        val request = Request.Builder().url(url).build()
        //Execution de la requête
        return client.newCall(request).execute().use { //it:Response
            //use permet de fermer la réponse qu'il y ait ou non une exception
            //Analyse du code retour
            if (!it.isSuccessful) {
                throw Exception("Erreur serveur :${it.code}\n${it.body.string()}")
            }
            //Résultat de la requête
            it.body.string()
        }
    }

    fun sendGetOpti(url: String): Response {
        println("url : $url")
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw Exception("Réponse du serveur incorrect : ${response.code}\n${response.body.string()}")
        }
        return response //.body.string()
    }
}

/* -------------------------------- */
// WEATHER
/* -------------------------------- */
data class WeatherAPIResult(val list: List<WeatherBean>)
data class WeatherBean(
    val id: Int, val name: String, var main: TempBean,
    var weather: List<DescriptionBean>,
    var wind: Wind
)

data class TempBean(var temp: Double)
data class DescriptionBean(var description: String, var icon: String)
data class Wind(var speed: Double)

/* -------------------------------- */
// USER
/* -------------------------------- */

data class UserBean(var name: String, var age: Int, var coord: CoordBean?) {

}

data class CoordBean(var phone: String?, var mail: String?)