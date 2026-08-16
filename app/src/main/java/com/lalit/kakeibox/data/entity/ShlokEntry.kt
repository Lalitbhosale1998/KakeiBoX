package com.personal.kakeibox.data.entity

data class ShlokEntry(
    val id: String,
    val situationKey: String,
    val situationTitleEn: String,
    val situationTitleMr: String,
    val situationTitleJa: String,
    val whenToReadEn: String = "",
    val whenToReadJa: String = "",
    val sanskritText: String,
    val readingText: String,
    val englishMeaning: String,
    val marathiMeaning: String,
    val recommendedTimeSlot: String
)
