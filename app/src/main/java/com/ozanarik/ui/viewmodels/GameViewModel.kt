package com.ozanarik.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ozanarik.business.model.GameGiveAwayResponse
import com.ozanarik.business.repositories.GameRepository
import com.ozanarik.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.platform.Platform
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val gameRepository: GameRepository) :ViewModel() {

    private val _allGameGiveAwayList:MutableStateFlow<Resource<GameGiveAwayResponse>> = MutableStateFlow(Resource.Loading())
    val allGameGiveAwayList:StateFlow<Resource<GameGiveAwayResponse>> = _allGameGiveAwayList


    private val _gameDetail:MutableStateFlow<Resource<GameGiveAwayResponse>> = MutableStateFlow(Resource.Loading())
    val gameDetail:StateFlow<Resource<GameGiveAwayResponse>> = _gameDetail


    fun getGameDetail(gameId:Int)=viewModelScope.launch {

        _gameDetail.value = Resource.Loading()

        try {
            val gameDetailResponse = withContext(Dispatchers.IO){
                gameRepository.getGameDetail(gameId)
            }

            if (gameDetailResponse.isSuccessful){
                _gameDetail.value = Resource.Success(gameDetailResponse.body()!!)
            }

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