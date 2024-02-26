package com.ozanarik.ui.fragments.game_screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ozanarik.business.model.GameGiveAwayResponseItem
import com.ozanarik.gamegrab.R
import com.ozanarik.gamegrab.databinding.FragmentGamesBinding
import com.ozanarik.ui.adapters.GameAdapter
import com.ozanarik.ui.fragments.GameFilterDialogFragment
import com.ozanarik.ui.fragments.main_fragments.FragmentAllDirections
import com.ozanarik.ui.viewmodels.GameViewModel
import com.ozanarik.utilities.Extensions.Companion.showSnackbar
import com.ozanarik.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamesFragment : Fragment(),GameFilterDialogFragment.OnGameFilterListener {

    private lateinit var gameAdapter: GameAdapter
    private lateinit var binding: FragmentGamesBinding
    private lateinit var gameViewModel: GameViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = FragmentGamesBinding.inflate(inflater,container,false)
        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]


        handleRv()

        getGames()
        handleSearchFunctionality()

        handleFilterDialog()


        return binding.root
    }



    private fun handleFilterDialog(){

        binding.imgFilterGames.setOnClickListener {

            val gameFilterDialogFragment = GameFilterDialogFragment()
            gameFilterDialogFragment.setOnGameFilterListener(this)


            gameFilterDialogFragment.show(parentFragmentManager,gameFilterDialogFragment.tag)
        }


    }

    override fun onGameFiltered(selectedPlatformList: List<String>) {

        gameViewModel.getMultiPlatformSearch(selectedPlatformList)
        viewLifecycleOwner.lifecycleScope.launch {

            gameViewModel.multiPlatformSearch.collect{multiPlatform->
                when(multiPlatform){
                    is Resource.Success->{gameAdapter.asyncDifferList.submitList(multiPlatform.data)}
                    is Resource.Error->{
                        Log.e("asd",multiPlatform.message!!)
                    }
                    is Resource.Loading->{

                    }
                }
            }
        }
    }


    private fun getGames(){

        gameViewModel.getGameGiveaways()
        viewLifecycleOwner.lifecycleScope.launch {

            gameViewModel.allGameGiveAwayList.collect{allGames->
                when(allGames){
                    is Resource.Success->{
                        val allGamesList = allGames.data

                        val filteredGameList = allGamesList?.filter { it.type == "Game" }


                        gameAdapter.asyncDifferList.submitList(filteredGameList)

                    }
                    is Resource.Error->{

                    }
                    is Resource.Loading->{

                    }
                }
            }
        }
    }

    private fun getAllGames(searchQuery:String){
        gameViewModel.getGameGiveaways()
        viewLifecycleOwner.lifecycleScope.launch {
            gameViewModel.allGameGiveAwayList.collect{allGamesList->
                when(allGamesList){
                    is Resource.Success->{

                        binding.noDataLayout.root.visibility = View.GONE
                        val allGames = allGamesList.data
                        val filteredGamesList = if(searchQuery.isEmpty()){
                            allGames
                        }else{
                            allGames!!.filter { it.title.lowercase().contains(searchQuery)   }

                        }
                        gameAdapter.asyncDifferList.submitList(filteredGamesList)


                    }
                    is Resource.Error->{
                        binding.noDataLayout.root.visibility = View.VISIBLE
                    }
                    is Resource.Loading->{
                        binding.noDataLayout.root.visibility = View.VISIBLE

                    }
                }
            }
        }
    }


    private fun handleSearchFunctionality(){
        binding.etSearchGames.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(searchText: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(searchText: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(searchQuery: Editable?) {
                val searchText = searchQuery.toString().trim().lowercase()

                getAllGames(searchText)


            }
        })
    }

    private fun handleRv(){
        gameAdapter = GameAdapter(object : GameAdapter.OnItemClickListener {
            override fun onItemClick(currentGame: GameGiveAwayResponseItem) {
                val bundle = Bundle().apply {
                    putSerializable("gameData",currentGame)
                }



                findNavController().navigate(R.id.action_fragmentAll_to_gameDetailFragment,bundle)

            }
        }, object : GameAdapter.OnBookMarked {
            override fun onGameBookmarked(currentGame: GameGiveAwayResponseItem) {
                showSnackbar("${currentGame.title} added to wishlist!")
            }
        })

        binding.apply {
            rvGames.setHasFixedSize(true)
            rvGames.adapter = gameAdapter
            rvGames.layoutManager = LinearLayoutManager(requireContext())
        }

    }


}