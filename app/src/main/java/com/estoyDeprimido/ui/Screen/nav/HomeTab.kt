package org.github.sleeknekro.nav

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.estoyDeprimido.R


object HomeTab: Tab{


    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(R.drawable.home)
            return remember {
                TabOptions(
                    index = 0u,
                    title= "",
                    icon=icon
                )
            }
        }

    @Composable
    override fun Content() {
//        val recipeState = produceState<List<RecipeProfile>>(initialValue = emptyList<>(), key1 = Unit) {
//
//            value=response
//        }
//        try {
//            val response
//        }
        LazyColumn {

        }
    }

}

