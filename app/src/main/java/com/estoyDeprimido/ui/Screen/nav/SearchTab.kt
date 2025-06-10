package com.estoyDeprimido.ui.Screen.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.estoyDeprimido.R
import com.estoyDeprimido.ui.Screen.SearchScreen

object SearchTab: Tab {


    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(R.drawable.search)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        SearchScreen(onUserClick = { userId ->

        })


    }
}