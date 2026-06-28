package com.personal.kakeibox.data.preferences

enum class DynamicTonalStyle {
    TONAL_SPOT,
    VIBRANT,
    EXPRESSIVE,
    FRUIT_SALAD,
    RAINBOW;

    companion object {
        fun fromStorage(value: String?): DynamicTonalStyle {
            return try {
                value?.let { valueOf(it) } ?: TONAL_SPOT
            } catch (e: Exception) {
                TONAL_SPOT
            }
        }
    }
}
