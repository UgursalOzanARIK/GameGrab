package com.ozanarik.business.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ozanarik.business.model.GameGiveAwayResponseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface GamesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun wishlistGame(game:GameGiveAwayResponseItem)

    @Query("SELECT * FROM game_Table")
     fun getAllWishlistedGames():Flow<List<GameGiveAwayResponseItem>>


    @Delete
     fun deleteGameFromWishlist(game:GameGiveAwayResponseItem)

    @Query("SELECT * FROM game_Table WHERE title LIKE :query")
     fun searchGame(query:String?):Flow<List<GameGiveAwayResponseItem>>




}