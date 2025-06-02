package org.github.sleeknekro.nav

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.estoyDeprimido.R
import com.estoyDeprimido.data.preferences.UserPreferences
import com.estoyDeprimido.ui.Screen.ProfileScreen
import kotlinx.coroutines.runBlocking

object ProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(R.drawable.user)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Perfil",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val context = LocalContext.current
        var userId by remember { mutableStateOf<Long?>(null) }

        LaunchedEffect(Unit) {
            userId = UserPreferences.getUserId(context)
            Log.d("ProfileTAB", "User ID obtenido en LaunchedEffect: $userId")
        }

        when {
            userId == null -> {
                Log.d("ProfileTAB", "Esperando a que se obtenga userId...")
                Text(text = "Cargando perfil...", style = MaterialTheme.typography.bodyMedium)
            }
            userId!! > 0L -> {
                Log.d("ProfileTAB", "✅ Navegando a ProfileScreen con userId=$userId") // 🔥 Verificación clave
                cafe.adriel.voyager.navigator.Navigator(ProfileScreen(userId = userId!!)) // 🚀 Agrega la navegación con Voyager
            }
            else -> {
                Log.e("ProfileTAB", "❌ Error: userId sigue siendo 0L después de obtenerlo")
                Text(text = "Error al obtener perfil", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }


}

