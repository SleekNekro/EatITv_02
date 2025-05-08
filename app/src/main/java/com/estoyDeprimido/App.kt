package com.estoyDeprimido

//noinspection UsingMaterialAndMaterial3Libraries
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.github.sleeknekro.nav.Navigation

class MainActivity:ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}
@Composable
@Preview
fun App(){
    MaterialTheme {
        Navigator(screen = Navigation()){
            navigator -> SlideTransition(navigator)
        }
    }
}

