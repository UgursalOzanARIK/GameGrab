package com.ozanarik.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.WindowManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ozanarik.gamegrab.R
import com.ozanarik.gamegrab.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)


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