package com.ozanarik.business.repositories

import com.ozanarik.business.remote.GameApi
import javax.inject.Inject

class GameRepository @Inject constructor(private val gameApi: GameApi) {

    suspend fun getGameGiveaways()=gameApi.getGameGiveaways()

    suspend fun getGameGiveAwaysByPlatforms(platform:String) = gameApi.getGameGiveAwaysByPlatforms(platform)

    suspend fun getGameDetail(gameId:Int)=gameApi.getGameDetails(gameId)

}