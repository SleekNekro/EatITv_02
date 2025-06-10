package com.estoyDeprimido.ui.Screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.rememberAsyncImagePainter
import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.ui.states.EditProfileUiState
import com.estoyDeprimido.ui.viewmodels.AuthViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.estoyDeprimido.data.model.ProfileData
import com.estoyDeprimido.ui.viewmodels.EditProfileViewModel

class EditProfileScreen(private val user: UserData) : Screen {
    @Composable
    override fun Content() {
        val authViewModel: AuthViewModel = viewModel()
        val editProfileViewModel: EditProfileViewModel = viewModel()
        var newUsername by remember { mutableStateOf(user.username) }
        var newEmail by remember { mutableStateOf(user.email) }
        var newPassword by remember { mutableStateOf("") }
        var newProfilePic by remember { mutableStateOf(user.profilePic) }


        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let { newProfilePic = it.toString() }
        }

        val navigator = LocalNavigator.current


        val editProfileState by authViewModel.editProfileState.collectAsState()

        LaunchedEffect(editProfileState) {
            if (editProfileState is EditProfileUiState.Saved) {
                navigator?.pop()
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(120.dp))
            // Imagen de perfil
            Image(
                painter = rememberAsyncImagePainter(newProfilePic),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))


            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = newUsername,
                onValueChange = { newUsername = it },
                label = { Text("Nuevo nombre") },
                placeholder = { Text("Introduce tu nuevo nombre") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = newEmail,
                onValueChange = { newEmail = it },
                label = { Text("Nuevo email") },
                placeholder = { Text("Introduce tu nuevo email") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Nueva contraseña") },
                placeholder = { Text("Introduce tu nueva contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val newProfileData = ProfileData(
                        id = user.id,
                        username = newUsername,
                        email = newEmail,
                        profilePic = newProfilePic
                    )
                    editProfileViewModel.saveChanges(newProfileData)
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF2E7D32))
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Guardar cambios",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar cambios", color = Color.White)
            }
        }
    }
}
