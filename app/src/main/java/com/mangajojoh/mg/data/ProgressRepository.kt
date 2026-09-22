package com.mangajojoh.mg.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Gere uniquement la progression par niveau.
 * Plus de score ni d'XP : seul le deblocage compte.
 */
class ProgressRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    fun getUnlockedLevel(): Int = prefs.getInt(KEY_UNLOCKED, 1)
    fun getCurrentLevel(): Int = prefs.getInt(KEY_CURRENT, 1)

    /** Marque un niveau comme reussi -> debloque le suivant. */
    fun markLevelCompleted(level: Int) {
        val editor = prefs.edit()
        if (level == getUnlockedLevel() && level < 100) {
            editor.putInt(KEY_UNLOCKED, level + 1)
        }
        editor.apply()
    }

    fun setCurrentLevel(level: Int) {
        prefs.edit().putInt(KEY_CURRENT, level.coerceIn(1, 100)).apply()
    }

    fun reset() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "mangachrono_progress"
        private const val KEY_UNLOCKED = "unlocked_level"
        private const val KEY_CURRENT = "current_level"
    }
}
