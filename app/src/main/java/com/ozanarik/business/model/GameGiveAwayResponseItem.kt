package com.ozanarik.business.model


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable


@Entity(tableName = "game_Table", indices = [Index(value = ["id"], unique = true)])
data class GameGiveAwayResponseItem(
    @PrimaryKey(autoGenerate = true)
    val gameId:Int = 0,

    var isGameBookmarked:Boolean = false,
    @Expose
    val description: String = "", // Say goodbye to boredom and hello to epic explosive physics with FlatOut – and guess what? You can grab it for free via GOG! Prepare yourself for high-speed racing with epic demolition derbies that'll have you wrecking opponents with glee, and stunts so wild they'll make everyone blush! You'll be launching yourself through the windshield in style!
    @SerializedName("end_date")
    @Expose
    val endDate: String = "", // 2024-02-17 23:59:00
    @SerializedName("gamerpower_url")
    @Expose
    val gamerpowerUrl: String = "", // https://www.gamerpower.com/flatout-gog-giveaway
    @Expose
    val id: Int = 0, // 2774
    @Expose
    val image: String = "", // https://www.gamerpower.com/offers/1b/65cccf51edd2e.jpg
    @Expose
    val instructions: String = "", // 1. Click the "Go to giveaway" green button and you will be redirected to the GOG home page2. Scroll down until you find the game banner.3. Click the "Get it Free" button and login into your GOG account to unlock the game.
    @SerializedName("open_giveaway")
    @Expose
    val openGiveaway: String = "", // https://www.gamerpower.com/open/flatout-gog-giveaway
    @SerializedName("open_giveaway_url")
    @Expose
    val openGiveawayUrl: String = "", // https://www.gamerpower.com/open/flatout-gog-giveaway
    @Expose
    val platforms: String = "", // PC, GOG, DRM-Free
    @SerializedName("published_date")
    @Expose
    val publishedDate: String = "", // 2024-02-14 09:33:54
    @Expose
    val status: String = "", // Active
    @Expose
    val thumbnail: String = "", // https://www.gamerpower.com/offers/1/65cccf51edd2e.jpg
    @Expose
    val title: String = "", // FlatOut (GOG) Giveaway
    @Expose
    val type: String = "", // Game
    @Expose
    val users: Int = 0, // 3350
    @Expose
    val worth: String = "" // $7.49
):Serializable