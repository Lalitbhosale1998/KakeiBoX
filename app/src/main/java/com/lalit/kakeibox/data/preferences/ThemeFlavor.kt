package com.personal.kakeibox.data.preferences

enum class ThemeFlavor {
    DYNAMIC_MATERIAL,
    MIDNIGHT_OBSIDIAN,
    EMERALD_ZEN,
    SUNSET_CORAL,
    TOKYO_GLASS,
    SHU_NURI,     // 朱塗り — Vermilion Lacquer (Miyajima Torii Gate)
    O_MIKI,       // 御神酒 — Sacred Sake (Meiji Kazaridaru Barrels)
    NEON_BRUTALIST; // ⚡ Neo-Brutalist Poster Theme

    companion object {
        fun fromStorage(value: String?): ThemeFlavor {
            return try {
                value?.let { valueOf(it) } ?: DYNAMIC_MATERIAL
            } catch (e: Exception) {
                DYNAMIC_MATERIAL
            }
        }
    }
}
