package com.personal.kakeibox.data.preferences

enum class ThemeStyle {
    M3_EXPRESSIVE,
    RETRO_SPACE;

    companion object {
        fun fromStorage(value: String?): ThemeStyle {
            return try {
                value?.let { valueOf(it) } ?: M3_EXPRESSIVE
            } catch (e: Exception) {
                M3_EXPRESSIVE
            }
        }
    }
}
