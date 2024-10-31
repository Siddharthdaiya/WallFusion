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


class WallpaperAdapter(val requireContext: Context, var imageList: MutableList<String>) :RecyclerView.Adapter<WallpaperAdapter.homeviewholder> (){

    inner class homeviewholder(itemView:View):RecyclerView.ViewHolder(itemView){
        val imageview=itemView.findViewById<ImageView>(R.id.main_image)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeviewholder {
        return homeviewholder(
            LayoutInflater.from(requireContext).inflate(R.layout.home_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: homeviewholder, position: Int) {
            Glide
            .with(requireContext)
            .load(imageList[position])
            .centerCrop()
            .into(holder.imageview);
        holder.itemView.setOnClickListener {
            val intent= Intent(requireContext,FinalWallpaper::class.java)
            intent.putExtra("link",imageList[position])

            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount():Int{
        return imageList.size
    }

    fun setItems(imageList: MutableList<String>){
        this.imageList = imageList
        notifyDataSetChanged()
    }



}