package com.personal.kakeibox.data.preferences

enum class NavBarStyle {
    FLOATING;

    companion object {
        fun fromStorage(value: String?): NavBarStyle {
            return FLOATING
        }
    }
}
