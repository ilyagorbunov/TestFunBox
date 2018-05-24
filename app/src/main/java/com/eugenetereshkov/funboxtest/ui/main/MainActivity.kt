package com.eugenetereshkov.funboxtest.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.presenter.main.MainViewModel
import com.eugenetereshkov.funboxtest.ui.backend.BackEndContainerFragment
import com.eugenetereshkov.funboxtest.ui.common.BaseFragment
import com.eugenetereshkov.funboxtest.ui.storefront.StoreFrontFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator

class MainActivity : AppCompatActivity() {

    companion object {
        const val MAIN_NAVIGATION = "main"
    }

    private val navigator by lazy {
        object : SupportAppNavigator(this, R.id.container) {
            override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? = null

            override fun createFragment(screenKey: String?, data: Any?): Fragment? = null
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectItem = item.itemId
        if (selectItem != currentTab) showTab(selectItem)
        true
    }
    private val navigatorHolder: NavigatorHolder by inject(name = MAIN_NAVIGATION)
    private var currentTab = R.id.navigation_store_front
    private lateinit var tabs: HashMap<String, BaseFragment>
    private val tabKeys = listOf(
            tabIdToFragmentTag(R.id.navigation_store_front),
            tabIdToFragmentTag(R.id.navigation_back_end)
    )
    private val viewModel: MainViewModel by inject()
    private val currentFragment
        get() = supportFragmentManager.findFragmentById(R.id.container) as BaseFragment?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            tabs = createNewFragments()
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, tabs[tabKeys[0]], tabKeys[0])
                    .add(R.id.container, tabs[tabKeys[1]], tabKeys[1])
                    .hide(tabs[tabKeys[1]])
                    .commitNow()
        } else {
            tabs = findFragments()
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onResumeFragments() {
        navigatorHolder.setNavigator(navigator)
        super.onResumeFragments()
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun tabIdToFragmentTag(id: Int) = "tab_$id"

    private fun showTab(item: Int) {
        supportFragmentManager.beginTransaction()
                .hide(tabs[tabIdToFragmentTag(currentTab)])
                .show(tabs[tabIdToFragmentTag(item)])
                .commit()
        currentTab = item
    }

    private fun createNewFragments(): HashMap<String, BaseFragment> = hashMapOf(
            tabKeys[0] to StoreFrontFragment.newInstance(),
            tabKeys[1] to BackEndContainerFragment.newInstance()
    )

    private fun findFragments(): HashMap<String, BaseFragment> = hashMapOf(
            tabKeys[0] to supportFragmentManager.findFragmentByTag(tabKeys[0]) as BaseFragment,
            tabKeys[1] to supportFragmentManager.findFragmentByTag(tabKeys[1]) as BaseFragment
    )

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: viewModel.onBackPressed()
    }
}
