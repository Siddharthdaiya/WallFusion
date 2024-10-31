package com.superelement.wallfusion.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.superelement.wallfusion.FinalWallpaper
import com.superelement.wallfusion.R

class FavoriteImageAdapter(private val context: Context, private val favoriteImageUrls: List<String>) :
    RecyclerView.Adapter<FavoriteImageAdapter.FavoriteImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_layout, parent, false)
        return FavoriteImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteImageViewHolder, position: Int) {
        Glide
            .with(context)
            .load(favoriteImageUrls[position])
            .centerCrop()
            .into(holder.favoriteImageView);
        holder.itemView.setOnClickListener {
            val intent= Intent(context, FinalWallpaper::class.java)
            intent.putExtra("link",favoriteImageUrls[position])

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return favoriteImageUrls.size
    }
    inner class FavoriteImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favoriteImageView: ImageView = itemView.findViewById(R.id.main_image)



    }
}




