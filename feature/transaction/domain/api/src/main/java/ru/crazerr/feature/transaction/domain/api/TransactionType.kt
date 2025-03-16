package ru.crazerr.feature.transaction.domain.api

enum class TransactionType(val id: Int) {
    Income(0), Expense(1), All(2)
}