package com.commaclash.arena.data.sample

import com.commaclash.arena.domain.model.FixKind
import com.commaclash.arena.domain.model.GrammarRule
import com.commaclash.arena.domain.model.RuleTier
import com.commaclash.arena.domain.model.SentenceItem
import com.commaclash.arena.domain.model.SentenceToken
import com.commaclash.arena.domain.model.TokenKind

object SampleData {

    val tiers: List<RuleTier> = listOf(
        RuleTier(1, "TIER 1 · COMMA BASICS", listOf("oxford_comma", "comma_conjunction", "comma_intro", "comma_address", "comma_adjectives")),
        RuleTier(2, "TIER 2 · APOSTROPHES", listOf("its_vs_its", "singular_possessive", "plural_possessive", "contractions", "no_plural_apostrophe")),
        RuleTier(3, "TIER 3 · CONFUSED PAIRS", listOf("your_youre", "their_theyre", "whos_whose", "then_than", "affect_effect")),
        RuleTier(4, "TIER 4 · JOINING CLAUSES", listOf("comma_splice", "semicolon_link", "semicolon_however", "colon_list", "dash_aside")),
        RuleTier(5, "TIER 5 · POLISH", listOf("nonrestrictive_commas", "no_comma_that", "quotation_punctuation", "serial_semicolon", "parenthetical_commas"))
    )

    val rules: List<GrammarRule> = listOf(
        GrammarRule("oxford_comma", 1, "LIST COMMA", "Use a comma before the final and in a list of three or more.", "She packed commas, semicolons, and a spare apostrophe.", "She packed commas, semicolons and a spare apostrophe."),
        GrammarRule("comma_conjunction", 1, "COMMA BEFORE AND", "Put a comma before and, but or so when it joins two complete sentences.", "The bell rang, and the editor stepped into the ring.", "The bell rang and the editor stepped into the ring."),
        GrammarRule("comma_intro", 1, "INTRO COMMA", "A comma follows an introductory phrase before the main clause.", "After the final bell, the editor sharpened her pen.", "After the final bell the editor sharpened her pen."),
        GrammarRule("comma_address", 1, "DIRECT ADDRESS", "A comma fences off the name of the person being addressed.", "Step into the ring, Marta.", "Step into the ring Marta."),
        GrammarRule("comma_adjectives", 1, "PAIRED ADJECTIVES", "Use a comma between two adjectives of equal weight.", "It was a long, brutal round.", "It was a long brutal round."),
        GrammarRule("its_vs_its", 2, "ITS / IT'S", "It's means it is; its shows possession.", "The arena lost its temper.", "The arena lost it's temper."),
        GrammarRule("singular_possessive", 2, "SINGULAR POSSESSIVE", "One owner takes an apostrophe before the s.", "The editor's pen never slipped.", "The editors pen never slipped."),
        GrammarRule("plural_possessive", 2, "PLURAL POSSESSIVE", "Several owners ending in s take the apostrophe after the s.", "The judges' scores were read aloud.", "The judge's scores were read aloud."),
        GrammarRule("contractions", 2, "CONTRACTIONS", "An apostrophe marks the letters a contraction drops.", "Don't drop your guard.", "Dont drop your guard."),
        GrammarRule("no_plural_apostrophe", 2, "PLAIN PLURALS", "A plain plural never takes an apostrophe.", "The ring was full of commas.", "The ring was full of comma's."),
        GrammarRule("your_youre", 3, "YOUR / YOU'RE", "You're means you are; your shows possession.", "You're going to lose this round.", "Your going to lose this round."),
        GrammarRule("their_theyre", 3, "THEIR / THERE / THEY'RE", "They're means they are, their shows possession, there is a place.", "They're already in the ring.", "Their already in the ring."),
        GrammarRule("whos_whose", 3, "WHO'S / WHOSE", "Who's means who is; whose asks about an owner.", "Whose pen is this?", "Who's pen is this?"),
        GrammarRule("then_than", 3, "THEN / THAN", "Than compares; then tells you when.", "She is faster than the rival.", "She is faster then the rival."),
        GrammarRule("affect_effect", 3, "AFFECT / EFFECT", "Affect is the verb; effect is the result.", "The comma had a real effect.", "The comma had a real affect."),
        GrammarRule("comma_splice", 4, "COMMA SPLICE", "A comma alone cannot join two complete sentences, so the mark must go.", "The round was over; the pen said nothing.", "The round was over, the pen said nothing."),
        GrammarRule("semicolon_link", 4, "LINKING SEMICOLON", "A semicolon joins two complete, closely related sentences.", "The round was over; the pen said nothing.", "The round was over the pen said nothing."),
        GrammarRule("semicolon_however", 4, "SEMICOLON BEFORE HOWEVER", "Put a semicolon before however or therefore joining two clauses.", "He trained for months; however, the comma beat him.", "He trained for months however, the comma beat him."),
        GrammarRule("colon_list", 4, "COLON BEFORE A LIST", "A colon introduces a list after a complete sentence.", "She carried three things: a pen, a rule and a grudge.", "She carried three things a pen, a rule and a grudge."),
        GrammarRule("dash_aside", 4, "DASH FOR AN ASIDE", "A dash marks an abrupt aside at the end of a sentence.", "He answered fast - too fast.", "He answered fast too fast."),
        GrammarRule("nonrestrictive_commas", 5, "NON-RESTRICTIVE CLAUSE", "An extra clause starting with who or which sits inside a pair of commas.", "My coach, who never blinks, called the fix.", "My coach who never blinks, called the fix."),
        GrammarRule("no_comma_that", 5, "NO COMMA WITH THAT", "A clause starting with that is essential, so it takes no comma.", "The pen that she borrowed was missing.", "The pen, that she borrowed was missing."),
        GrammarRule("quotation_punctuation", 5, "COMMA BEFORE A QUOTE", "A comma introduces a quoted sentence after a speech verb.", "The coach shouted, \"Strike it.\"", "The coach shouted \"Strike it.\""),
        GrammarRule("serial_semicolon", 5, "SERIAL SEMICOLON", "Semicolons separate list items that already contain commas.", "The panel came from Leeds, England; Cork, Ireland and Perth, Australia.", "The panel came from Leeds, England Cork, Ireland and Perth, Australia."),
        GrammarRule("parenthetical_commas", 5, "PARENTHETICAL COMMAS", "Phrases such as for example sit inside a pair of commas.", "The rule, for example, covers names.", "The rule for example, covers names.")
    )

