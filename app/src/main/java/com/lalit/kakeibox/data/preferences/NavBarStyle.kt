package com.personal.kakeibox.data.preferences

enum class NavBarStyle {
    FULL_WIDTH,
    FLOATING;

    companion object {
        fun fromStorage(value: String?): NavBarStyle {
            return try {
                value?.let { valueOf(it) } ?: FULL_WIDTH
            } catch (e: Exception) {
                FULL_WIDTH
            }
        }
    }
}
