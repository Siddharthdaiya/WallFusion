package com.superelement.wallfusion.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.superelement.wallfusion.Adapter.WallpaperAdapter
import com.superelement.wallfusion.databinding.FragmentAnimationBinding
import com.superelement.wallfusion.model.ImagesAll

class AnimationFragment : Fragment() {
    lateinit var wallpaperAdapter: WallpaperAdapter
    lateinit var db: FirebaseDatabase
    private lateinit var database: DatabaseReference
    lateinit var binding: FragmentAnimationBinding
    var imageList = mutableListOf<String>()
    var imageDataList : ArrayList<ImagesAll>? = null
    var subFolderList : ArrayList<String>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAnimationBinding.inflate(layoutInflater,container,false)
        //    binding.homeRv.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        binding.animatedRv.layoutManager= StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        wallpaperAdapter = WallpaperAdapter(requireContext(),imageList)
        binding.animatedRv.adapter= wallpaperAdapter

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        database.child("Animated").addValueEventListener(object : ValueEventListener {
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

            }

        })

    }
}