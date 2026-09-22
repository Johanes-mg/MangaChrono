package com.mangajojoh.mg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mangajojoh.mg.navigation.AppNavigation
import com.mangajojoh.mg.ui.theme.MangaChronoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MangaChronoTheme {
                AppNavigation()
            }
        }
    }
}
