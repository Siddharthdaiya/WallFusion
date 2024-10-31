package com.superelement.wallfusion

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.internal.NavigationSubMenu
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.superelement.wallfusion.Fragment.*
import com.superelement.wallfusion.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() ,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var networkConnection: NetworkConnection
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var builder: AlertDialog
    private lateinit var logout: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        networkConnection = NetworkConnection(this)
        networkConnection.registerNetworkCallback()
        var viewPager = findViewById(R.id.viewPager) as ViewPager
        var tablayout = findViewById(R.id.tablayout) as TabLayout

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(HomeFragment(), "Home")
        fragmentAdapter.addFragment(RecentFragment(), "Recent")
        fragmentAdapter.addFragment(CollectionFragment(), "Collection")
        fragmentAdapter.addFragment(AnimationFragment(), "Animated")

        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)

        @SuppressLint("SuspiciousIndentation")
        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> startActivity(Intent(this,MainActivity::class.java))
            R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))

            R.id.nav_favourite -> startActivity(Intent(this, FavouriteActivty::class.java))

            R.id.nav_history -> startActivity(Intent(this, HistoryActivity::class.java))

            R.id.nav_terms_privacy -> startActivity(Intent(this, Terms_and_Privacy::class.java))

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment() {
        val myFragment=HomeFragment()
        val fragment:Fragment?=supportFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
        if (fragment !is HomeFragment){
            supportFragmentManager.beginTransaction()
                .add(R.id.linearFragment_container,myFragment,HomeFragment::class.java.simpleName)
                .commit()
        }
    }
    override fun onDestroy() {
        // Unregister the NetworkConnection when the activity is destroyed
        networkConnection.unregisterNetworkCallback()
        super.onDestroy()
    }
    private fun handleNetworkState(isConnected: Boolean) {
        if (isConnected) {

           //setContentonView(R.layout.activity_main)
        } else {
            // Perform actions when the device is offline
            // For example, show a message or disable certain features
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            setContentView(R.layout.network_layout_error)
        }
    }
    override fun onResume() {
        super.onResume()
        // Get the current network state and call handleNetworkState
        val isConnected = NetworkUtils.isNetworkAvailable(this)
        handleNetworkState(isConnected)

    }
    fun onNetworkAvailable() {
        val isConnected = NetworkUtils.isNetworkAvailable(this)
        handleNetworkState(isConnected)
    }

    fun onNetworkLost() {
        // Handle network lost event if needed (optional)
    }

    //
    override fun onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            onBackPressedDispatcher.onBackPressed()
//        }
        showCustomDialogBox()
//
//          val builder=AlertDialog.Builder(this)
//
//        builder.setTitle("Alert ! ")
//            .setMessage("Do you want to exit?")
//            .setCancelable(true)
//            .setPositiveButton("Yes"){dialogInterface,it->
//                finishAffinity()
//            }
//            .setNegativeButton("No"){dialogInterface,it->
//                dialogInterface.cancel()
//            }
//            .show()


    }

    private fun showCustomDialogBox() {
        val dialog=Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dailog_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes: Button =dialog.findViewById(R.id.btn_yes)
        val btnNo: Button =dialog.findViewById(R.id.btn_No)

      //  tvmessage.text=message
        btnYes.setOnClickListener {
            finishAffinity()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()


    }
}