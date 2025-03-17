package ru.crazerr.feature.budget.data.workManager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import ru.crazerr.feature.domain.api.Budget
import java.util.Calendar
import java.util.concurrent.TimeUnit

internal class BudgetManager(private val context: Context) {
    fun createNewBudget(budget: Budget) {
        val inputData = workDataOf(
            "repeatBudgetId" to budget.repeatBudgetId,
            "maxAmount" to budget.maxAmount,
            "categoryId" to budget.category.id,
            "isAlarm" to budget.isAlarm,
            "isWarning" to budget.isWarning,
        )

        val workRequest = PeriodicWorkRequestBuilder<RepeatBudgetWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateDelay(), TimeUnit.MILLISECONDS)
            .setInputData(inputData = inputData)
            .build()

        WorkManager.getInstance(context = context)
            .enqueueUniquePeriodicWork(
                "repeat_budget_${budget.repeatBudgetId}",
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
    }

    private fun calculateDelay(): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 55)
            set(Calendar.SECOND, 0)
        }

        if (now.after(target)) {
            target.add(Calendar.DAY_OF_MONTH, 1)
        }

        return target.timeInMillis - now.timeInMillis
    }
}