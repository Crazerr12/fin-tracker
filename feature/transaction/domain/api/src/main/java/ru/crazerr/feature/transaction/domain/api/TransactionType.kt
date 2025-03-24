package ru.crazerr.feature.transaction.domain.api

enum class TransactionType(val id: Int) {
    Income(0), Expense(1), All(2);

    companion object {
        fun fromId(id: Int): TransactionType? {
            return entries.find { it.id == id }
        }
    }
}