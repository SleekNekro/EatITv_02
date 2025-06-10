package org.github.sleeknekro.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.estoyDeprimido.R
import com.estoyDeprimido.ui.Screen.CreateScreen

object CreateTab: Tab {


    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(R.drawable.add)
            return remember {
                TabOptions(
                    index = 2u,
                    title= "",
                    icon=icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        CreateScreen(onRecipeCreated = {
            navigator.pop()
        })
    }

}