package com.superelement.wallfusion

import android.R.attr.bitmap
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.superelement.wallfusion.databinding.ActivityFinalWallpaperBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.net.URL
import java.util.*


class FinalWallpaper : AppCompatActivity() {
    lateinit var binding: ActivityFinalWallpaperBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var imageUrl: String? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: FirebaseUser
    var imageList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalWallpaperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        val url = intent.getStringExtra("link")

        binding.finalWallpaper.setOnClickListener {

        }

        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser!!


        var imageUrl = url
        val imageView = findViewById<ImageView>(R.id.finalWallpaper)

        Glide.with(this).load(imageUrl).into(imageView)



        binding.favouriteChbox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                addToFavorites(imageUrl)
//                val userid = firebaseAuth.currentUser?.uid
//                val imagelist = binding.finalWallpaper.toString()
//                database = FirebaseDatabase.getInstance().getReference("Favourite")
//
//                var array = mutableListOf<String>(url!!,url, url, url)
//
//                var map = mutableMapOf<String, Any>("images" to array)
//
//                database.child(userid!!).updateChildren(map)


            } else {
              //  Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show()
                removeFromFavorites()
            }

        }


        val urlImage = URL(url)
        binding.finalWallpaper
        Glide
            .with(this)
            .load(url)
            .centerCrop()
            .into(binding.finalWallpaper);


        binding.btnDownload.setOnClickListener {
            //download Wallpaper
            val result: kotlinx.coroutines.Deferred<Bitmap?> = GlobalScope.async {
                urlImage.toBitmap()

            }
            GlobalScope.launch(Dispatchers.Main) {
                saveImage(result.await())
            }


        }
        binding.btnUpload.setOnClickListener {
            //set Wallpaer
            val result: kotlinx.coroutines.Deferred<Bitmap?> = GlobalScope.async {
                urlImage.toBitmap()

            }
            GlobalScope.launch(Dispatchers.Main) {
                val wallpapermanager = WallpaperManager.getInstance(applicationContext)
                wallpapermanager.setBitmap(result.await())
            }
        }
        imageUrl=url


    }

    private fun removeFromFavorites() {
        val uid = user.uid
        if (uid != null && imageUrl != null) {
            // Reference to the 'users' node
            val usersRef = databaseReference.child("users")

            // Check if the 'users' node exists
            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(uid)) {
                        // If the 'users' node exists, remove the image from favorites
                        val favoritesRef = usersRef.child(uid).child("favorites")
                        favoritesRef.orderByValue().equalTo(imageUrl).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (dataSnapshot in snapshot.children) {
                                    dataSnapshot.ref.removeValue()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                                binding.favouriteChbox.isChecked = true
                            }
                        })
                    } else {
                        // The 'users' node doesn't exist, so the image is not in favorites, nothing to remove
                        // You can handle this case based on your app's requirements
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    binding.favouriteChbox.isChecked = true
                }
            })
        }
    }



    private fun checkIfImageIsFavorite() {
        val uid = user.uid
        if (uid != null ) {
            val favoritesRef = databaseReference.child("users").child(uid).child("favorites")
            favoritesRef.orderByValue().equalTo(imageUrl).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isFavorite = snapshot.exists()
                    binding.favouriteChbox.isChecked = isFavorite
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }



    private fun addToFavorites(imageUrl: String?) {
        val uid = user.uid
        if (uid != null) {
            val favoriteRef = database.reference.child("users").child(uid).child("favorites")
            val imageId = favoriteRef.push().key // Generate a unique key for the new image entry

            if (imageId != null) {
                favoriteRef.child(imageId).setValue(imageUrl)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Image added to favorites successfully
                            saveImageUrlToSharedPreferences(imageUrl)
                            // Optionally, you can start the next activity here
                            // startActivity(Intent(this, FavoriteActivity::class.java))
                        } else {
                            // Handle error
                        }
                    }
            }
        }

    }

    private fun saveImageUrlToSharedPreferences(imageUrl: String?) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("favoriteImageUrl", imageUrl)
        editor.apply()
    }


    fun URL.toBitmap(): Bitmap? {
        return try {
            BitmapFactory.decodeStream(openStream())
        } catch (e: IOException) {
            null
        }
    }

    private fun saveImage(image: Bitmap?) {
        val random1 = Random().nextInt(520985)
        val random2 = Random().nextInt(952663)

        val name = "WALLFUSION-${random1 + random2}"

        val data: OutputStream
        try {
            val resolver = contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$name.jpg")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + File.separator + "WallFusion Wallpapers"
            )
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            data = resolver.openOutputStream(Objects.requireNonNull(imageUri)!!)!!
            image?.compress(Bitmap.CompressFormat.JPEG, 100, data)
            Objects.requireNonNull<OutputStream>(data)
            Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Image Not  Saved", Toast.LENGTH_SHORT).show()
        }


    }
}
