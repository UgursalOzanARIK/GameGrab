package com.ozanarik.ui.adapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.ozanarik.business.model.GameGiveAwayResponseItem
import com.ozanarik.gamegrab.R
import com.ozanarik.gamegrab.databinding.GameGiveawayListItemBinding
import com.ozanarik.utilities.DataStoreManager
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GameAdapter (private val onItemClickListener: OnItemClickListener, private val onBookMarked: OnBookMarked) :RecyclerView.Adapter<GameAdapter.GameHolder>() {

    inner class GameHolder(val binding: GameGiveawayListItemBinding):RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object :DiffUtil.ItemCallback<GameGiveAwayResponseItem>(){
        override fun areItemsTheSame(
            oldItem: GameGiveAwayResponseItem, newItem: GameGiveAwayResponseItem): Boolean {
            return oldItem.id == newItem.id }

        override fun areContentsTheSame(
            oldItem: GameGiveAwayResponseItem, newItem: GameGiveAwayResponseItem): Boolean {
            return oldItem == newItem }
    }

    val asyncDifferList = AsyncListDiffer(this,diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        val layoutFrom = LayoutInflater.from(parent.context)
        val binding:GameGiveawayListItemBinding = GameGiveawayListItemBinding.inflate(layoutFrom,parent,false)
        return GameHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        val currentGame = asyncDifferList.currentList[position]

        holder.binding.apply {

            tvGameName.text = currentGame.title
            tvGamePrice.text = currentGame.worth
            tvGamePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            Picasso.get().load(currentGame.image).error(R.drawable.error).placeholder(R.drawable.placeholder).into(imageViewGamePhoto)
            tvGameDescription.text = currentGame.description


                val chip = Chip(holder.itemView.context)
                chip.text = currentGame.platforms
                chip.isClickable = false
                chip.isCheckable = false

                chipGroup.removeAllViews()

                chipGroup.addView(chip)


            tvRedeemBefore.text = if (currentGame.endDate.isEmpty()){
                ""
            }else{
                "Redeem Before: ${currentGame.endDate}"
            }
            tvGameType.text = currentGame.type

            imageViewBookmark.setOnClickListener {

                setBookmarkState(currentGame,imageViewBookmark)


                animateBookMarkImageView(currentGame,imageViewBookmark)

                onBookMarked.onGameBookmarked(currentGame)
            }

        }

        holder.binding.buttonGetItNowForFree.setOnClickListener { onItemClickListener.onItemClick(currentGame) }

    }

    private fun setBookmarkState(game:GameGiveAwayResponseItem,bookmarkImageView: ImageView){
        if (game.isGameBookmarked){
            bookmarkImageView.setImageResource(R.drawable.bookmarkadded)
        }else{
            bookmarkImageView.setImageResource(R.drawable.notbookmarkedyet)
        }
    }


    private fun animateBookMarkImageView(game: GameGiveAwayResponseItem,bookmarkImageView:ImageView){


        if (game.isGameBookmarked){

            val scaleX = ObjectAnimator.ofFloat(bookmarkImageView,"scaleX",1.0f,1.5f)
            scaleX.duration = 500L

            val scaleY = ObjectAnimator.ofFloat(bookmarkImageView,"scaleY",1.0f,1.5f)
            scaleY.duration =  500L

            val multiAnim = AnimatorSet().apply {
                duration = 500L
                playTogether(scaleX,scaleY)
            }
            multiAnim.start()
        }else{

            val scaleX = ObjectAnimator.ofFloat(bookmarkImageView,"scaleX",1.5f,1.0f)
            scaleX.duration = 500L

            val scaleY = ObjectAnimator.ofFloat(bookmarkImageView,"scaleY",1.5f,1.0f)
            scaleY.duration =  500L

            val multiAnim = AnimatorSet().apply {
                duration = 500L
                playTogether(scaleX,scaleY)
            }
            multiAnim.start()
        }

    }

    override fun getItemCount(): Int {
        return asyncDifferList.currentList.size
    }

    interface OnBookMarked{
        fun onGameBookmarked(currentGame: GameGiveAwayResponseItem)
    }

    interface OnItemClickListener{
        fun onItemClick(currentGame:GameGiveAwayResponseItem)
    }


}