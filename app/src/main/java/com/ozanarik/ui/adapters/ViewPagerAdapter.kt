package com.ozanarik.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ozanarik.utilities.Constants.Companion.NAME_FRAGMENT_LIST
import com.ozanarik.utilities.Constants.Companion.fragmentList

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return NAME_FRAGMENT_LIST.size
    }

    override fun createFragment(position: Int): Fragment {

        return fragmentList[position]


    }






    }
