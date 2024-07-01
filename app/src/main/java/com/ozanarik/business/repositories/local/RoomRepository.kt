package com.ozanarik.business.repositories.local

import com.ozanarik.business.local.GamesDatabase
import com.ozanarik.business.model.GameGiveAwayResponseItem
import com.ozanarik.utilities.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RoomRepository @Inject constructor(private val gamesDatabase: GamesDatabase) {


    suspend fun wishlistGame(game:GameGiveAwayResponseItem)=gamesDatabase.gamesDao.wishlistGame(game)

    suspend fun deleteGame(game:GameGiveAwayResponseItem)=gamesDatabase.gamesDao.deleteGameFromWishlist(game)

    suspend fun searchGame(query:String?)=gamesDatabase.gamesDao.searchGame(query)

    suspend fun getWishlistedGames():Flow<Resource<List<GameGiveAwayResponseItem>>> = flow {

        emit(Resource.Loading())

        try {
            val games = gamesDatabase.gamesDao.getAllWishlistedGames()

            games.collect{wishlistedGames->
                emit(Resource.Success(wishlistedGames))
            }

        }catch (e:Exception){
            emit(Resource.Error(e.message?:e.localizedMessage!!))
        }
    }

    suspend fun getAllWishlistedGamesCount()=gamesDatabase.gamesDao.getWishlistedGamesCount()

}