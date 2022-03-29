package com.findmyrecycling

import android.app.Application
import org.koin.android.ext.koin.androidLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

class FindMyRecyclingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin{
           androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@FindMyRecyclingApplication)
            modules(appModule)
        }

    }
}