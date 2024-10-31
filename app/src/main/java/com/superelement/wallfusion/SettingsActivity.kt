package com.superelement.wallfusion

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.superelement.wallfusion.Fragment.HomeFragment
import com.superelement.wallfusion.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var mytoolbar:Toolbar

    private lateinit var textViewUserId: TextView
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textViewUserId = findViewById(R.id.textViewUserId)

        // Fetch and display the user ID
        val userId = getUserId()
        textViewUserId.text = "User ID: $userId"


        binding.arrowBack.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        var logout = findViewById<Button>(R.id.btn_logout)
        logout.setOnClickListener{
            showCustomDialogBox()

        }
        mytoolbar=findViewById<Toolbar>(R.id.mytoolbar)
        mytoolbar.title="          Settings"
        setSupportActionBar(mytoolbar)
    }

    private fun getUserId(): String {
        // Retrieve the user ID from shared preferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getString("user_id_key", "No user ID found") ?: "No user ID found"

    }

    override fun onBackPressed() {

    }

    private fun showCustomDialogBox() {
        val dialog= Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.logout_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes: Button =dialog.findViewById(R.id.btn_yes)
        val btnNo: Button =dialog.findViewById(R.id.btn_No)

        //  tvmessage.text=message
        btnYes.setOnClickListener {
           // finishAffinity()
            Firebase.auth.signOut()
            val intent= Intent(this,SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("KEY_RESTART_INTENT", "nextIntent")
            startActivity(intent)
            finishAffinity()
            Toast.makeText(this,"LogoutSuccessFully",Toast.LENGTH_SHORT).show()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}