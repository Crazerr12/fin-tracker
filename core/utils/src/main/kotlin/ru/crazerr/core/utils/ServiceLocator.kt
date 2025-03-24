package ru.crazerr.core.utils

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.crazerr.core.utils.notifications.NotificationSender
import ru.crazerr.core.utils.resourceManager.AndroidResourceManager
import ru.crazerr.core.utils.resourceManager.ResourceManager
import ru.crazerr.core.utils.snackbar.SnackbarManager

val utilsModule = module {
    singleOf(::SnackbarManager)
    singleOf(::AndroidResourceManager) { bind<ResourceManager>() }
    singleOf(::NotificationSender)
}