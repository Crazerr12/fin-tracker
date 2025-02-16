package ru.crazerr.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val DATABASE_NAME = "main-database"

val databaseModule = module {
    single {
        createRoomDatabase(context = get())
    }
}

fun createRoomDatabase(context: Context): RoomDatabase =
    Room.databaseBuilder(context = context, AppDatabase::class.java, DATABASE_NAME).build()