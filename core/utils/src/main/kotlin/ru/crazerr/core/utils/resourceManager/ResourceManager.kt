package ru.crazerr.core.utils.resourceManager

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

interface ResourceManager {
    fun getString(@StringRes id: Int): String

    fun getString(@StringRes id: Int, vararg parameters: String): String

    fun getDrawable(@DrawableRes id: Int): Drawable?
}

internal class AndroidResourceManager(private val context: Context) : ResourceManager {
    override fun getString(id: Int): String {
        return context.getString(id)
    }

    override fun getString(id: Int, vararg parameters: String): String {
        return context.getString(id, *parameters)
    }

    override fun getDrawable(id: Int): Drawable? {
        return ContextCompat.getDrawable(context, id)
    }
}