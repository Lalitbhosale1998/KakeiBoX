package com.personal.kakeibox.data.converter

import androidx.room.TypeConverter
import com.personal.kakeibox.data.entity.SpendCategory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {

    @TypeConverter
    fun fromSpendCategory(category: SpendCategory): String {
        return category.name
    }

    @TypeConverter
    fun toSpendCategory(value: String): SpendCategory {
        return SpendCategory.valueOf(value)
    }

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, formatter) }
    }
}