
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.estoyDeprimido.data.preferences.UserPreferences
import com.estoyDeprimido.ui.Screen.LoginScreen
import org.github.sleeknekro.nav.Navigation

object StartScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current

        var token by remember { mutableStateOf<String?>(null) }
        var loading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            token = UserPreferences.getToken(context)
            loading = false
            println("Token leído: $token")
            if (token != null) {
                // Aquí reemplazamos con la pantalla de Navigation, que es nuestro HomeTab
                navigator.replaceAll(Navigation())
            } else {
                // Si no hay token, seguimos mostrando el LoginScreen (o AuthScreen)
                navigator.replaceAll(LoginScreen())
            }
        }

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Cargando...")
            }
        }
    }
}
