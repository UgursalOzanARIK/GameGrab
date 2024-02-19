package com.ozanarik.ui.fragments.game_screens.game_detail_fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ozanarik.business.model.GameGiveAwayResponse
import com.ozanarik.gamegrab.R
import com.ozanarik.gamegrab.databinding.FragmentGameDetailBinding
import com.ozanarik.ui.viewmodels.GameViewModel
import com.ozanarik.utilities.Resource
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameDetailFragment : Fragment() {

    private lateinit var binding: FragmentGameDetailBinding
    private lateinit var gameViewModel: GameViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]
        binding = FragmentGameDetailBinding.inflate(inflater,container,false)




        getGameDetails()

        return binding.root
    }


    private fun getGameDetails(){


        val gameDetailArgs:GameDetailFragmentArgs by navArgs()
        val detail = gameDetailArgs.gameData


        gameViewModel.getGameDetail(detail.id)
        viewLifecycleOwner.lifecycleScope.launch {
            gameViewModel.gameDetail.collect{gameDetailResponse->

                when(gameDetailResponse){
                    is Resource.Success->{

                        val gameDetails = gameDetailResponse.data ?: emptyList()

                        binding.apply {

                            if (gameDetails!=null){

                                val gameDetail = gameDetails.get(0)

                                tvGameGiveawayUrl.text = gameDetail.openGiveawayUrl
                                tvGameTypeDetail.text = "Type : ${gameDetail.type}"
                                Picasso.get().load(gameDetail.image).placeholder(R.drawable.placeholder).error(R.drawable.error).into(imgGamePhoto)
                                tvRedeemDetail.text = "Redeem before ${gameDetail.endDate}"
                                tvGameDescriptionDetail.text = gameDetail.description
                                tvPublishedDateDetail.text = gameDetail.publishedDate
                                cvGameUrl.setOnClickListener {

                                    shareLink(gameDetail.openGiveawayUrl)
                                }

                                handleChipGroup(gameDetail.platforms,chipGrpPlatforms)

                                handleButtonGetItIntent(gameDetail.openGiveawayUrl,buttonGetIt)

                            }

                        }

                    }
                    is Resource.Error->{
                        Log.e("detail message",gameDetailResponse.message!!)}
                    is Resource.Loading->{}
                }

            }
        }



    }




    private fun shareLink(url:String){

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,url)
        startActivity(Intent.createChooser(intent,"Share link via"))

    }


    private fun handleChipGroup(platform:String,chipGroup: ChipGroup){


        val chip = Chip(requireContext())
        chip.text = platform
        chip.isClickable = false
        chip.isCheckable = false

        chipGroup.removeAllViews()
        chipGroup.addView(chip)

    }

    private fun handleButtonGetItIntent(url:String,buttonGetIt:Button){


        buttonGetIt.setOnClickListener {

            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW,uri)

            startActivity(intent)

        }


    }






}