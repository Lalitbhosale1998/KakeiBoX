package com.personal.kakeibox.data.preferences

enum class AppFont {
    NUNITO,
    MONOSPACE,
    SYSTEM_SANS,
    OUTFIT,
    PLAYFAIR;

    companion object {
        fun fromStorage(value: String?): AppFont {
            return try {
                value?.let { valueOf(it) } ?: NUNITO
            } catch (e: Exception) {
                NUNITO
            }
        }
    }
}
