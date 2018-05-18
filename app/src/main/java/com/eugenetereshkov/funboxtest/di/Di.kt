package com.eugenetereshkov.funboxtest.di

import com.eugenetereshkov.funboxtest.data.storage.RawAppData
import com.eugenetereshkov.funboxtest.presenter.backend.BackEndViewModel
import com.eugenetereshkov.funboxtest.presenter.editproduct.EditProductViewModel
import com.eugenetereshkov.funboxtest.presenter.main.MainViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router


val appModule = applicationContext {
    val mainCicerone = Cicerone.create()
    bean("main") { mainCicerone.router }
    bean("main") { mainCicerone.navigatorHolder }


    val backEndCicerone = Cicerone.create()
    bean("backEnd") { backEndCicerone.router }
    bean("backEnd") { backEndCicerone.navigatorHolder }

    bean { RawAppData(androidApplication().resources.assets) }

    viewModel { MainViewModel(get<Router>(name = "main"), get()) }
    viewModel { BackEndViewModel(get<Router>(name = "backEnd")) }
    viewModel { EditProductViewModel(get<Router>(name = "backEnd")) }
}