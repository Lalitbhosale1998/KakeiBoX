package com.personal.kakeibox.data.preferences

enum class ThemeFlavor {
    DYNAMIC_MATERIAL;

    companion object {
        fun fromStorage(value: String?): ThemeFlavor {
            return DYNAMIC_MATERIAL
        }
    }
}
