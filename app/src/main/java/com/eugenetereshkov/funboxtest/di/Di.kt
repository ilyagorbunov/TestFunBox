package com.eugenetereshkov.funboxtest.di

import com.eugenetereshkov.funboxtest.presenter.MainViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import ru.terrakok.cicerone.Cicerone


val appModule = applicationContext {
    val cicerone = Cicerone.create()

    bean { cicerone.router }
    bean { cicerone.navigatorHolder }
    viewModel { MainViewModel(get()) }
}