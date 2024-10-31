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
import com.superelement.wallfusion.Adapter.WallpaperAdapter
import com.superelement.wallfusion.databinding.FragmentRecent2Binding

class RecentFragment : Fragment() {
    lateinit var db: FirebaseDatabase
    private lateinit var database: DatabaseReference
    lateinit var binding: FragmentRecent2Binding
    var imageList = mutableListOf<String>()

    lateinit var wallpaperAdapter: WallpaperAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRecent2Binding.inflate(layoutInflater,container,false)
        binding.recentRv.layoutManager= StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        wallpaperAdapter = WallpaperAdapter(requireContext(),imageList)
        binding.recentRv.adapter= wallpaperAdapter


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().reference

        database.child("Recent").addValueEventListener(object : ValueEventListener {

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
