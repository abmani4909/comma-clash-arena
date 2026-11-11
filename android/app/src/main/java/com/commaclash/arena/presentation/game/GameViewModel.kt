package com.commaclash.arena.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commaclash.arena.core.config.GameConfig
import com.commaclash.arena.domain.model.GameMode
import com.commaclash.arena.domain.model.RoundResult
import com.commaclash.arena.domain.model.RuleOutcome
import com.commaclash.arena.domain.model.SentenceItem
import com.commaclash.arena.domain.model.SentenceToken
import com.commaclash.arena.domain.model.TokenKind
import com.commaclash.arena.domain.repository.ProgressRepository
import com.commaclash.arena.domain.repository.SentenceRepository
import com.commaclash.arena.domain.repository.SettingsRepository
import com.commaclash.arena.domain.usecase.BuildClinicRoundUseCase
import com.commaclash.arena.domain.usecase.BuildRoundUseCase
import com.commaclash.arena.domain.usecase.EvaluateAnswerUseCase
import com.commaclash.arena.domain.usecase.UpdateProgressUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed interface GameEvent {
    data class Finished(val result: RoundResult) : GameEvent
}

class GameViewModel(
    private val mode: GameMode,
    private val buildRound: BuildRoundUseCase,
    private val buildClinicRound: BuildClinicRoundUseCase,
    private val evaluateAnswer: EvaluateAnswerUseCase,
    private val updateProgress: UpdateProgressUseCase,
    private val sentenceRepository: SentenceRepository,
    private val progressRepository: ProgressRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    private val outcomes = ArrayList<RuleOutcome>()
    private val items: List<SentenceItem>
    private val timerEnabled = settingsRepository.settings.value.timerEnabled
    val hapticsEnabled = settingsRepository.settings.value.hapticsEnabled

    private var timerJob: Job? = null
    private var finished = false

    init {
        val progress = progressRepository.progress.value
        val round = if (mode == GameMode.CLINIC) {
            buildClinicRound(progress.clinic.keys.toList(), GameConfig.SENTENCES_PER_ROUND)
        } else {
            buildRound(progress.tier, GameConfig.SENTENCES_PER_ROUND)
        }
        items = if (round.isEmpty()) {
            buildRound(progress.tier, GameConfig.SENTENCES_PER_ROUND)
        } else {
            round
        }
        _state.update {
            it.copy(
                total = items.size.coerceAtLeast(1),
                pips = List(items.size.coerceAtLeast(1)) { PipState.PENDING },
                timerEnabled = timerEnabled
            )
        }
        loadSentence(0)
    }

    fun selectToken(token: SentenceToken) {
        val current = _state.value
        if (current.phase != GamePhase.ANSWERING || current.paused) return
        if (token.kind == TokenKind.WORD) return
        _state.update {
            val sameToken = it.selected == token.index
            it.copy(
                selected = token.index,
                swapped = if (token.kind == TokenKind.SWAP) {
                    if (sameToken) !it.swapped else true
                } else {
                    false
                }
            )
        }
    }

    fun check() {
        val current = _state.value
        if (current.phase != GamePhase.ANSWERING || current.paused) return
        resolve()
    }

    fun next() {
        val current = _state.value
        if (current.phase != GamePhase.RESOLVED || current.paused) return
        val nextIndex = current.index + 1
        if (current.knockedOut) {
            finishRound(burst = false)
        } else if (nextIndex >= items.size || current.rivalHp <= 0) {
            finishRound(burst = true)
        } else {
            loadSentence(nextIndex)
        }
    }

    fun pause() {
        timerJob?.cancel()
        timerJob = null
    }

    fun openPause() {
        pause()
        _state.update { it.copy(paused = true) }
    }

    fun resume() {
        val current = _state.value
        if (!current.paused) return
        _state.update { it.copy(paused = false) }
        if (current.phase == GamePhase.ANSWERING) startTimer(current.timerFraction)
    }

    fun quitRound() {
        _state.update { it.copy(paused = false) }
        finishRound(burst = false)
    }

    private fun loadSentence(index: Int) {
        val item = items.getOrNull(index) ?: return
        _state.update {
            it.copy(
                phase = GamePhase.ANSWERING,
                index = index,
                item = item,
                rule = sentenceRepository.ruleById(item.ruleId),
                selected = -1,
                swapped = false,
                lastCorrect = false,
                timerFraction = 1f,
                lastSentence = index == items.size - 1
            )
        }
        startTimer(1f)
    }

    private fun startTimer(fromFraction: Float) {
        timerJob?.cancel()
        if (!timerEnabled) return
        timerJob = viewModelScope.launch {
            var left = (GameConfig.SENTENCE_TIMER_MS * fromFraction).toLong()
            while (isActive && left > 0) {
                delay(GameConfig.TIMER_TICK_MS)
                left -= GameConfig.TIMER_TICK_MS
                _state.update {
                    it.copy(timerFraction = (left.toFloat() / GameConfig.SENTENCE_TIMER_MS).coerceIn(0f, 1f))
                }
            }
            if (isActive) resolve()
        }
    }

    private fun resolve() {
        timerJob?.cancel()
        timerJob = null
        val current = _state.value
        val item = current.item ?: return
        val correct = evaluateAnswer(item, current.selected, current.swapped)
        outcomes.add(RuleOutcome(item.ruleId, correct))
        val bonus = if (current.streak + 1 >= GameConfig.STREAK_BONUS_FROM) {
            minOf(GameConfig.STREAK_BONUS_CAP, GameConfig.STREAK_BONUS * (current.streak + 2 - GameConfig.STREAK_BONUS_FROM))
        } else {
            0
        }
        val hit = if (correct) GameConfig.DAMAGE_PER_HIT + bonus else 0
        val pips = current.pips.toMutableList()
        if (current.index in pips.indices) {
            pips[current.index] = if (correct) PipState.SOLVED else PipState.MISSED
        }
        val newStreak = if (correct) current.streak + 1 else 0
        val misses = if (correct) current.misses else current.misses + 1
        val knockedOut = misses >= GameConfig.MISS_LIMIT
        _state.update {
            it.copy(
                phase = GamePhase.RESOLVED,
                lastCorrect = correct,
                rivalHp = (it.rivalHp - hit).coerceAtLeast(0),
                misses = misses,
                knockedOut = knockedOut,
                streak = newStreak,
                bestStreak = maxOf(it.bestStreak, newStreak),
                damage = it.damage + hit,
                correctCount = it.correctCount + if (correct) 1 else 0,
                pips = pips,
                timerFraction = 0f,
                stampTick = if (correct) it.stampTick + 1 else it.stampTick,
                wiggleTick = if (correct) it.wiggleTick else it.wiggleTick + 1,
                hitTick = if (correct) it.hitTick + 1 else it.hitTick
            )
        }
        if (knockedOut) {
            viewModelScope.launch {
                delay(GameConfig.KO_DELAY_MS)
                finishRound(burst = false)
            }
        }
    }

    private fun finishRound(burst: Boolean) {
        if (finished) return
        finished = true
        timerJob?.cancel()
        timerJob = null
        _state.update { it.copy(phase = GamePhase.FINISHING) }
        viewModelScope.launch {
            if (burst) delay(GameConfig.BURST_MS)
            val current = _state.value
            val result = RoundResult(
                mode = mode,
                correct = current.correctCount,
                total = outcomes.size.coerceAtLeast(1),
                damage = current.damage,
                bestStreak = current.bestStreak,
                brokenRules = outcomes.filter { !it.correct }.map { it.ruleId }.distinct(),
                rivalDown = current.rivalHp <= 0
            )
            updateProgress(result, outcomes.toList())
            eventChannel.send(GameEvent.Finished(result))
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        timerJob = null
        super.onCleared()
    }
}
