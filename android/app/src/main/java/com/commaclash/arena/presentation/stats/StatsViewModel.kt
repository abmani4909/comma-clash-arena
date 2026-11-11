package com.commaclash.arena.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commaclash.arena.domain.model.WeekDayStat
import com.commaclash.arena.domain.repository.ProgressRepository
import com.commaclash.arena.domain.repository.SentenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RuleAccuracy(val name: String, val percent: Int)

data class StatsUiState(
    val overall: Int = 0,
    val rounds: Int = 0,
    val bestStreak: Int = 0,
    val week: List<WeekDayStat> = emptyList(),
    val ruleAccuracy: List<RuleAccuracy> = emptyList()
)

class StatsViewModel(
    private val sentenceRepository: SentenceRepository,
    progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StatsUiState())
    val state: StateFlow<StatsUiState> = _state.asStateFlow()

    private val weekLabels = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                _state.value = StatsUiState(
                    overall = progress.accuracyPercent,
                    rounds = progress.rounds,
                    bestStreak = progress.bestStreak,
                    week = weekLabels.mapIndexed { index, label ->
                        WeekDayStat(label, progress.weekly.getOrElse(index) { -1 })
                    },
                    ruleAccuracy = sentenceRepository.rules().map { rule ->
                        RuleAccuracy(rule.name, progress.rulePercent(rule.id))
                    }
                )
            }
        }
    }
}
