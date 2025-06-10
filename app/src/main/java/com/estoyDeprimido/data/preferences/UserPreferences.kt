package com.estoyDeprimido.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.data.model.http_.UserResponse
import kotlinx.coroutines.flow.first
import android.util.Log
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.runBlocking

private const val TAG1 = "DataStore"
private const val TAG2 = "DataStore_Token"
object UserPreferences {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    suspend fun saveUser(context: Context, user: UserResponse, token: String) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey("token")] = token
            prefs[stringPreferencesKey("username")] = user.username
            prefs[stringPreferencesKey("email")] = user.email
            prefs[stringPreferencesKey("profilePic")] = user.profilePic ?: ""
            prefs[intPreferencesKey("followers")] = user.followers
            prefs[longPreferencesKey("userId")] = user.id
        }
        Log.d(TAG1, "saveUser: User ID guardado: ${user.id}, Token guardado: $token")
    }


    suspend fun getUser(context: Context): UserData? {
        val prefs = context.dataStore.data.first()
        val token = prefs[stringPreferencesKey("token")] ?: return null
        return UserData(
            id = 0L, // No se guarda el ID aquí
            username = prefs[stringPreferencesKey("username")] ?: "",
            email = prefs[stringPreferencesKey("email")] ?: "",
            profilePic = prefs[stringPreferencesKey("profilePic")] ?: "",
            followers = prefs[intPreferencesKey("followers")] ?: 0,
            createdAt = prefs[stringPreferencesKey("createdAt")] ?: "",
            following = 0,
            recipesCount = 0
        )
    }

    suspend fun getToken(context: Context): String? {
        val prefs = context.dataStore.data.first()
        val token = prefs[stringPreferencesKey("token")]
        Log.d(TAG2, "getToken: Token obtenido: ${token ?: "null o vacío"}")
        return token
    }

    suspend fun getUserId(context: Context): Long? {

        val prefs = context.dataStore.data.first() // ✅ No es necesario `runBlocking`
        Log.d("UserPreferences", "getUserId: User ID obtenido: ${prefs[longPreferencesKey("userId")] ?: "null o vacío"}")
        return prefs[longPreferencesKey("userId")]
    }
    suspend fun clearUserData(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(stringPreferencesKey("token"))
            prefs.remove(stringPreferencesKey("username"))
            prefs.remove(stringPreferencesKey("email"))
            prefs.remove(stringPreferencesKey("profilePic"))
            prefs.remove(intPreferencesKey("followers"))
            prefs.remove(longPreferencesKey("userId"))
        }
        Log.d(TAG1, "clearUserData: Datos del usuario y token eliminados correctamente")
    }


}
