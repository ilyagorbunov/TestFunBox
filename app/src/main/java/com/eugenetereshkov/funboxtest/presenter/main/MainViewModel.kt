package com.eugenetereshkov.funboxtest.presenter.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.data.storage.RawAppData
import com.eugenetereshkov.funboxtest.extension.bindTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router

class MainViewModel(
        private val router: Router,
        private val rawAppData: RawAppData
) : ViewModel() {

    val dataLiveData = MutableLiveData<List<Product>>()
    val loadingLiveData = MutableLiveData<Boolean>()

    private val disposable = CompositeDisposable()

    init {
        rawAppData.getProducts()
                .doOnSubscribe { loadingLiveData.postValue(true) }
                .doAfterTerminate { loadingLiveData.postValue(false) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { dataLiveData.value = it },
                        { router.showSystemMessage(it.message) }
                )
                .bindTo(disposable)

    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    fun onBackPressed() {
        router.exit()
    }
}