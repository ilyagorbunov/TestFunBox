package com.eugenetereshkov.funboxtest.di

import android.os.Environment
import com.eugenetereshkov.funboxtest.data.repository.IProductRepository
import com.eugenetereshkov.funboxtest.data.repository.ProductRepository
import com.eugenetereshkov.funboxtest.data.storage.IStorageFormatVisitor
import com.eugenetereshkov.funboxtest.data.storage.StorageFormatVisitor
import com.eugenetereshkov.funboxtest.presenter.backend.BackEndViewModel
import com.eugenetereshkov.funboxtest.presenter.editproduct.EditProductViewModel
import com.eugenetereshkov.funboxtest.presenter.main.MainViewModel
import com.eugenetereshkov.funboxtest.presenter.storefront.StoreFrontViewModel
import com.eugenetereshkov.funboxtest.ui.backend.BackEndContainerFragment.Companion.BACK_END_NAVIGATION
import com.eugenetereshkov.funboxtest.ui.editproduct.EditProductFragment
import com.eugenetereshkov.funboxtest.ui.main.MainActivity.Companion.MAIN_NAVIGATION
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext
import ru.terrakok.cicerone.Cicerone


val appModule = applicationContext {
    val mainCicerone = Cicerone.create()
    bean(MAIN_NAVIGATION) { mainCicerone.router }
    bean(MAIN_NAVIGATION) { mainCicerone.navigatorHolder }


    val backEndCicerone = Cicerone.create()
    bean(BACK_END_NAVIGATION) { backEndCicerone.router }
    bean(BACK_END_NAVIGATION) { backEndCicerone.navigatorHolder }

    bean {
        StorageFormatVisitor(
                assets = androidApplication().resources.assets,
                filesDir = androidApplication().getExternalFilesDir(Environment.MEDIA_MOUNTED)
        ) as IStorageFormatVisitor
    }
    bean { ProductRepository(get()) as IProductRepository }

    viewModel { MainViewModel(get(name = MAIN_NAVIGATION), get()) }
    viewModel { BackEndViewModel(get(name = BACK_END_NAVIGATION)) }
    viewModel { params ->
        EditProductViewModel(get(name = BACK_END_NAVIGATION), params[EditProductFragment.PRODUCT_ID])
    }
    viewModel { StoreFrontViewModel(get(name = MAIN_NAVIGATION)) }
}