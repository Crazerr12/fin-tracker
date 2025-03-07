package ru.crazerr.core.utils.presentation

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

fun Long.toAmountFormat(currencySign: Char): String {
    return "${this.toAmountFormat()} $currencySign"
}

fun Long.toAmountFormat(): String {
    val stringNum = this.toString()

    return if (stringNum.contains('.')) {
        stringNum.substringBefore('.').chunked(3)
            .joinToString(" ") + '.' + stringNum.substringAfter('.')
    } else {
        stringNum.substringBefore('.').chunked(3).joinToString(" ")
    }
}