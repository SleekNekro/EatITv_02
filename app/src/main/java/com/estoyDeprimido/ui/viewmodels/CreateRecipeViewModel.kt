package com.estoyDeprimido.ui.viewmodels

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.estoyDeprimido.data.model.RecipeData
import com.estoyDeprimido.data.model.http_.CreateRecipeRequest
import com.estoyDeprimido.data.preferences.UserPreferences
import com.estoyDeprimido.data.remote.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class CreateRecipeViewModel(application: Application) : AndroidViewModel(application) {
    // Estados de los campos de entrada
    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var servingsStr = mutableStateOf("")
    var imageUri = mutableStateOf<Uri?>(null)

    // Estados para el progreso y errores
    var isUploading = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    /**
     * Llama a la API para subir la imagen (si se ha seleccionado) y, posteriormente,
     * crea la receta usando los datos introducidos.
     */
    fun createRecipe(onRecipeCreated: () -> Unit) {
        val context = getApplication<Application>().applicationContext
        val servings = servingsStr.value.toIntOrNull()
        if (title.value.isBlank() || description.value.isBlank() || servings == null || servings <= 0) {
            errorMessage.value = "Complete todos los campos correctamente"
            return
        }
        viewModelScope.launch {
            isUploading.value = true

            var uploadedImageUrl: String? = null
            if (imageUri.value != null) {
                try {
                    context.contentResolver.openInputStream(imageUri.value!!)?.use { inputStream ->
                        val bytes = inputStream.readBytes()
                        val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                        val multipartBody = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)
                        val apiService = RetrofitClient.createApiService(context)
                        val response = apiService.uploadImage(multipartBody)
                        uploadedImageUrl = response.location // Se espera que la respuesta tenga el campo "location"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    errorMessage.value = "Error al subir la imagen"
                    isUploading.value = false
                    return@launch
                }
            }


            val recipeRequest = CreateRecipeRequest(
                userId = UserPreferences.getUserId(getApplication())!!,
                title = title.value,
                description = description.value,
                servings = servings,
                imageUrl = uploadedImageUrl
            )
            try {
                val apiService = RetrofitClient.createApiService(context)
                val createResponse = apiService.createRecipe(recipeRequest)
                if (createResponse.isSuccessful) {
                    onRecipeCreated()
                } else {
                    errorMessage.value = "Error al crear receta"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = "Error: ${e.message}"
            }
            isUploading.value = false
        }
    }
}