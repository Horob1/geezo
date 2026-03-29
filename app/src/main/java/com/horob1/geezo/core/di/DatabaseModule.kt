package com.horob1.geezo.core.di

import androidx.room.Room
import com.horob1.geezo.core.data.local.database.GeezoAppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val DATABASE_NAME = "geezo_app.db"

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            GeezoAppDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    single { get<GeezoAppDatabase>().networkLogDao() }
}

