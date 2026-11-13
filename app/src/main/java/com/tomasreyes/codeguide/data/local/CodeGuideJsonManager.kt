package com.tomasreyes.codeguide.data.local

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.tomasreyes.codeguide.ui.viewmodel.CodeGuideUiState
import java.io.File

object CodeGuideJsonManager {
    private const val FILE_NAME = "codeguide_state.json"

    // Guarda el estado de la app en la base de datos JSON local
    fun saveToDatabase(context: Context, state: CodeGuideUiState) {
        try {
            val gson = Gson()
            val jsonString = gson.toJson(state)
            val file = File(context.filesDir, FILE_NAME)
            file.writeText(jsonString)
            Log.d("CodeGuideJsonManager", "Estado local persistido correctamente.")
        } catch (e: Exception) {
            Log.e("CodeGuideJsonManager", "Error al guardar el estado local", e)
        }
    }

    // Carga el estado previamente guardado en el arranque de la app
    fun loadFromDatabase(context: Context): CodeGuideUiState? {
        return try {
            val file = File(context.filesDir, FILE_NAME)
            if (file.exists()) {
                val jsonString = file.readText()
                val gson = Gson()
                Log.d("CodeGuideJsonManager", "Estado local cargado con éxito.")
                gson.fromJson(jsonString, CodeGuideUiState::class.java)
            } else {
                Log.d("CodeGuideJsonManager", "No se encontró un estado guardado previamente.")
                null
            }
        } catch (e: Exception) {
            Log.e("CodeGuideJsonManager", "Error al cargar el estado local", e)
            null
        }
    }
}


