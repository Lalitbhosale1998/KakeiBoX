package com.personal.kakeibox.data.preferences

enum class LanguagePreference(val code: String, val label: String) {
    ENGLISH("en", "English"),
    JAPANESE("ja", "日本語");

    companion object {
        fun fromStorage(name: String?): LanguagePreference {
            return entries.find { it.name == name } ?: ENGLISH
        }
    }
}
