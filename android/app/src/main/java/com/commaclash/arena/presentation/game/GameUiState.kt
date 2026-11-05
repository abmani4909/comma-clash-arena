package com.commaclash.arena.presentation.game

import com.commaclash.arena.core.config.GameConfig
import com.commaclash.arena.domain.model.GrammarRule
import com.commaclash.arena.domain.model.SentenceItem

enum class GamePhase { ANSWERING, RESOLVED, FINISHING }

enum class PipState { PENDING, SOLVED, MISSED }

data class GameUiState(
    val phase: GamePhase = GamePhase.ANSWERING,
    val index: Int = 0,
    val total: Int = GameConfig.SENTENCES_PER_ROUND,
    val item: SentenceItem? = null,
    val rule: GrammarRule? = null,
    val selected: Int = -1,
    val swapped: Boolean = false,
    val lastCorrect: Boolean = false,
    val rivalHp: Int = GameConfig.RIVAL_MAX_HP,
    val misses: Int = 0,
    val knockedOut: Boolean = false,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val damage: Int = 0,
    val correctCount: Int = 0,
    val pips: List<PipState> = List(GameConfig.SENTENCES_PER_ROUND) { PipState.PENDING },
    val timerFraction: Float = 1f,
    val timerEnabled: Boolean = true,
    val paused: Boolean = false,
    val stampTick: Int = 0,
    val wiggleTick: Int = 0,
    val hitTick: Int = 0,
    val lastSentence: Boolean = false
) {
    val rivalFraction: Float get() = rivalHp.toFloat() / GameConfig.RIVAL_MAX_HP
}
