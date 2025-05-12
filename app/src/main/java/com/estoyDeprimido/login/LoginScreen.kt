package com.estoyDeprimido.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.estoyDeprimido.R

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        var selectedTab by remember { mutableStateOf(0) }
        val tabTitles = listOf("Login", "Registro")

        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF8F2F2)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = painterResource(R.drawable.logoeatit_oscuro_contexto),
                contentDescription = "Logo de Eat It",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // TabRow redondeado
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)) // 🔥 Bordes redondeados en el contenedor
                    .background(Color(0xFFE91E63)), // Color de fondo del TabRow
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color.White // Color del indicador
                    )
                },
                divider = {} // Oculta la línea separadora
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Animación entre Login y Registro
            if (selectedTab == 0) {
                LoginForm()
            } else {
                RegisterForm()
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Frase final
            Text(
                text = "If u can cook it u can share it 😊",
                fontSize = 16.sp,
                color = Color(0xFF6A1B9A)
            )
        }
    }
}

@Composable
fun LoginForm() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
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

        Text("Forgot your password? Click here", color = Color(0xFFE91E63))

        Spacer(modifier = Modifier.height(20.dp))

        // Botón de enviar formulario
        Button(
            onClick = { /* Acción de login */ },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
        ) {
            Text("Enviar", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun RegisterForm() {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        TextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(10.dp))

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

        // Botón de enviar formulario
        Button(
            onClick = { /* Acción de registro */ },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
        ) {
            Text("Enviar", fontSize = 18.sp, color = Color.White)
        }
    }
}
