package com.ozanarik.business.remote

import com.ozanarik.business.model.GameGiveAwayResponse
import com.ozanarik.business.model.GameGiveAwayResponseItem
import okhttp3.internal.platform.Platform
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GameApi {

    @GET("api/giveaways")
    suspend fun getGameGiveaways():Response<GameGiveAwayResponse>

    @GET("api/giveaways")
    suspend fun getGameGiveAwaysByPlatforms(
        @Query("platform")platform:String
    ):Response<GameGiveAwayResponse>


    @GET("api/giveaway")
    suspend fun getGameDetails(
        @Query("id")id:Int
    ):GameGiveAwayResponseItem

    @GET("api/filter?")
    suspend fun getMultiPlatforms(
        @Query("platform")platform:String?=null
    ):Response<GameGiveAwayResponse>


}