package ru.crazerr.core.utils.date

import android.content.Context
import ru.crazerr.core.utils.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.toFormatDate(context: Context): String {
    val today = LocalDate.now()
    return when (this) {
        today.plusDays(1) -> context.getString(R.string.tomorrow)
        today -> context.getString(R.string.today)
        today.minusDays(1) -> context.getString(R.string.yesterday)
        else -> {
            val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
            this.format(formatter)
        }
    }
}

fun LocalDate.toMonthYearFormat(): String {
    val formatter = DateTimeFormatter.ofPattern("LLLL yyyy", Locale.getDefault())
    return this.format(formatter)
}