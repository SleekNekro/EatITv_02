package com.estoyDeprimido.ui.Screen.nav

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.estoyDeprimido.ui.Screen.LoginScreen

object LoginTab : Tab {
    @Composable
    override fun Content() {
        LoginScreen() // Aquí se llama a tu pantalla de login
    }

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            title = "", // Título vacío para que no aparezca
            icon = painterResource(0),
            index = 9U
        )
}