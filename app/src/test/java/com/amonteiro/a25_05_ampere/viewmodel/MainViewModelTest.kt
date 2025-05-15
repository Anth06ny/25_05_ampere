package com.amonteiro.a25_05_ampere.viewmodel

import com.amonteiro.a25_05_ampere.model.DescriptionBean
import com.amonteiro.a25_05_ampere.model.TempBean
import com.amonteiro.a25_05_ampere.model.WeatherBean
import com.amonteiro.a25_05_ampere.model.WeatherRepository
import com.amonteiro.a25_05_ampere.model.Wind
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class MainViewModelTest {

    //Dispatcher pour les Coroutines, pilotables à  l'aide de advanceUntilIdle()
    private val testDispatcher = StandardTestDispatcher()
    private val viewModel = MainViewModel(testDispatcher)


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadWeather() = runTest(testDispatcher) {


        // Vérifier l'état avant le lancement de la coroutine
        assertFalse(viewModel.runInProgress.value)

        mockkObject(WeatherRepository)
        every { WeatherRepository.loadWeathers("Paris") }.returns(getParisFakeResult())

        // Appeler la méthode à tester
        viewModel.loadWeathers("Paris")

        // Vérifier que runInProgress est true
        assertTrue(viewModel.runInProgress.value)

        // Avancer l'exécution des coroutines jusqu'à l'état courant
        advanceUntilIdle()

        //On vérifie que loadWeathers("Paris") à bien été appelé
        verify { WeatherRepository.loadWeathers("Paris") }

        //On vérifie qu'aucun autre appel à WeatherRepository à été effectué
        confirmVerified(WeatherRepository)


        // Vérifier que runInProgress est false
        assertFalse(viewModel.runInProgress.value)

        // Qu'on a des éléments dans la liste
        assertFalse(viewModel.dataList.value.isEmpty())
    }

    fun getParisFakeResult() = arrayListOf(
        WeatherBean(
            id = 1,
            name = "Paris",
            main = TempBean(temp = 20.0),
            wind = Wind(speed = 5.0),
            weather = listOf(DescriptionBean(description = "Ensoleillé", icon = "01d"))
        )
    )
}