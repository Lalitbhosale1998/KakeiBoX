package com.personal.kakeibox.data.preferences

enum class GlowIntensity {
    OFF,
    SUBTLE,
    NEON,
    PULSING;

    companion object {
        fun fromStorage(value: String?): GlowIntensity {
            return try {
                value?.let { valueOf(it) } ?: SUBTLE
            } catch (e: Exception) {
                SUBTLE
            }
        }
    }
}
