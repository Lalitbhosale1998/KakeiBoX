package com.personal.kakeibox.data.preferences

enum class BackdropPattern {
    NONE,
    RADAR_DOTS,
    BLUEPRINT_GRID,
    COCKPIT_STRIPES,
    WATER_RIPPLE,   // 朱塗り — horizontal sine-wave lines (still water)
    WEAVE_DOTS,     // 御神酒 — hexagonal honeycomb dot grid (barrel straw weave)
    TEMPLE_JAALI;   // 🛕 स्थापत्य — Ancient Indian stone relief lattice grid

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
