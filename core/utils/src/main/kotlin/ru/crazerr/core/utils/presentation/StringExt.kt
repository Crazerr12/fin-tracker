package ru.crazerr.core.utils.presentation

fun String.capitalizeFirst() =
    this.replaceFirstChar { if (it.isUpperCase()) it.titlecase() else it.toString() }