package com.eugenetereshkov.funboxtest.extension

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager


fun ViewGroup.inflate(@LayoutRes layoutResId: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(this.context).inflate(layoutResId, this, attachToRoot)

fun ViewGroup.removeFocus() {
    val focused = this.focusedChild ?: return
    val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(focused.windowToken, 0)
}