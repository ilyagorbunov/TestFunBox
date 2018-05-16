package com.eugenetereshkov.funboxtest.presenter.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router

class MainViewModel(
        private val router: Router
) : ViewModel() {

    val intervalLiveData = MutableLiveData<Long>()

    private val disposable = CompositeDisposable()

    init {
//        Flowable.interval(0, 1000, TimeUnit.MILLISECONDS, Schedulers.computation())
//                .subscribe { intervalLiveData.postValue(it) }
//                .bindTo(disposable)
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    fun onBackPressed() {
        router.exit()
    }
}