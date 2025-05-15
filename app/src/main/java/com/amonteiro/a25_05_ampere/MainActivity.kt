package com.amonteiro.a25_05_ampere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.amonteiro.a25_05_ampere.ui.screens.SearchScreen
import com.amonteiro.a25_05_ampere.ui.theme._25_05_ampereTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        println("onCreate")
        setContent {
            _25_05_ampereTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    SearchScreen(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }
}