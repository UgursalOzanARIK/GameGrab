package com.ozanarik.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ozanarik.business.model.GameGiveAwayResponse
import com.ozanarik.business.model.GameGiveAwayResponseItem
import com.ozanarik.business.repositories.GameRepository
import com.ozanarik.business.repositories.local.RoomRepository
import com.ozanarik.utilities.DataStoreManager
import com.ozanarik.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(application:Application,private val gameRepository: GameRepository, private val roomRepository: RoomRepository) :AndroidViewModel(application) {

    private val _allGameGiveAwayList:MutableStateFlow<Resource<GameGiveAwayResponse>> = MutableStateFlow(Resource.Loading())
    val allGameGiveAwayList:StateFlow<Resource<GameGiveAwayResponse>> = _allGameGiveAwayList


    private val _gameDetail:MutableStateFlow<Resource<GameGiveAwayResponseItem>> = MutableStateFlow(Resource.Loading())
    val gameDetail:StateFlow<Resource<GameGiveAwayResponseItem>> = _gameDetail

    private val _multiPlatformSearch:MutableStateFlow<Resource<List<GameGiveAwayResponseItem>>> = MutableStateFlow(Resource.Loading())
    val multiPlatformSearch:StateFlow<Resource<List<GameGiveAwayResponseItem>>> = _multiPlatformSearch


    private val _getAllWishlistedGames:MutableStateFlow<Resource<List<GameGiveAwayResponseItem>>> = MutableStateFlow(Resource.Loading())
    val getAllWishlistedGames:StateFlow<Resource<List<GameGiveAwayResponseItem>>> =_getAllWishlistedGames

    private val _isGameBookmarked:MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isGameBookmarked:StateFlow<Boolean> = _isGameBookmarked

    private val dataStore = DataStoreManager(application)

    fun getBookmarked()=viewModelScope.launch {

        dataStore.getBookmarked().collect{isBookmarked->

            _isGameBookmarked.value = isBookmarked
        }
    }

    fun setBookmarked(isBookMarked:Boolean)=viewModelScope.launch {
        dataStore.setBookmarked(isBookMarked)
    }


    fun getMultiPlatformSearch(platform:List<String>)=viewModelScope.launch {
        _multiPlatformSearch.value = Resource.Loading()

        try {

            val platformQuery = platform.joinToString(".")
            val multiSearch = withContext(Dispatchers.IO){
                gameRepository.getMultiplePlatforms(platformQuery)
            }

            if (multiSearch.isSuccessful){
                val gamesList = multiSearch.body()?: emptyList()

                _multiPlatformSearch.value = Resource.Success(gamesList)
            }


        }catch (e:Exception){
            _multiPlatformSearch.value = Resource.Error(e.message?:e.localizedMessage!!)

        }catch (e:IOException){
            _multiPlatformSearch.value = Resource.Error(e.message?:e.localizedMessage!!)
        }

    }


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



    fun wishlistGame(game:GameGiveAwayResponseItem)=viewModelScope.launch { roomRepository.wishlistGame(game) }
    fun deleteGame(game:GameGiveAwayResponseItem)=viewModelScope.launch { roomRepository.deleteGame(game) }
    fun getAllWishlistedGames()=viewModelScope.launch {
        roomRepository.getWishlistedGames().collect{ wishlistedGamesList->
        when(wishlistedGamesList){
            is Resource.Success->{_getAllWishlistedGames.value = Resource.Success(wishlistedGamesList.data!!)}
            is Resource.Loading->{_getAllWishlistedGames.value = Resource.Loading()}
            is Resource.Error->{_getAllWishlistedGames.value = Resource.Error(wishlistedGamesList.message!!)}
        }

    } }

    fun searchGames(query:String?)=viewModelScope.launch { roomRepository.searchGame(query) }

    fun getWishlistedGamesFromDatabaseCount()=viewModelScope.launch { roomRepository.getAllWishlistedGamesCount() }

}