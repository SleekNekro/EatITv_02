package com.estoyDeprimido.utils

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.estoyDeprimido.ui.viewmodels.AuthViewModel

@Composable
fun LogoutButton(authViewModel: AuthViewModel) {
    val context = LocalContext.current
    IconButton(
        onClick = {
            authViewModel.logout(context) {
                // En lugar de navegar a LoginTab, reiniciamos la aplicación.
                restartApp(context)
            }
        }
    ) {
        Icon(
            imageVector = Icons.Filled.ExitToApp,
            contentDescription = "Cerrar sesión",
            tint = Color.White
        )
    }
}




