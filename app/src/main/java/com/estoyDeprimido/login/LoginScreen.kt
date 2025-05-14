package com.estoyDeprimido.login

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.estoyDeprimido.R

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        var selectedTab by remember { mutableIntStateOf(0) }
        val tabTitles = listOf("Login", "Registro")

        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp),
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
                        onClick = { selectedTab = index },

                    ){
                        Box(
                            modifier = Modifier.background(
                            color = if (selectedTab==index) MaterialTheme.colorScheme.onSecondary
                                else Color.Transparent,
                                shape = RoundedCornerShape(30.dp)
                            )
                                .padding(vertical = 8.dp, horizontal = 61.dp)
                        ){
                                Text(
                                    title,
                                    fontSize = 18.sp,
                                    color = if (selectedTab==index) Color.White else MaterialTheme.colorScheme.primary
                                )
                        }
                    }
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

            Row(
                Modifier.padding(16.dp),
                Arrangement.spacedBy(8.dp),
                Alignment.CenterVertically
            ) {
                Text(
                text = "If u can cook it u can share it",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 16.sp,
                color = Color.Black
                )
                Image(
                    painterResource(R.drawable.logoeatit_oscuro),
                    contentDescription = null,
                    Modifier.size(15.dp)

                )
            }

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
        Row(
            Modifier.padding(16.dp),
            Arrangement.spacedBy(6.dp),
            Alignment.CenterVertically

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
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("url"))
                }
            )
        }


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
