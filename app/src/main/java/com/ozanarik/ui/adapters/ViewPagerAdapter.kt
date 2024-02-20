package com.ozanarik.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ozanarik.ui.fragments.game_screens.AllGamesListFragment
import com.ozanarik.ui.fragments.game_screens.DLCsFragment
import com.ozanarik.ui.fragments.game_screens.GamesFragment
import com.ozanarik.utilities.Constants.Companion.NAME_FRAGMENT_LIST

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return NAME_FRAGMENT_LIST.size
    }

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0->AllGamesListFragment()
            1->GamesFragment()
            2->DLCsFragment()
            else->AllGamesListFragment()
        }


    }






    }
