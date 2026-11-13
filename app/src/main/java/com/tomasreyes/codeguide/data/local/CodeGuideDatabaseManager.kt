package com.tomasreyes.codeguide.data.local

import android.content.Context
import com.google.gson.Gson
import com.tomasreyes.codeguide.data.model.EnergyLevel
import com.tomasreyes.codeguide.data.model.MicroTask
import com.tomasreyes.codeguide.data.model.ProjectItem
import com.tomasreyes.codeguide.data.model.ProjectStatus
import com.tomasreyes.codeguide.ui.viewmodel.CodeGuideUiState
import java.io.File

/**
 * Gestor de Base de Datos Local con Persistencia Real en Disco (CodeGuide DB)
 */
data class CodeGuideDatabaseEntity(
    val dailyTasks: List<MicroTask>,
    val projectsList: List<ProjectItem>,
    val energyLevelName: String,
    val streakDays: Int,
    val completedBlocksCount: Int,
    val totalFocusMinutes: Int,
    val clarityIndex: Int,
    val isDarkMode: Boolean
)

object CodeGuideDatabaseManager {

    private const val DB_FILE_NAME = "codeguide_local_db.json"
    private val gson = Gson()

    fun loadFromDatabase(context: Context): CodeGuideUiState? {
        return try {
            val dbFile = File(context.filesDir, DB_FILE_NAME)
            if (!dbFile.exists()) return null

            val jsonString = dbFile.readText()
            if (jsonString.isBlank()) return null

            val entity = gson.fromJson(jsonString, CodeGuideDatabaseEntity::class.java) ?: return null

            val energyLevel = try {
                EnergyLevel.valueOf(entity.energyLevelName)
            } catch (e: Exception) {
                EnergyLevel.OPTIMA
            }

            CodeGuideUiState(
                dailyTasks = entity.dailyTasks ?: emptyList(),
                projectsList = entity.projectsList ?: emptyList(),
                energyLevel = energyLevel,
                streakDays = entity.streakDays,
                completedBlocksCount = entity.completedBlocksCount,
                totalFocusMinutes = entity.totalFocusMinutes,
                clarityIndex = entity.clarityIndex,
                isDarkMode = entity.isDarkMode
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveToDatabase(context: Context, uiState: CodeGuideUiState) {
        try {
            val entity = CodeGuideDatabaseEntity(
                dailyTasks = uiState.dailyTasks,
                projectsList = uiState.projectsList,
                energyLevelName = uiState.energyLevel.name,
                streakDays = uiState.streakDays,
                completedBlocksCount = uiState.completedBlocksCount,
                totalFocusMinutes = uiState.totalFocusMinutes,
                clarityIndex = uiState.clarityIndex,
                isDarkMode = uiState.isDarkMode
            )

            val jsonString = gson.toJson(entity)
            val dbFile = File(context.filesDir, DB_FILE_NAME)
            dbFile.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
