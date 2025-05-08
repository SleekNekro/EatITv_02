package org.github.sleeknekro.nav

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.estoyDeprimido.nav.SearchTab
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class Navigation: Screen {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        TabNavigator(
            HomeTab,
            tabDisposable = {
                TabDisposable(
                    it,
                    listOf(HomeTab,CreateTab,ProfileTab,SearchTab)
                )
            }
        ){
            Scaffold (
                content = {
                    CurrentTab()
                },
                topBar = {
                    TopAppBar(title = { Text(it.current.options.title) })
                },
                bottomBar = {
                    BottomNavigation(backgroundColor = Color(0xFF68355C)) {
                        val tabNavigator = LocalTabNavigator.current

                        BottomNavigationItem(selected = tabNavigator.current.key == HomeTab.key, label = { Text(HomeTab.options.title) }, icon = {
                                Icon(
                                    painter = HomeTab.options.icon!!,
                                    contentDescription = null,
                                    modifier = Modifier.size(45.dp).padding(8.dp),
                                    tint = Color(0xFFFFE1FF)
                                )
                            }, onClick = { tabNavigator.current = HomeTab })
                        BottomNavigationItem(selected = tabNavigator.current.key == SearchTab.key, label = { Text(SearchTab.options.title) }, icon = {
                                Icon(
                                    painter = SearchTab.options.icon!!,
                                    contentDescription = null,
                                    modifier = Modifier.size(45.dp).padding(8.dp),
                                    tint = Color(0xFFFFE1FF)
                                )
                            }, onClick = { tabNavigator.current = SearchTab })
                        BottomNavigationItem(
                            selected = tabNavigator.current.key == CreateTab.key,
                            label = { Text(CreateTab.options.title) },
                            icon = {Icon(
                                painter = CreateTab.options.icon!!,
                                contentDescription = null,
                                modifier = Modifier.size(45.dp).padding(8.dp),
                                tint = Color(0xFFFFE1FF)
                            )},
                            onClick = {tabNavigator.current = CreateTab}
                        )
                        BottomNavigationItem(
                            selected = tabNavigator.current.key == ProfileTab.key,
                            label = { Text(ProfileTab.options.title) },
                            icon = {Icon(
                                painter = ProfileTab.options.icon!!,
                                contentDescription = null,
                                modifier = Modifier.size(45.dp).padding(8.dp),
                                tint = Color(0xFFFFE1FF)
                            )},
                            onClick = {tabNavigator.current = ProfileTab}
                        )
                    }
                }
            )
        }
    }
}