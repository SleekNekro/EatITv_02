package com.estoyDeprimido.ui.Screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.estoyDeprimido.ui.viewmodels.CreateRecipeViewModel

@Composable
fun CreateScreen(
    viewModel: CreateRecipeViewModel = viewModel(),
    onRecipeCreated: () -> Unit
) {
    val context = LocalContext.current

    // Launcher para seleccionar imagen desde almacenamiento local
    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.imageUri.value = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(96.dp))
        // Área para seleccionar y previsualizar la imagen
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.imageUri.value != null) {
                Image(
                    painter = rememberAsyncImagePainter(viewModel.imageUri.value),
                    contentDescription = "Imagen de la receta",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Selecciona una imagen",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        // Campo para el título
        OutlinedTextField(
            value = viewModel.title.value,
            onValueChange = { viewModel.title.value = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )
        // Campo para la descripción
        OutlinedTextField(
            value = viewModel.description.value,
            onValueChange = { viewModel.description.value = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        // Campo para las porciones (servings)
        OutlinedTextField(
            value = viewModel.servingsStr.value,
            onValueChange = { viewModel.servingsStr.value = it },
            label = { Text("Porciones") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        // Botón para crear la receta
        Button(
            onClick = {
                viewModel.createRecipe {
                    // Callback al finalizar la creación de la receta
                    onRecipeCreated()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isUploading.value
        ) {
            if (viewModel.isUploading.value) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Crear Receta")
            }
        }
        // Mostrar mensaje de error, en caso de existir
        if (viewModel.errorMessage.value.isNotBlank()) {
            Text(
                text = viewModel.errorMessage.value,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
