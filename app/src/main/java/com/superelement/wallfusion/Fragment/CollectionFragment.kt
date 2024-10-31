package com.superelement.wallfusion.Fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.superelement.wallfusion.R
import com.superelement.wallfusion.collection.FlowerActivity
import com.superelement.wallfusion.databinding.FragmentCollectionBinding

class CollectionFragment : Fragment() {
    lateinit var binding: FragmentCollectionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCollectionBinding.inflate(layoutInflater,container,false)
        binding.category1Flowers.setOnClickListener {
           val intent=Intent(requireContext(),FlowerActivity::class.java)
            startActivity(intent)
        }

        return binding.root

    }


}