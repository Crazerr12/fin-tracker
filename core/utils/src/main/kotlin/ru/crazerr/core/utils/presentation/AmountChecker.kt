package ru.crazerr.core.utils.presentation

import android.annotation.SuppressLint
import kotlin.math.roundToInt
import kotlin.text.Regex

fun String.isValidAmount(): Result<String> {
    val regex = Regex("^\\d*?(\\.\\d{0,2})?$")

    val formatted = if (this.contains(".")) {
        this.substringBefore(".") + "." + this.substringAfter(".").take(2)
    } else {
        this
    }

    return if (regex.matches(formatted)) {
        Result.success(formatted)
    } else {
        Result.failure(Throwable("Invalid amount format"))
    }
}

@SuppressLint("DefaultLocale")
fun Double.formatWithAmountZeros(): String = when {
    this % 1 != 0.0 -> String.format("%.1f", this)
    this % 1 != 0.00 -> String.format("%.2f", this)
    else -> String.format("%.0f", this)
}

fun Double.toAmountFormat(currencySign: Char): String {
    return "${this.toAmountFormat()} $currencySign"
}

fun Double.toAmountFormat(): String {
    val stringNum = ((this * 100.0).roundToInt() / 100.0).toString()
    val (integerPart, decimalPart) = stringNum.split('.', limit = 2)
        .let { it[0] to it.getOrNull(1) }

    val formattedInteger = integerPart.reversed().chunked(3).joinToString(" ").reversed()
    return if (decimalPart != null && decimalPart.toDouble() / 1 > 0.00) "$formattedInteger.$decimalPart" else formattedInteger
}
