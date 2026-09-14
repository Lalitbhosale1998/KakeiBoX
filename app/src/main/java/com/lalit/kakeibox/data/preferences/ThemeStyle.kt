package com.personal.kakeibox.data.preferences

enum class ThemeStyle {
    DEFAULT,
    EDITORIAL_POSTER;

    companion object {
        fun fromStorage(value: String?): ThemeStyle {
            return try {
                value?.let { valueOf(it) } ?: DEFAULT
            } catch (e: Exception) {
                DEFAULT
            }
        }
    }
}
