package com.ozanarik.ui.fragments.main_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.ozanarik.gamegrab.databinding.FragmentAllBinding
import com.ozanarik.ui.adapters.ViewPagerAdapter
import com.ozanarik.utilities.Constants.Companion.NAME_FRAGMENT_LIST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint

class FragmentAll : Fragment() {


    private lateinit var auth:FirebaseAuth
    private lateinit var binding: FragmentAllBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllBinding.inflate(inflater,container,false)

        auth= FirebaseAuth.getInstance()

        handleTabLayout()

        handleGameTitle()











        return binding.root
    }
    
    
    private fun handleGameTitle(){
        binding.apply { 
            collapsingToolbarLayout.title = if (auth.currentUser!=null){
                "Welcome ${auth.currentUser!!.displayName}!"
            }else{
                "Welcome!"
            }
        }
    }





    private fun handleTabLayout(){

        viewPagerAdapter = ViewPagerAdapter(childFragmentManager,lifecycle)

        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,position->
            tab.text = NAME_FRAGMENT_LIST[position]
        }.attach()


    }


}