package com.amonteiro.a25_05_ampere.model

import org.junit.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class WeatherRepositoryTest {


    @Test
    fun loadWeatherNiceTest() {

        var result = WeatherRepository.loadWeathers("Nice")

        assertTrue( result.isNotEmpty(), "La liste est vide")

        for (city in result) {
            assertTrue( city.name.contains("Nice", true), "Le nom ne contient pas Nice",)
            assertTrue( city.main.temp in -40.0..60.0, "La température n'est pas entre -40 et 60°",)
            assertTrue(city.weather.isNotEmpty(), "La description est vide",)
            assertTrue( city.weather[0].icon.isNotBlank(),"Il n'y a pas d'icône")
        }
    }

    @Test
    fun loadWeathersEmptyString() {
        try {
            WeatherRepository.loadWeathers("")
            fail("L'appel avec une ville vide aurait du lever une exception")
        } catch (e: Exception) {
            //ok
        }
    }

    @Test(expected = Exception::class)
    fun loadWeathersEmptyString2() {
        WeatherRepository.loadWeathers("")
    }
}