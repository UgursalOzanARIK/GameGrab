package com.ozanarik.business.repositories

import com.ozanarik.business.model.GameGiveAwayResponse
import com.ozanarik.business.model.GameGiveAwayResponseItem
import com.ozanarik.business.remote.GameApi
import javax.inject.Inject

class GameRepository @Inject constructor(private val gameApi: GameApi) {

    suspend fun getGameGiveaways()=gameApi.getGameGiveaways()


    suspend fun getGameDetail(gameId:Int):GameGiveAwayResponseItem = gameApi.getGameDetails(gameId)

    suspend fun getMultiplePlatforms(platform:String)=gameApi.getMultiPlatforms(platform)

}