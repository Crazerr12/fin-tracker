package ru.crazerr.feature.budget.data.exception

class BudgetAlreadyExistException(
    override val message: String? = "The budget for this month has already been set for the specified category."
) : Exception()