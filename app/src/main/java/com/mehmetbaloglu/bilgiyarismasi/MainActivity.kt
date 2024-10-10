package com.mehmetbaloglu.bilgiyarismasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mehmetbaloglu.bilgiyarismasi.ui.screens.HomeScreen
import com.mehmetbaloglu.bilgiyarismasi.ui.theme.BilgiYarismasiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BilgiYarismasiTheme {
                HomeScreen()

            }
        }
    }
}

