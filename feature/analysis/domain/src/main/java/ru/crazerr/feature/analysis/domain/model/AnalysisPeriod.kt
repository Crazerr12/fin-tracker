package ru.crazerr.feature.analysis.domain.model

import java.time.DayOfWeek
import java.time.LocalDate

sealed class AnalysisPeriod {
    abstract fun getStartDate(date: LocalDate): LocalDate
    abstract fun getEndDate(date: LocalDate): LocalDate

    object Week : AnalysisPeriod() {
        override fun getStartDate(date: LocalDate) = date.with(DayOfWeek.MONDAY)
        override fun getEndDate(date: LocalDate) = getStartDate(date).plusDays(6)
    }

    object Month : AnalysisPeriod() {
        override fun getStartDate(date: LocalDate) = date.withDayOfMonth(1)
        override fun getEndDate(date: LocalDate) = getStartDate(date).plusMonths(1).minusDays(1)
    }

    object Year : AnalysisPeriod() {
        override fun getStartDate(date: LocalDate) = date.withDayOfYear(1)
        override fun getEndDate(date: LocalDate) = getStartDate(date).plusYears(1).minusDays(1)
    }
}
