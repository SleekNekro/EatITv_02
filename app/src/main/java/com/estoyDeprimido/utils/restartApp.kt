package com.estoyDeprimido.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

fun restartApp(context: Context) {
    // Obtiene el intent de lanzamiento de la propia aplicación.
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(context.packageName)

    // Agrega las banderas para limpiar la pila de tareas.
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

    // Inicia el intent.
    context.startActivity(intent)

    // Si el contexto es una actividad, ciérrala.
    if (context is Activity) {
        context.finish()
    }

    // Termina el proceso actual. (Opcional, para asegurarse de recrear el estado)
    Runtime.getRuntime().exit(0)
}