package com.personal.kakeibox.util

import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

object DateUtils {

    fun getCurrentMonth(): Int = LocalDate.now().monthValue

    fun getCurrentYear(): Int = LocalDate.now().year

    fun getMonthName(month: Int, locale: Locale = Locale.getDefault()): String {
        return Month.of(month).getDisplayName(TextStyle.FULL, locale)
    }

    fun getShortMonthName(month: Int, locale: Locale = Locale.getDefault()): String {
        return Month.of(month).getDisplayName(TextStyle.SHORT, locale)
    }

    // Returns list of years from 2020 to current year + 1
    fun getYearRange(): List<Int> {
        val currentYear = getCurrentYear()
        return (2020..currentYear + 1).toList()
    }

    // Format month/year as display string e.g. "April 2026"
    fun formatMonthYear(month: Int, year: Int, locale: Locale = Locale.getDefault()): String {
        return "${getMonthName(month, locale)} $year"
    }
}