package com.ozanarik.business.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ozanarik.business.model.GameGiveAwayResponseItem

@Database(entities = [GameGiveAwayResponseItem::class], version = 1)
abstract class GamesDatabase:RoomDatabase() {

    abstract val gamesDao:GamesDao

}