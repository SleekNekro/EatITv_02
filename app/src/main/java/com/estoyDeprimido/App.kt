package com.estoyDeprimido

//noinspection UsingMaterialAndMaterial3Libraries
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.estoyDeprimido.login.LoginScreen
import com.estoyDeprimido.ui.theme.EatITv_02Theme
import org.github.sleeknekro.nav.Navigation


class MainActivity:ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EatITv_02Theme {
                Navigator(Navigation()){
                    n-> SlideTransition(n)
                }
            }
        }
    }
}
@Composable
@Preview
fun App(){
    MaterialTheme {
        Navigator(screen = Navigation())
    }
}

