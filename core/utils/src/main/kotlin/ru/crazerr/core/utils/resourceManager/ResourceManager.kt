package ru.crazerr.core.utils.resourceManager

import android.content.Context
import androidx.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes id: Int): String
}

internal class AndroidResourceManager(private val context: Context) : ResourceManager {
    override fun getString(id: Int): String {
        return context.getString(id)
    }
}