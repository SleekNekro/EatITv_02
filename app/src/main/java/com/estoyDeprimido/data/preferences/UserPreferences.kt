package com.estoyDeprimido.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.estoyDeprimido.data.model.UserData
import com.estoyDeprimido.data.model.UserResponse
import kotlinx.coroutines.flow.first

object UserPreferences {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    suspend fun saveUser(context: Context, user: UserResponse, token: String) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey("token")] = token
            prefs[stringPreferencesKey("username")] = user.username
            prefs[stringPreferencesKey("email")] = user.email
            prefs[stringPreferencesKey("profilePic")] = user.profilePic ?: ""
            prefs[intPreferencesKey("followers")] = user.followers
        }
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
            createdAt = prefs[stringPreferencesKey("createdAt")] ?: ""
        )
    }

    suspend fun getToken(context: Context): String? {
        val prefs = context.dataStore.data.first()
        return prefs[stringPreferencesKey("token")]
    }
}
