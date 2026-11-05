package com.commaclash.arena.domain.model

enum class TokenKind { WORD, GAP, MARK, SWAP }

enum class FixKind { INSERT, REMOVE, SWAP }

data class SentenceToken(
    val index: Int,
    val kind: TokenKind,
    val text: String,
    val alternate: String = ""
)

data class SentenceItem(
    val ruleId: String,
    val fixKind: FixKind,
    val tokens: List<SentenceToken>,
    val answerIndex: Int,
    val insertMark: String = ""
)
