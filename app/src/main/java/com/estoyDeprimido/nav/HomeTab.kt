package org.github.sleeknekro.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
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
        val recipeState = produceState<List<RecipeProfile>>(initialValue = emptyList<>(), key1 = Unit) {

            value=response
        }
        try {
            val response
        }
        LazyColumn {

        }
    }

}

