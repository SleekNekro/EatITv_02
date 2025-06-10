package com.estoyDeprimido.ui.Screen.nav

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.estoyDeprimido.R
import com.estoyDeprimido.ui.states.AuthUiState
import com.estoyDeprimido.ui.viewmodels.AuthViewModel
import com.estoyDeprimido.utils.LogoutButton
import org.github.sleeknekro.nav.CreateTab
import org.github.sleeknekro.nav.HomeTab
import org.github.sleeknekro.nav.ProfileTab

class Navigation : Screen {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    override fun Content() {
        TabNavigator(
            HomeTab,   // La pestaña inicial es HomeTab.
            tabDisposable = {
                TabDisposable(
                    it,
                    listOf(HomeTab, CreateTab, ProfileTab, SearchTab)
                )
            }
        ) {
            val authViewModel: AuthViewModel = viewModel()
            val tabNavigator = LocalTabNavigator.current
            val navigator = LocalNavigator.current
            val context = LocalContext.current
            val uiState by authViewModel.uiState.collectAsState()

            LaunchedEffect(uiState) {
                if (uiState is AuthUiState.Loggedout) {
                    navigator?.replaceAll(LoginTab)
                }
            }

            Scaffold(
                content = { CurrentTab() },
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(Modifier.width(16.dp))
                                Image(
                                    painter = painterResource(R.drawable.logoeatit_claro),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    alignment = Alignment.TopCenter
                                )
                                Spacer(Modifier.width(70.dp))
                                Image(
                                    painter = painterResource(R.drawable.eatit_claro_texto),
                                    contentDescription = null,
                                    modifier = Modifier.size(120.dp),
                                    alignment = Alignment.TopCenter
                                )

                                Spacer(Modifier.weight(1f)) // 🔥 Empuja el botón hacia la derecha

                                LogoutButton(authViewModel)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF66335A)
                        )
                    )
                }
                ,
                bottomBar = {
                    Modifier.border(1.dp, color = Color(0xFFCAC4D0))
                    BottomNavigation(backgroundColor = Color(0xFFDFDDCE)) {
                        BottomNavigationItem(
                            selected = tabNavigator.current.key == key,
                            icon = {
                                FancyBottomNavigationIcon(
                                    painter = HomeTab.options.icon!!,
                                    contentDescription = HomeTab.options.title,
                                    selected = (tabNavigator.current.key == key)
                                )
                            },
                            onClick = { tabNavigator.current = HomeTab }
                        )
                        BottomNavigationItem(
                            selected = tabNavigator.current.key == key,
                            icon = {
                                FancyBottomNavigationIcon(
                                    painter = SearchTab.options.icon!!,
                                    contentDescription = SearchTab.options.title,
                                    selected = (tabNavigator.current.key == key)
                                )
                            },
                            onClick = { tabNavigator.current = SearchTab }
                        )
                        BottomNavigationItem(
                            selected = tabNavigator.current.key == key,
                            icon = {
                                FancyBottomNavigationIcon(
                                    painter = CreateTab.options.icon!!,
                                    contentDescription = CreateTab.options.title,
                                    selected = (tabNavigator.current.key == key)
                                )
                            },
                            onClick = { tabNavigator.current = CreateTab }
                        )
                        BottomNavigationItem(
                            selected = tabNavigator.current.key == key,
                            icon = {
                                FancyBottomNavigationIcon(
                                    painter = ProfileTab.options.icon!!,
                                    contentDescription = ProfileTab.options.title,
                                    selected = (tabNavigator.current.key == key)
                                )
                            },
                            onClick = { tabNavigator.current = ProfileTab }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun FancyBottomNavigationIcon(
    painter: Painter,
    contentDescription: String?,
    selected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 30.dp,
    activeTint: Color = Color.White,
    inactiveTint: Color = Color(0xFFFB90A4),
    activeBackground: Color = Color(0xFF66335A),
    inactiveBackground: Color = Color.Transparent,
    cornerRadius: Dp = 16.dp,
    animationDuration: Int = 300
) {
    // Anima la escala del icono
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = animationDuration)
    )
    // Anima el tinte del icono
    val tint by animateColorAsState(
        targetValue = if (selected) activeTint else inactiveTint,
        animationSpec = tween(durationMillis = animationDuration)
    )
    // Anima el color del fondo
    val bgColor by animateColorAsState(
        targetValue = if (selected) activeBackground else inactiveBackground,
        animationSpec = tween(durationMillis = animationDuration)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(scale)
            .shadow(elevation = if (selected) 8.dp else 0.dp, shape = RoundedCornerShape(cornerRadius))
            .background(color = bgColor, shape = RoundedCornerShape(cornerRadius))
            .padding(8.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(size),
            tint = tint
        )
    }
}