    private val sources: List<SentenceSource> = listOf(
        SentenceSource("oxford_comma", FixKind.INSERT, ",", "She packed commas, semicolons^ and a spare apostrophe."),
        SentenceSource("oxford_comma", FixKind.INSERT, ",", "The judges praised her timing, her nerve^ and her red pen."),
        SentenceSource("oxford_comma", FixKind.INSERT, ",", "He brought a notebook, two pencils^ and a stubborn opinion."),
        SentenceSource("comma_conjunction", FixKind.INSERT, ",", "The bell rang^ and the editor stepped into the ring."),
        SentenceSource("comma_conjunction", FixKind.INSERT, ",", "She read the line twice^ but the error stayed hidden."),
        SentenceSource("comma_conjunction", FixKind.INSERT, ",", "The crowd went quiet^ so the writer took her shot."),
        SentenceSource("comma_intro", FixKind.INSERT, ",", "After the final bell^ the editor sharpened her pen."),
        SentenceSource("comma_intro", FixKind.INSERT, ",", "In the second round^ the rival lost his grip."),
        SentenceSource("comma_intro", FixKind.INSERT, ",", "Before the match began^ she rehearsed every rule."),
        SentenceSource("comma_address", FixKind.INSERT, ",", "Step into the ring^ Marta."),
        SentenceSource("comma_address", FixKind.INSERT, ",", "You are late^ champion."),
        SentenceSource("comma_address", FixKind.INSERT, ",", "Hold the line^ editor."),
        SentenceSource("comma_adjectives", FixKind.INSERT, ",", "It was a long^ brutal round."),
        SentenceSource("comma_adjectives", FixKind.INSERT, ",", "She threw a quick^ clean correction."),
        SentenceSource("comma_adjectives", FixKind.INSERT, ",", "He wore a worn^ inky jacket."),
        SentenceSource("its_vs_its", FixKind.SWAP, "", "The arena lost <it's|its> temper."),
        SentenceSource("its_vs_its", FixKind.SWAP, "", "<Its|It's> too late to fix that line."),
        SentenceSource("its_vs_its", FixKind.SWAP, "", "The pen dropped <it's|its> cap."),
        SentenceSource("singular_possessive", FixKind.SWAP, "", "The <editors|editor's> pen never slipped."),
        SentenceSource("singular_possessive", FixKind.SWAP, "", "The <writers|writer's> hand shook once."),
        SentenceSource("singular_possessive", FixKind.SWAP, "", "The <champions|champion's> comma landed first."),
        SentenceSource("plural_possessive", FixKind.SWAP, "", "The <judge's|judges'> scores were read aloud."),
        SentenceSource("plural_possessive", FixKind.SWAP, "", "The <writer's|writers'> room smelled of ink."),
        SentenceSource("plural_possessive", FixKind.SWAP, "", "The <rival's|rivals'> corner stayed silent."),
        SentenceSource("contractions", FixKind.SWAP, "", "<Dont|Don't> drop your guard."),
        SentenceSource("contractions", FixKind.SWAP, "", "<Were|We're> one comma from the belt."),
        SentenceSource("contractions", FixKind.SWAP, "", "She <didnt|didn't> see the semicolon coming."),
        SentenceSource("no_plural_apostrophe", FixKind.SWAP, "", "The ring was full of <comma's|commas>."),
        SentenceSource("no_plural_apostrophe", FixKind.SWAP, "", "He collected rare <apostrophe's|apostrophes>."),
        SentenceSource("no_plural_apostrophe", FixKind.SWAP, "", "Two <judge's|judges> left the hall."),
        SentenceSource("your_youre", FixKind.SWAP, "", "<Your|You're> going to lose this round."),
        SentenceSource("your_youre", FixKind.SWAP, "", "Keep <you're|your> pen steady."),
        SentenceSource("your_youre", FixKind.SWAP, "", "<Your|You're> the one who missed it."),
        SentenceSource("their_theyre", FixKind.SWAP, "", "<Their|They're> already in the ring."),
        SentenceSource("their_theyre", FixKind.SWAP, "", "The writers lost <there|their> nerve."),
        SentenceSource("their_theyre", FixKind.SWAP, "", "<Their|There> is no comma here."),
        SentenceSource("whos_whose", FixKind.SWAP, "", "<Who's|Whose> pen is this?"),
        SentenceSource("whos_whose", FixKind.SWAP, "", "<Whose|Who's> ready for round two?"),
        SentenceSource("whos_whose", FixKind.SWAP, "", "The editor <who's|whose> name you forgot returned."),
        SentenceSource("then_than", FixKind.SWAP, "", "She is faster <then|than> the rival."),
        SentenceSource("then_than", FixKind.SWAP, "", "Fix the comma, <than|then> throw the punch."),
        SentenceSource("then_than", FixKind.SWAP, "", "Nothing stings more <then|than> a splice."),
        SentenceSource("affect_effect", FixKind.SWAP, "", "The comma had a real <affect|effect>."),
        SentenceSource("affect_effect", FixKind.SWAP, "", "One slip can <effect|affect> the whole line."),
        SentenceSource("affect_effect", FixKind.SWAP, "", "The <affect|effect> of the semicolon was instant."),
        SentenceSource("comma_splice", FixKind.REMOVE, "", "The round was over!, the pen said nothing."),
        SentenceSource("comma_splice", FixKind.REMOVE, "", "She fixed the line!, he conceded the point."),
        SentenceSource("comma_splice", FixKind.REMOVE, "", "The crowd roared!, the editor smiled."),
        SentenceSource("semicolon_link", FixKind.INSERT, ";", "The round was over^ the Red Pen said nothing."),
        SentenceSource("semicolon_link", FixKind.INSERT, ";", "She never guessed^ she knew the rule."),
        SentenceSource("semicolon_link", FixKind.INSERT, ";", "The bell rang twice^ nobody moved."),
        SentenceSource("semicolon_however", FixKind.INSERT, ";", "He trained for months^ however, the comma beat him."),
        SentenceSource("semicolon_however", FixKind.INSERT, ";", "The line looked clean^ however, a splice hid inside."),
        SentenceSource("semicolon_however", FixKind.INSERT, ";", "She was ready^ therefore, the round was short."),
        SentenceSource("colon_list", FixKind.INSERT, ":", "She carried three things^ a pen, a rule and a grudge."),
        SentenceSource("colon_list", FixKind.INSERT, ":", "The kit held two items^ ink and nerve."),
        SentenceSource("colon_list", FixKind.INSERT, ":", "Remember the order^ read, fix, strike."),
        SentenceSource("dash_aside", FixKind.INSERT, "-", "He answered fast^ too fast."),
        SentenceSource("dash_aside", FixKind.INSERT, "-", "She knew the rule^ or thought she did."),
        SentenceSource("dash_aside", FixKind.INSERT, "-", "The round ended^ finally."),
        SentenceSource("nonrestrictive_commas", FixKind.INSERT, ",", "My coach^ who never blinks, called the fix."),
        SentenceSource("nonrestrictive_commas", FixKind.INSERT, ",", "The rival^ who writes in red, lost the round."),
        SentenceSource("nonrestrictive_commas", FixKind.INSERT, ",", "The manual^ which weighs a ton, settled it."),
        SentenceSource("no_comma_that", FixKind.REMOVE, "", "The pen!, that she borrowed was missing."),
        SentenceSource("no_comma_that", FixKind.REMOVE, "", "The rule!, that saved her was simple."),
        SentenceSource("no_comma_that", FixKind.REMOVE, "", "The sentence!, that he wrote needed nothing."),
        SentenceSource("quotation_punctuation", FixKind.INSERT, ",", "The coach shouted^ \"Strike it.\""),
        SentenceSource("quotation_punctuation", FixKind.INSERT, ",", "She whispered^ \"Your line is clean.\""),
        SentenceSource("quotation_punctuation", FixKind.INSERT, ",", "He answered^ \"Not this time.\""),
        SentenceSource("serial_semicolon", FixKind.INSERT, ";", "The panel came from Leeds, England^ Cork, Ireland and Perth, Australia."),
        SentenceSource("serial_semicolon", FixKind.INSERT, ";", "We met in Oslo, Norway^ Bath, England and Tulsa, Oklahoma."),
        SentenceSource("serial_semicolon", FixKind.INSERT, ";", "The judges were Ana, a poet^ Theo, a critic and Mia, an editor."),
        SentenceSource("parenthetical_commas", FixKind.INSERT, ",", "The rule^ for example, covers names."),
        SentenceSource("parenthetical_commas", FixKind.INSERT, ",", "The semicolon^ for instance, joins two clauses."),
        SentenceSource("parenthetical_commas", FixKind.INSERT, ",", "Her timing^ in fact, was perfect.")
    )

