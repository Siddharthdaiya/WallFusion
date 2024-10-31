package com.superelement.wallfusion.collection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.*
import com.superelement.wallfusion.Adapter.WallpaperAdapter
import com.superelement.wallfusion.R
import com.superelement.wallfusion.databinding.ActivityFlowerBinding

class FlowerActivity : AppCompatActivity() {
    lateinit var db: FirebaseDatabase

    private val imageUrls = mutableListOf<String>()
    private lateinit var database: DatabaseReference
    var imageList = mutableListOf<String>()
    lateinit var wallpaperAdapter: WallpaperAdapter
    lateinit var binding:ActivityFlowerBinding
    private lateinit var mytoolbar:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFlowerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.arrowBack.setOnClickListener {
          onBackPressed()
        }
        mytoolbar=findViewById<Toolbar>(R.id.mytoolbar)
        mytoolbar.title="          Collections"
        setSupportActionBar(mytoolbar)
        binding.flowerRv.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.flowerRv.layoutManager= StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        wallpaperAdapter = WallpaperAdapter(this,imageList)
        binding.flowerRv.adapter= wallpaperAdapter


        database = FirebaseDatabase.getInstance().reference
        database.child("Flowers").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                Log.d("TAG", "onDataChange: ")
                imageList.clear()
                for (snap in snapshot.children) {

                    var url = snap.getValue(String::class.java)

                    imageList.add(url.toString())
                }

                wallpaperAdapter.setItems(imageList)
                Log.d("TAG", "onDataChange: $imageList")

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }

    override fun onBackPressed() {
        finish()

    }

}