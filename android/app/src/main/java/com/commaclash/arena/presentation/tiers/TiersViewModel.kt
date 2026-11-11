package com.commaclash.arena.presentation.tiers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commaclash.arena.core.config.GameConfig
import com.commaclash.arena.domain.model.TierState
import com.commaclash.arena.domain.repository.ProgressRepository
import com.commaclash.arena.domain.repository.SentenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TierCardState(
    val id: Int,
    val title: String,
    val ruleCount: Int,
    val masteredCount: Int,
    val state: TierState
)

data class TiersUiState(
    val tiers: List<TierCardState> = emptyList(),
    val lockedNotice: Boolean = false
)

class TiersViewModel(
    private val sentenceRepository: SentenceRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TiersUiState())
    val state: StateFlow<TiersUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                val cards = sentenceRepository.tiers().map { tier ->
                    val mastered = tier.ruleIds.count { ruleId ->
                        progress.ruleAnswered(ruleId) &&
                            progress.rulePercent(ruleId) >= GameConfig.MASTERY_THRESHOLD
                    }
                    val tierState = when {
                        tier.id > progress.unlockedTier -> TierState.LOCKED
                        tier.id == progress.tier -> TierState.ACTIVE
                        else -> TierState.OPEN
                    }
                    TierCardState(tier.id, tier.title, tier.ruleIds.size, mastered, tierState)
                }
                _state.value = _state.value.copy(tiers = cards)
            }
        }
    }

    fun selectTier(tierId: Int): Boolean {
        val card = _state.value.tiers.firstOrNull { it.id == tierId } ?: return false
        if (card.state == TierState.LOCKED) {
            _state.value = _state.value.copy(lockedNotice = true)
            return false
        }
        progressRepository.selectTier(tierId)
        _state.value = _state.value.copy(lockedNotice = false)
        return true
    }
}
