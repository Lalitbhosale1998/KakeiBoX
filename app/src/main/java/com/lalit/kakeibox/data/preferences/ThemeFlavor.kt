package com.personal.kakeibox.data.preferences

enum class ThemeFlavor {
    DYNAMIC_MATERIAL,
    MIDNIGHT_OBSIDIAN,
    EMERALD_ZEN,
    SUNSET_CORAL,
    TOKYO_GLASS;

    companion object {
        fun fromStorage(value: String?): ThemeFlavor {
            return try {
                value?.let { valueOf(it) } ?: DYNAMIC_MATERIAL
            } catch (e: Exception) {
                DYNAMIC_MATERIAL
            }
        }
    }
}
