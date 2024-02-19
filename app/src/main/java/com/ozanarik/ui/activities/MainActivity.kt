package com.ozanarik.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ozanarik.gamegrab.R
import com.ozanarik.gamegrab.databinding.ActivityMainBinding
import com.ozanarik.ui.fragments.main_fragments.FragmentAll
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var activeFragment: Fragment = FragmentAll()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)


        handleBottomNav()


        setContentView(binding.root)
    }





    private fun handleBottomNav(){


        val navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        val bottomNav = binding.bottomNav

        NavigationUI.setupWithNavController(bottomNav,navHost.navController)


    }



}