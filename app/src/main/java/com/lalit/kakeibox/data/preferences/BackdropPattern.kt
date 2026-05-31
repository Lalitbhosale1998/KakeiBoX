package com.personal.kakeibox.data.preferences

enum class BackdropPattern {
    NONE,
    RADAR_DOTS,
    BLUEPRINT_GRID,
    COCKPIT_STRIPES;

    companion object {
        fun fromStorage(value: String?): BackdropPattern {
            return try {
                value?.let { valueOf(it) } ?: NONE
            } catch (e: Exception) {
                NONE
            }
        }
    }
}
