package com.estoyDeprimido.ui.Screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.estoyDeprimido.R
import com.estoyDeprimido.ui.viewmodels.AuthViewModel
import com.estoyDeprimido.ui.states.AuthUiState
import com.estoyDeprimido.ui.Screen.nav.Navigation

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        // Obtiene el ViewModel y el Navigator de Voyager.
        val viewModel = viewModel<AuthViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        // Para controlar cuál pestaña se muestra: Login o Registro.
        var selectedTab by remember { mutableIntStateOf(0) }
        val tabTitles = listOf("Login", "Registro")

        // Observa el estado de la UI proveniente del ViewModel.
        val uiState by viewModel.uiState.collectAsState()

        // Navega a la pantalla de navegación (HomeTab) cuando el login es exitoso.
        LaunchedEffect(uiState) {
            if (uiState is AuthUiState.Success) {
                navigator.replaceAll(Navigation())
            }
        }

        // Contenido visual de la pantalla de login/registro.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = painterResource(R.drawable.logoeatit_oscuro_contexto),
                contentDescription = "Logo de Eat It",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // TabRow con pestañas para Login y Registro.
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp)),
                indicator = {},
                divider = {}
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (selectedTab == index) MaterialTheme.colorScheme.onSecondary
                                    else Color.Transparent,
                                    shape = RoundedCornerShape(30.dp)
                                )
                                .padding(vertical = 8.dp, horizontal = 61.dp)
                        ) {
                            Text(
                                title,
                                fontSize = 18.sp,
                                color = if (selectedTab == index) Color.White else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Muestra el formulario correspondiente según la pestaña seleccionada.
            if (selectedTab == 0) {
                LoginForm(viewModel)
            } else {
                RegisterForm(viewModel)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "If u can cook it u can share it",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Image(
                    painter = painterResource(R.drawable.logoeatit_oscuro),
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Composable
fun LoginForm(viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Forgot your password?",
                color = Color.Black,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                "Click here",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelMedium.copy(textDecoration = TextDecoration.Underline),
                modifier = Modifier.clickable {
                    // Abre una URL para recuperar la contraseña.
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("url"))
                    // En este ejemplo no se envía el intent ya que se requiere un contexto.
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para enviar el formulario de login.
        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
        ) {
            Text("Enviar", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun RegisterForm(viewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para enviar el formulario de registro.
        Button(
            onClick = { viewModel.register(
                email = email,       // El campo de email recibe el valor de email
                username = username, // El campo de username recibe el valor de username
                password = password  // El campo de password recibe el valor de password
            )  },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
        ) {
            Text("Enviar", fontSize = 18.sp, color = Color.White)
        }
    }
}
