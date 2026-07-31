package com.personal.kakeibox.data.preferences

enum class BackdropPattern {
    NONE,
    RADAR_DOTS,
    BLUEPRINT_GRID,
    COCKPIT_STRIPES,
    WATER_RIPPLE,   // 朱塗り — horizontal sine-wave lines (still water)
    WEAVE_DOTS;     // 御神酒 — hexagonal honeycomb dot grid (barrel straw weave)

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
