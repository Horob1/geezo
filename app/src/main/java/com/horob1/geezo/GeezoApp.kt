package com.horob1.geezo

import android.app.Application
import com.horob1.geezo.core.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GeezoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GeezoApp)
            modules(appModules)
        }
    }
}