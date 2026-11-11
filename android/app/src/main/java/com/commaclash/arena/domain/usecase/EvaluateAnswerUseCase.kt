package com.commaclash.arena.domain.usecase

import com.commaclash.arena.domain.model.FixKind
import com.commaclash.arena.domain.model.SentenceItem

class EvaluateAnswerUseCase {

    operator fun invoke(item: SentenceItem, selectedIndex: Int, swapped: Boolean): Boolean {
        if (selectedIndex < 0) return false
        if (selectedIndex != item.answerIndex) return false
        return item.fixKind != FixKind.SWAP || swapped
    }
}
