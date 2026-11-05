package com.commaclash.arena.data.local

import android.content.Context
import android.content.SharedPreferences
import com.commaclash.arena.domain.model.AppSettings
import com.commaclash.arena.domain.model.PlayerProgress
import com.commaclash.arena.domain.model.SpellingVariant
import com.commaclash.arena.domain.model.TextSizeOption

class AppPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("comma_clash_arena", Context.MODE_PRIVATE)

    fun readProgress(): PlayerProgress = PlayerProgress(
        tier = prefs.getInt(KEY_TIER, 1),
        unlockedTier = prefs.getInt(KEY_UNLOCKED, 1),
        rounds = prefs.getInt(KEY_ROUNDS, 0),
        bestStreak = prefs.getInt(KEY_BEST_STREAK, 0),
        totalCorrect = prefs.getInt(KEY_TOTAL_CORRECT, 0),
        totalAnswers = prefs.getInt(KEY_TOTAL_ANSWERS, 0),
        ruleCorrect = decodeMap(prefs.getString(KEY_RULE_CORRECT, "")),
        ruleTotal = decodeMap(prefs.getString(KEY_RULE_TOTAL, "")),
        clinic = decodeMap(prefs.getString(KEY_CLINIC, "")),
        weekly = decodeWeek(prefs.getString(KEY_WEEKLY, ""))
    )

    fun writeProgress(progress: PlayerProgress) {
        prefs.edit()
            .putInt(KEY_TIER, progress.tier)
            .putInt(KEY_UNLOCKED, progress.unlockedTier)
            .putInt(KEY_ROUNDS, progress.rounds)
            .putInt(KEY_BEST_STREAK, progress.bestStreak)
            .putInt(KEY_TOTAL_CORRECT, progress.totalCorrect)
            .putInt(KEY_TOTAL_ANSWERS, progress.totalAnswers)
            .putString(KEY_RULE_CORRECT, encodeMap(progress.ruleCorrect))
            .putString(KEY_RULE_TOTAL, encodeMap(progress.ruleTotal))
            .putString(KEY_CLINIC, encodeMap(progress.clinic))
            .putString(KEY_WEEKLY, progress.weekly.joinToString(","))
            .apply()
    }

    fun clearProgress() {
        prefs.edit()
            .remove(KEY_TIER)
            .remove(KEY_UNLOCKED)
            .remove(KEY_ROUNDS)
            .remove(KEY_BEST_STREAK)
            .remove(KEY_TOTAL_CORRECT)
            .remove(KEY_TOTAL_ANSWERS)
            .remove(KEY_RULE_CORRECT)
            .remove(KEY_RULE_TOTAL)
            .remove(KEY_CLINIC)
            .remove(KEY_WEEKLY)
            .apply()
    }

    fun readSettings(): AppSettings = AppSettings(
        textSize = runCatching {
            TextSizeOption.valueOf(prefs.getString(KEY_TEXT_SIZE, TextSizeOption.NORMAL.name).orEmpty())
        }.getOrDefault(TextSizeOption.NORMAL),
        timerEnabled = prefs.getBoolean(KEY_TIMER, true),
        hapticsEnabled = prefs.getBoolean(KEY_HAPTICS, true),
        spelling = runCatching {
            SpellingVariant.valueOf(prefs.getString(KEY_SPELLING, SpellingVariant.UK.name).orEmpty())
        }.getOrDefault(SpellingVariant.UK)
    )

    fun writeSettings(settings: AppSettings) {
        prefs.edit()
            .putString(KEY_TEXT_SIZE, settings.textSize.name)
            .putBoolean(KEY_TIMER, settings.timerEnabled)
            .putBoolean(KEY_HAPTICS, settings.hapticsEnabled)
            .putString(KEY_SPELLING, settings.spelling.name)
            .apply()
    }

    private fun encodeMap(source: Map<String, Int>): String =
        source.entries.joinToString(";") { entry -> entry.key + ":" + entry.value }

    private fun decodeMap(raw: String?): Map<String, Int> {
        if (raw.isNullOrBlank()) return emptyMap()
        val result = LinkedHashMap<String, Int>()
        raw.split(";").forEach { pair ->
            val parts = pair.split(":")
            if (parts.size == 2) {
                val value = parts[1].toIntOrNull()
                if (value != null) result[parts[0]] = value
            }
        }
        return result
    }

    private fun decodeWeek(raw: String?): List<Int> {
        if (raw.isNullOrBlank()) return List(7) { -1 }
        val parsed = raw.split(",").mapNotNull { it.trim().toIntOrNull() }
        return if (parsed.size == 7) parsed else List(7) { -1 }
    }

    private companion object {
        const val KEY_TIER = "tier"
        const val KEY_UNLOCKED = "unlocked_tier"
        const val KEY_ROUNDS = "rounds"
        const val KEY_BEST_STREAK = "best_streak"
        const val KEY_TOTAL_CORRECT = "total_correct"
        const val KEY_TOTAL_ANSWERS = "total_answers"
        const val KEY_RULE_CORRECT = "rule_correct"
        const val KEY_RULE_TOTAL = "rule_total"
        const val KEY_CLINIC = "clinic"
        const val KEY_WEEKLY = "weekly"
        const val KEY_TEXT_SIZE = "text_size"
        const val KEY_TIMER = "timer_enabled"
        const val KEY_HAPTICS = "haptics_enabled"
        const val KEY_SPELLING = "spelling"
    }
}
