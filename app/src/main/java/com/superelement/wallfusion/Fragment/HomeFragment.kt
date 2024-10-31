package com.superelement.wallfusion.Fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.*
import com.superelement.wallfusion.databinding.FragmentHomeBinding
import com.superelement.wallfusion.Adapter.WallpaperAdapter as Wallpaper
class HomeFragment : Fragment() {
    lateinit var db:FirebaseDatabase
    private lateinit var database:DatabaseReference
    lateinit var binding:FragmentHomeBinding
    var imageList = mutableListOf<String>()
    lateinit var recyclerView:RecyclerView
    lateinit var wallpaperAdapter: Wallpaper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            binding=FragmentHomeBinding.inflate(layoutInflater,container,false)

    //    binding.homeRv.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        binding.homeRv.layoutManager=StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        wallpaperAdapter = Wallpaper(requireContext(), imageList)
        binding.homeRv.adapter= wallpaperAdapter
        //        val dialogBuilder = AlertDialog.Builder(activity!!)
//        dialogBuilder.setMessage("DO you want to exit ?")
//            // if the dialog is cancelable
//            .setCancelable(false)
//            .setPositiveButton("Ok", DialogInterface.OnClickListener {
//                    dialog, id ->
//                dialog.dismiss()
//
//            })
//            .setNegativeButton("No"){dialogInterface,it->
//                dialogInterface.cancel()
//            }
//
//        val alert = dialogBuilder.create()
//        alert.setTitle("Test")
//        alert.show()

        return binding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        database.child("Wallpapers").addValueEventListener(object : ValueEventListener {
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