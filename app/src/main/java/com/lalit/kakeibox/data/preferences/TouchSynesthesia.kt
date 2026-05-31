package com.personal.kakeibox.data.preferences

enum class TouchSynesthesia {
    OFF,
    SUBTLE,
    CASSETTE_CLICK,
    MECHANICAL;

    companion object {
        fun fromStorage(value: String?): TouchSynesthesia {
            return try {
                value?.let { valueOf(it) } ?: SUBTLE
            } catch (e: Exception) {
                SUBTLE
            }
        }
    }
}
