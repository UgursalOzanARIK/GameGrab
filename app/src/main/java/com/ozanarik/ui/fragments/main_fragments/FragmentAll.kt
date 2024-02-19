package com.ozanarik.ui.fragments.main_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.ozanarik.gamegrab.R
import com.ozanarik.gamegrab.databinding.FragmentAllBinding
import com.ozanarik.ui.adapters.ViewPagerAdapter
import com.ozanarik.utilities.Constants.Companion.NAME_FRAGMENT_LIST
import com.ozanarik.utilities.Constants.Companion.fragmentList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class FragmentAll : Fragment() {

    private lateinit var binding: FragmentAllBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllBinding.inflate(inflater,container,false)

        handleTabLayout()

        return binding.root
    }





    private fun handleTabLayout(){

        viewPagerAdapter = ViewPagerAdapter(childFragmentManager,lifecycle)

        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,position->
            tab.text = NAME_FRAGMENT_LIST[position]
        }.attach()


    }


}