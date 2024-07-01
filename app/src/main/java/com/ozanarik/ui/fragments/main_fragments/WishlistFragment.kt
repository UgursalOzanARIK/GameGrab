package com.ozanarik.ui.fragments.main_fragments

import android.os.Bundle
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
import com.ozanarik.gamegrab.databinding.FragmentWishlistBinding
import com.ozanarik.ui.adapters.GameAdapter
import com.ozanarik.ui.viewmodels.GameViewModel
import com.ozanarik.utilities.Extensions.Companion.showSnackbar
import com.ozanarik.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint

class WishlistFragment : Fragment() {
    private lateinit var gameAdapter: GameAdapter
    private lateinit var gameViewModel: GameViewModel
    private lateinit var binding: FragmentWishlistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment


        binding = FragmentWishlistBinding.inflate(inflater,container,false)

        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]


        handleRv()

        getAllWishlistedGames()




        return binding.root
    }






    private fun getAllWishlistedGames(){

        gameViewModel.getAllWishlistedGames()

        viewLifecycleOwner.lifecycleScope.launch {

            gameViewModel.getAllWishlistedGames.collect{wishlistedGameResponse->
                when(wishlistedGameResponse){
                    is Resource.Success->{gameAdapter.asyncDifferList.submitList(wishlistedGameResponse.data)}
                    is Resource.Loading->{
                        binding.noWishlistedGames.root.visibility = View.VISIBLE
                        showSnackbar("Fetching games...")
                    }
                    is Resource.Error->{
                        binding.noWishlistedGames.root.visibility = View.VISIBLE
                        showSnackbar(wishlistedGameResponse.message!!)
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

                findNavController().navigate(R.id.action_wishlistFragment_to_gameDetailFragment,bundle)

            }
        },object : GameAdapter.OnBookMarked {
            override fun onGameBookmarked(currentGame: GameGiveAwayResponseItem) {
                gameViewModel.deleteGame(currentGame)
            }
        } )


        binding.apply {

            rvWishlist.setHasFixedSize(true)
            rvWishlist.layoutManager = LinearLayoutManager(requireContext())
            rvWishlist.adapter = gameAdapter
        }

    }


}