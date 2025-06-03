package ru.crazerr.feature.budget.data.workManager

import android.content.Context
import android.icu.util.Calendar
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.crazerr.core.database.AppDatabase
import ru.crazerr.core.database.budgets.model.BudgetEntity
import java.time.LocalDate

internal class RepeatBudgetWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters), KoinComponent {

    private val appDatabase: AppDatabase by inject()
    private val budgetsDao = appDatabase.budgetsDao()

    override fun doWork(): Result {
        return try {
            val now = Calendar.getInstance()
            val lastDay = now.getActualMaximum(Calendar.DAY_OF_MONTH)

            if (now.get(Calendar.DAY_OF_MONTH) == lastDay) {
                val repeatBudgetId = inputData.getLong("repeatBudgetId", -1)
                val maxAmount = inputData.getDouble("maxAmount", 0.0)
                val categoryId = inputData.getLong("categoryId", -1)
                val isAlarm = inputData.getBoolean("isAlarm", false)
                val isWarning = inputData.getBoolean("isWarning", false)

                if (repeatBudgetId == -1L || categoryId == -1L) {
                    return Result.failure()
                }

                CoroutineScope(Dispatchers.IO).launch {
                    budgetsDao.insert(
                        BudgetEntity(
                            repeatBudgetId = repeatBudgetId,
                            categoryId = categoryId,
                            maxAmount = maxAmount,
                            isAlarm = isAlarm,
                            isWarning = isWarning,
                            date = LocalDate.now().plusDays(1)
                        )
                    )
                }
            }

            Result.success()
        } catch (ex: Exception) {
            Result.retry()
        }
    }
}