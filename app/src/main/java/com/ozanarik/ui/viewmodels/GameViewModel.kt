package com.ozanarik.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ozanarik.business.model.GameGiveAwayResponse
import com.ozanarik.business.model.GameGiveAwayResponseItem
import com.ozanarik.business.repositories.GameRepository
import com.ozanarik.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.platform.Platform
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val gameRepository: GameRepository) :ViewModel() {

    private val _allGameGiveAwayList:MutableStateFlow<Resource<GameGiveAwayResponse>> = MutableStateFlow(Resource.Loading())
    val allGameGiveAwayList:StateFlow<Resource<GameGiveAwayResponse>> = _allGameGiveAwayList


    private val _gameDetail:MutableStateFlow<Resource<GameGiveAwayResponseItem>> = MutableStateFlow(Resource.Loading())
    val gameDetail:StateFlow<Resource<GameGiveAwayResponseItem>> = _gameDetail


    fun getGameDetail(gameId:Int)=viewModelScope.launch {

        _gameDetail.value = Resource.Loading()

        try {
            val gameDetailResponse = withContext(Dispatchers.IO){
                gameRepository.getGameDetail(gameId)
            }

            _gameDetail.value = Resource.Success(gameDetailResponse)


        }catch (e:Exception){
            _gameDetail.value = Resource.Error(e.localizedMessage?:e.message!!)
        }catch (e:IOException){
            _gameDetail.value = Resource.Error(e.localizedMessage?:e.message!!)

        }

    }


    fun getGameGiveaways()=viewModelScope.launch {
        _allGameGiveAwayList.value = Resource.Loading()

        try {

            val gameGiveAwayResponse = withContext(Dispatchers.IO){
                gameRepository.getGameGiveaways()
            }
            if (gameGiveAwayResponse.isSuccessful){
                gameGiveAwayResponse.body()?.let { _allGameGiveAwayList.value = Resource.Success(it) }
            }


        }catch (e:Exception){
            _allGameGiveAwayList.value = Resource.Error(e.localizedMessage?:e.message!!)

        }catch (e:IOException){
            _allGameGiveAwayList.value = Resource.Error(e.localizedMessage?:e.message!!)
        }

    }

}