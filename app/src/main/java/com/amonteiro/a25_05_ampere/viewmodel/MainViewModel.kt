package com.amonteiro.a25_05_ampere.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amonteiro.a25_05_ampere.model.PictureBean
import com.amonteiro.a25_05_ampere.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

suspend fun main() {
    val viewModel = MainViewModel()
    viewModel.loadWeathers("Toulouse")
    viewModel.loadWeathers("Nice")
    viewModel.loadWeathers("Marseille")
    viewModel.loadWeathers("Toulon")

    while (viewModel.runInProgress.value) {
        delay(500)
        println("Attente...")
    }

    println("ErrorMessage : ${viewModel.errorMessage.value}")
    //Affichage de la liste (qui doit être remplie) contenue dans la donnée observable
    println("List : ${viewModel.dataList.value.joinToString("\n")}")

}

class MainViewModel : ViewModel() {
    //MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<PictureBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    fun loadWeathers(cityName: String) {

        runInProgress.value = true
        errorMessage.value = ""

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val listWeather = WeatherRepository.loadWeathers(cityName)
                dataList.value = listWeather.map { weather ->
                    PictureBean(
                        id = weather.id,
                        url = weather.weather.firstOrNull()?.icon ?: "",
                        title = weather.name,
                        longText = """
                    Il fait ${weather.main.temp}° à ${weather.name} (id=${weather.id}) avec un vent de ${weather.wind.speed} m/s
                    -Description : ${weather.weather.firstOrNull()?.description ?: "-"}
                    -Icône : ${weather.weather.firstOrNull()?.icon ?: "-"}""".trimIndent()
                    )
                }
            }catch (e:Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur est survenue"
            }
            finally {
                runInProgress.value = false
            }
        }
    }
}