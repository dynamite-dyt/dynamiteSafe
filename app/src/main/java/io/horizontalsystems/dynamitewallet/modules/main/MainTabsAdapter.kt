package io.horizontalsystems.dynamitewallet.modules.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.horizontalsystems.dynamitewallet.modules.balance.BalanceFragment
import io.horizontalsystems.dynamitewallet.modules.settings.main.MainSettingsFragment
import io.horizontalsystems.dynamitewallet.modules.transactions.TransactionsFragment

class MainTabsAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    var currentItem = 0

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> BalanceFragment()
        1 -> TransactionsFragment()
        else -> MainSettingsFragment()
    }

}
