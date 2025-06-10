package com.estoyDeprimido.ui.Screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import coil.compose.rememberAsyncImagePainter
import com.estoyDeprimido.R
import com.estoyDeprimido.ui.viewmodels.ProfileViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator

class ProfileScreen(private val userId: Long) : Screen {
    @Composable
    override fun Content() {
        Log.d("ProfileScreen", "✅ Renderizando ProfileScreen")
        val context = LocalContext.current
        val viewModel: ProfileViewModel = viewModel()


        LaunchedEffect(Unit) {
            viewModel.loadProfileRecipes()
        }

        val user by viewModel.user.collectAsState(initial = null)
        val recipes by viewModel.recipes.collectAsState(initial = emptyList())
        val followersCount by viewModel.followersCount.collectAsState(initial = 0)
        val followingCount by viewModel.followingCount.collectAsState(initial = 0)
        val recipeCount by viewModel.recipeCount.collectAsState(initial = 0)

        Scaffold { innerPadding ->
            val navigator = LocalNavigator.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (user != null) {
                    val profilePicUrl = "${user!!.profilePic}?ts=${System.currentTimeMillis()}"
                    Log.d("ProfileScreen", "profilePic URL: ${user?.profilePic}")
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(65.dp))
                        Image(
                            painter = rememberAsyncImagePainter(profilePicUrl),
                            contentDescription = "Foto de perfil de ${user!!.username}",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = user!!.username,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$followersCount",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Seguidores",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$followingCount",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Seguidos",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$recipeCount",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Recetas",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (user != null) {
                                Button(onClick = {
                                    navigator?.push(EditProfileScreen(user!!))
                                }) {
                                    Text("Editar perfil")
                                }
                            } else {
                                Text("Cargando datos de perfil...")
                            }

                        }
                    }
                    } else {
                    Text(
                        text = "Cargando perfil...",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                // Lista de recetas del usuario
                val listState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = listState
                ) {
                    item { Spacer(modifier = Modifier.height(92.dp)) }
                    items(recipes) { recipeCard ->
                        var expanded by remember { mutableStateOf(false) }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = !expanded },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!recipeCard.imageUrl.isNullOrEmpty()) {
                                        Image(
                                            painter = rememberAsyncImagePainter(recipeCard.imageUrl),
                                            contentDescription = "Imagen pequeña de ${recipeCard.title}",
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(MaterialTheme.shapes.medium)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }

                                    Text(
                                        text = recipeCard.title,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f)
                                    )

                                    IconButton(
                                        onClick = {
                                            viewModel.deleteRecipe(recipeCard.id) {
                                                Log.d("ProfileScreen", "✅ Receta eliminada exitosamente")
                                            }
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.redtrashcanicon),
                                            contentDescription = "Eliminar receta",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                AnimatedVisibility(visible = expanded) {
                                    Column {
                                        if (!recipeCard.imageUrl.isNullOrEmpty()) {
                                            Image(
                                                painter = rememberAsyncImagePainter(recipeCard.imageUrl),
                                                contentDescription = "Imagen expandida de ${recipeCard.title}",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(200.dp)
                                                    .clip(MaterialTheme.shapes.medium)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }

                                        Text(
                                            text = recipeCard.description,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            text = "Porciones: ${recipeCard.servings}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(92.dp))
                    }

                }
            }
        }
    }
}
