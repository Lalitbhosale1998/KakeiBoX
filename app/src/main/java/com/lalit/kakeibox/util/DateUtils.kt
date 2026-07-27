package com.personal.kakeibox.util

import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

object DateUtils {

    fun getCurrentMonth(): Int = LocalDate.now().monthValue

    fun getCurrentYear(): Int = LocalDate.now().year

    fun getMonthName(month: Int, locale: Locale = Locale.getDefault()): String {
        val m = try { Month.of(month) } catch(e: Exception) { Month.JANUARY }
        return m.getDisplayName(TextStyle.FULL, locale)
    }

    fun getShortMonthName(month: Int, locale: Locale = Locale.getDefault()): String {
        val m = try { Month.of(month) } catch(e: Exception) { Month.JANUARY }
        return m.getDisplayName(TextStyle.SHORT, locale)
    }

    // Returns list of years from 2022 to 2029
    fun getYearRange(): List<Int> {
        return (2022..2029).toList()
    }

    // Format month/year as display string e.g. "April 2026"
    fun formatMonthYear(month: Int, year: Int, locale: Locale = Locale.getDefault()): String {
        return "${getMonthName(month, locale)} $year"
    }

    /**
     * Calculates the next salary payday (25th of the month).
     * If the 25th falls on a weekend (Saturday or Sunday), payday moves to the preceding Friday.
     * Returns a Pair of (Payday LocalDate, Days Remaining from today).
     */
    fun calculateNextPayday(today: LocalDate = LocalDate.now()): Pair<LocalDate, Long> {
        val info = calculatePaydayProgress(today)
        return Pair(info.nextPayday, info.daysRemaining)
    }

    fun calculatePaydayProgress(today: LocalDate = LocalDate.now()): PaydayProgressInfo {
        fun getAdjustedPayday(yearMonthDate: LocalDate): LocalDate {
            val base25 = yearMonthDate.withDayOfMonth(25)
            return when (base25.dayOfWeek) {
                java.time.DayOfWeek.SATURDAY -> base25.minusDays(1)
                java.time.DayOfWeek.SUNDAY -> base25.minusDays(2)
                else -> base25
            }
        }

        val thisMonthPayday = getAdjustedPayday(today)
        val (prevPayday, targetPayday) = if (!today.isAfter(thisMonthPayday)) {
            val lastMonth = today.withDayOfMonth(1).minusMonths(1)
            Pair(getAdjustedPayday(lastMonth), thisMonthPayday)
        } else {
            val nextMonth = today.withDayOfMonth(1).plusMonths(1)
            Pair(thisMonthPayday, getAdjustedPayday(nextMonth))
        }

        val totalDays = java.time.temporal.ChronoUnit.DAYS.between(prevPayday, targetPayday).coerceAtLeast(1L)
        val elapsed = java.time.temporal.ChronoUnit.DAYS.between(prevPayday, today).coerceAtLeast(0L)
        val daysRemaining = java.time.temporal.ChronoUnit.DAYS.between(today, targetPayday).coerceAtLeast(0L)
        val ratio = (elapsed.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f)

        return PaydayProgressInfo(
            nextPayday = targetPayday,
            daysRemaining = daysRemaining,
            elapsedDays = elapsed,
            totalDaysInCycle = totalDays,
            progressRatio = ratio
        )
    }
}

data class PaydayProgressInfo(
    val nextPayday: LocalDate,
    val daysRemaining: Long,
    val elapsedDays: Long,
    val totalDaysInCycle: Long,
    val progressRatio: Float
)