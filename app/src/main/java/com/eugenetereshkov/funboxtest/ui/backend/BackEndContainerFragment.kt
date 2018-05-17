package com.eugenetereshkov.funboxtest.ui.backend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.eugenetereshkov.funboxtest.R
import com.eugenetereshkov.funboxtest.Screen
import com.eugenetereshkov.funboxtest.data.entity.Product
import com.eugenetereshkov.funboxtest.ui.common.BaseFragment
import com.eugenetereshkov.funboxtest.ui.editproduct.EditProductFragment
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.SupportAppNavigator

class BackEndContainerFragment : BaseFragment() {

    companion object {
        fun newInstance() = BackEndContainerFragment()
    }

    override val layoutResId: Int = R.layout.fragment_back_end_container

    private val navigator by lazy {
        object : SupportAppNavigator(activity, childFragmentManager, R.id.container) {
            override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? = null

            override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
                Screen.BACK_END_SCREEN -> BackEndFragment.newInstance()
                Screen.EDIT_PRODUCT_SCREEN -> EditProductFragment.newInstance(data as? Product)
                else -> null
            }

        }
    }
    private val navigatorHolder: NavigatorHolder by inject(name = "backEnd")
    private val router: Router by inject(name = "backEnd")

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        childFragmentManager.findFragmentById(R.id.container)
                ?: router.replaceScreen(Screen.BACK_END_SCREEN)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() = router.exit()
}