package ru.crazerr.core.database

import android.content.Context
import androidx.room.Room
import org.koin.dsl.module

private const val DATABASE_NAME = "main-database"

val databaseModule = module {
    single<AppDatabase> {
        createRoomDatabase(context = get())
    }
}

fun createRoomDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()