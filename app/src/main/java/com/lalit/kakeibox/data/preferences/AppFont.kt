package com.personal.kakeibox.data.preferences

enum class AppFont {
    NUNITO,
    MONOSPACE,
    SYSTEM_SANS,
    OUTFIT,
    PLAYFAIR,
    GOOGLE_SANS_FLEX,
    CLIMATE_CRISIS,
    LUCKIEST_GUY,
    DELA_GOTHIC_ONE,
    HACHI_MARU_POP,
    KOSUGI_MARU,
    MOCHIY_POP_P_ONE,
    POTTA_ONE,
    RAMPART_ONE,
    WDXL_LUBRIFONT_JPN;

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
