package com.personal.kakeibox.data.preferences

enum class NavBarStyle {
    FLOATING,              // Option A: Floating Capsule Dock with Morphing FAB
    EXPANDED_SEGMENTED;    // Option B: Expanded M3 Segmented Dock Bar

    companion object {
        fun fromStorage(value: String?): NavBarStyle {
            return try {
                value?.let { valueOf(it) } ?: FLOATING
            } catch (e: Exception) {
                FLOATING
            }
        }
    }
}
