package ru.crazerr.core.utils.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import ru.crazerr.core.utils.R
import ru.crazerr.core.utils.resourceManager.ResourceManager

const val BUDGET_CHANNEL_ID = "budget_channel"

class NotificationSender(
    private val context: Context,
    resourceManager: ResourceManager,
) {
    init {
        createNotificationChannel(
            name = resourceManager.getString(R.string.budget_channel_name),
            descriptionText = resourceManager.getString(R.string.budget_channel_description)
        )
    }

    fun sendNotification(title: String, textContent: String) {
        var builder = NotificationCompat.Builder(context, BUDGET_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(1, builder)
    }

    private fun createNotificationChannel(name: String, descriptionText: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(BUDGET_CHANNEL_ID, name, importance).apply {
            description = descriptionText
            enableVibration(true)
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}