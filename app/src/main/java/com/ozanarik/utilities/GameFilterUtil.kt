package com.ozanarik.utilities

import com.ozanarik.business.model.GameGiveAwayResponse
import com.ozanarik.business.model.GameGiveAwayResponseItem

object GameFilterUtil {


    fun filterPcGames(allGames:List<GameGiveAwayResponseItem>,platform:String):List<GameGiveAwayResponseItem>{

        return allGames.filter { it.type == platform }
    }

}