package com.ozanarik.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ozanarik.business.model.GameGiveAwayResponseItem
import com.ozanarik.gamegrab.R
import com.ozanarik.gamegrab.databinding.FragmentGameFilterDialogBinding
import com.ozanarik.ui.viewmodels.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class GameFilterDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentGameFilterDialogBinding
    private lateinit var onGameFilterListener: OnGameFilterListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameFilterDialogBinding.inflate(inflater,container,false)

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)






        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgClose.setOnClickListener {
            dismiss()
        }

        binding.buttonSetPlatforms.setOnClickListener {
            handleFilterButton()

        }

    }



    private fun handleFilterButton(){
            handleGameFilter()
            dismiss()
    }

    fun setOnGameFilterListener(listener: OnGameFilterListener) {
        this.onGameFilterListener = listener
    }


    private fun handleGameFilter():List<String>{

        val filteredGamesList = mutableListOf<String>()

        binding.apply {

            if (checkBoxAndroid.isChecked) {
                filteredGamesList.add("android")
            }

            if (checkBoxSteam.isChecked) {
                filteredGamesList.add("steam")
            }

            if (checkBoxGOG.isChecked) {
                filteredGamesList.add("gog")
            }

            if (checkBoxIOS.isChecked) {
                filteredGamesList.add("ios")
            }

            if (checkBoxXbox.isChecked) {
                filteredGamesList.add("xbox-one")
            }

            if (checkBoxEpicGames.isChecked) {
                filteredGamesList.add("epic-games-store")
            }

            if (checkBoxOrigin.isChecked) {
                filteredGamesList.add("origin")
            }

        }


        onGameFilterListener.onGameFiltered(filteredGamesList)

        return filteredGamesList

    }

    interface OnGameFilterListener{
        fun onGameFiltered(selectedPlatformList:List<String>)
    }

}