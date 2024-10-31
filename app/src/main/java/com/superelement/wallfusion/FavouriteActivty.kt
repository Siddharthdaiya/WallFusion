package com.superelement.wallfusion

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.superelement.wallfusion.Adapter.FavoriteImageAdapter
import com.superelement.wallfusion.Adapter.WallpaperAdapter
import com.superelement.wallfusion.databinding.ActivityFavouriteActivtyBinding

class FavouriteActivty : AppCompatActivity() {
    lateinit var binding:ActivityFavouriteActivtyBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var database: FirebaseDatabase
    private val favoriteImageUrls: MutableList<String> = mutableListOf()
    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var favoriteImageAdapter: FavoriteImageAdapter
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFavouriteActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mytoolbar = findViewById<Toolbar>(R.id.mytoolbar)
        mytoolbar.title="          Favourite"
        setSupportActionBar(mytoolbar)


        //binding.favouirteRv.layoutManager=StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL,)
        //favoriteImageAdapter=FavoriteImageAdapter(this, favoriteImageUrls)
        //binding.favouirteRv.adapter= favoriteImageAdapter


        binding.arrowBack.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

     //   binding.favouirteRv.adapter= favoriteImageAdapter
        // Initialize Firebase references
        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser!!

//        favoriteRecyclerView = findViewById(R.id.favouirte_rv)
//        favoriteRecyclerView.layoutManager = LinearLayoutManager(this)
//        // Create the adapter and set it to the RecyclerView
//        favoriteImageAdapter = FavoriteImageAdapter(this, favoriteImageUrls)
//        favoriteRecyclerView.adapter = favoriteImageAdapter

        //For Grid Layout......
        binding.favouirteRv.layoutManager=StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        favoriteImageAdapter = FavoriteImageAdapter(this, favoriteImageUrls)
        binding.favouirteRv.adapter=favoriteImageAdapter

        // Retrieve the favorite image URLs from Firebase
        retrieveFavoriteImageUrlsFromFirebase()

    }

    private fun retrieveFavoriteImageUrlsFromFirebase() {
        val uid = user.uid
        if (uid != null) {
            val favoriteRef = database.reference.child("users").child(uid).child("favorites")
            favoriteRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    favoriteImageUrls.clear()
                    for (dataSnapshot in snapshot.children) {
                        val imageUrl = dataSnapshot.getValue(String::class.java)
                        if (imageUrl != null) {
                            favoriteImageUrls.add(imageUrl)
                        }
                    }
                    favoriteImageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error if needed
                }
            })
        }
    }

}