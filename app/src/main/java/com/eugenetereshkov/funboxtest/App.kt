package com.eugenetereshkov.funboxtest

import android.app.Application
import com.eugenetereshkov.funboxtest.di.appModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin(listOf(appModule))
    }
}