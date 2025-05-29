package com.estoyDeprimido

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.estoyDeprimido.ui.theme.EatITv_02Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EatITv_02Theme {
                Navigator(screen = StartScreen) { screen ->
                    SlideTransition(screen)
                }
            }
        }
    }
}

@Composable
@Preview
fun AppPreview() {
    MaterialTheme {
        Navigator(screen = StartScreen)
    }
}
