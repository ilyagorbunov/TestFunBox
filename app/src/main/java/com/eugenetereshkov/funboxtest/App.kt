package com.eugenetereshkov.funboxtest

import android.app.Application
import com.eugenetereshkov.funboxtest.di.appModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(listOf(appModule))
    }
}