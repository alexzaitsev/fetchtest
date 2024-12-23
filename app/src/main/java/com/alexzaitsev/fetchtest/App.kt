package com.alexzaitsev.fetchtest

import android.app.Application
import com.alexzaitsev.fetchtest.data.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModules + dataModule)
        }
    }
}