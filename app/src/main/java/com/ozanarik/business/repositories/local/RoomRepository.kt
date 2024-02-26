package com.ozanarik.business.repositories.local

import com.ozanarik.business.local.GamesDatabase
import com.ozanarik.business.model.GameGiveAwayResponseItem
import javax.inject.Inject

class RoomRepository @Inject constructor(private val gamesDatabase: GamesDatabase) {


    suspend fun wishlistGame(game:GameGiveAwayResponseItem)=gamesDatabase.gamesDao.wishlistGame(game)

    suspend fun deleteGame(game:GameGiveAwayResponseItem)=gamesDatabase.gamesDao.deleteGameFromWishlist(game)

    suspend fun searchGame(query:String?)=gamesDatabase.gamesDao.searchGame(query)

    suspend fun getAllWishlistedGames()=gamesDatabase.gamesDao.getAllWishlistedGames()

}