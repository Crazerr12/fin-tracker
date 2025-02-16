package ru.crazerr.core.utils

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.utils.snackbar.SnackbarManager

val utilsModule = module {
    singleOf(::SnackbarManager)
}