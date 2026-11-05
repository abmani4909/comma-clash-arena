package com.commaclash.arena.domain.model

enum class TextSizeOption(val size: Int) { NORMAL(22), LARGE(24), HUGE(26) }

enum class SpellingVariant { UK, US }

data class AppSettings(
    val textSize: TextSizeOption = TextSizeOption.NORMAL,
    val timerEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val spelling: SpellingVariant = SpellingVariant.UK
)
