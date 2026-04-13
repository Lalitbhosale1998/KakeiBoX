package com.personal.kakeibox.data.converter

import androidx.room.TypeConverter
import com.personal.kakeibox.data.entity.SpendCategory

class Converters {

    @TypeConverter
    fun fromSpendCategory(category: SpendCategory): String {
        return category.name
    }

    @TypeConverter
    fun toSpendCategory(value: String): SpendCategory {
        return SpendCategory.valueOf(value)
    }
}