    val sentences: List<SentenceItem> by lazy { sources.map { it.toItem() } }

    val weekLabels: List<String> = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
}

private data class SentenceSource(
    val ruleId: String,
    val fixKind: FixKind,
    val insert: String,
    val source: String
)

private fun SentenceSource.toItem(): SentenceItem {
    val tokens = ArrayList<SentenceToken>()
    var answer = -1
    var gapPending = false
    source.trim().split(' ').forEachIndexed { position, chunk ->
        if (position > 0) {
            tokens.add(SentenceToken(tokens.size, TokenKind.GAP, ""))
            if (gapPending) {
                answer = tokens.size - 1
                gapPending = false
            }
        }
        when {
            chunk.startsWith("<") && chunk.contains('>') -> {
                val close = chunk.indexOf('>')
                val parts = chunk.substring(1, close).split('|')
                val shown = parts[0]
                val alternate = if (parts.size > 1) parts[1] else parts[0]
                tokens.add(SentenceToken(tokens.size, TokenKind.SWAP, shown, alternate))
                if (fixKind == FixKind.SWAP) answer = tokens.size - 1
                val tail = chunk.substring(close + 1)
                if (tail.isNotEmpty()) tokens.add(SentenceToken(tokens.size, TokenKind.WORD, tail))
            }
            chunk.contains('!') || chunk.contains('~') -> {
                val at = chunk.indexOfFirst { it == '!' || it == '~' }
                val isAnswer = chunk[at] == '!'
                val word = chunk.substring(0, at)
                val mark = chunk.substring(at + 1)
                if (word.isNotEmpty()) tokens.add(SentenceToken(tokens.size, TokenKind.WORD, word))
                tokens.add(SentenceToken(tokens.size, TokenKind.MARK, mark))
                if (isAnswer) answer = tokens.size - 1
            }
            chunk.endsWith("^") -> {
                tokens.add(SentenceToken(tokens.size, TokenKind.WORD, chunk.dropLast(1)))
                gapPending = true
            }
            else -> tokens.add(SentenceToken(tokens.size, TokenKind.WORD, chunk))
        }
    }
    return SentenceItem(ruleId, fixKind, tokens, answer, insert)
}
