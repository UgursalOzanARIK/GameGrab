package com.ozanarik.ui.fragments.game_screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ozanarik.business.model.GameGiveAwayResponseItem
import com.ozanarik.gamegrab.R
import com.ozanarik.gamegrab.databinding.FragmentAllGamesListBinding
import com.ozanarik.ui.adapters.GameAdapter
import com.ozanarik.ui.fragments.main_fragments.FragmentAllDirections
import com.ozanarik.ui.viewmodels.GameViewModel
import com.ozanarik.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.InputStreamReader

@AndroidEntryPoint
class AllGamesListFragment : Fragment() {
    private lateinit var binding: FragmentAllGamesListBinding
    private lateinit var gameAdapter: GameAdapter
    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAllGamesListBinding.inflate(inflater,container,false)
        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]


        handleRv()

        getAllGames()



        return binding.root
    }




    private fun getAllGames(){
        gameViewModel.getGameGiveaways()
        viewLifecycleOwner.lifecycleScope.launch {
            gameViewModel.allGameGiveAwayList.collect{allGames->
                when(allGames){
                    is Resource.Success->{
                        val allGamesList = allGames.data

                        gameAdapter.asyncDifferList.submitList(allGamesList)

                    }
                    is Resource.Error->{

                    }
                    is Resource.Loading->{

                    }
                }
            }
        }
    }




    private fun handleRv(){

        gameAdapter = GameAdapter(object : GameAdapter.OnItemClickListener {
            override fun onItemClick(currentGame: GameGiveAwayResponseItem) {
                val bundle = Bundle().apply {
                    putSerializable("gameData",currentGame)
                }



                findNavController().navigate(R.id.action_fragmentAll_to_gameDetailFragment,bundle)

            }
        })

        binding.apply {
            rvGames.setHasFixedSize(true)
            rvGames.layoutManager = LinearLayoutManager(requireContext())
            rvGames.adapter = gameAdapter
        }

    }

}