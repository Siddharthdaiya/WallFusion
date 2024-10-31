package com.superelement.wallfusion

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.superelement.wallfusion.Fragment.HomeFragment
import com.superelement.wallfusion.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var firebaseAuth:FirebaseAuth
    private var progressBar: ProgressBar? = null
    private lateinit var builder: AlertDialog

    private var i = 0
    private val handler = Handler()
   private lateinit var binding:ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        val userid = firebaseAuth.currentUser?.uid


        // It is for the user id in Settings
        saveUserIdToSharedPreferences(userid.toString())


        binding.forgetPassword.setOnClickListener {
            val builder=AlertDialog.Builder(this)
            val view =layoutInflater.inflate(R.layout.dialog_forget_password,null)
            val userEmail=view.findViewById<EditText>(R.id.et_forgot)

            builder.setView(view)
            val dialog=builder.create()
            view.findViewById<Button>(R.id.btn_reset).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()



            }
            view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window!=null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()




        }


        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        progressBar = findViewById<ProgressBar>(R.id.progress_bar) as ProgressBar


        binding.btnRegister.setOnClickListener{
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{
                    if (it.isSuccessful) {
                        val mProgressDialog = ProgressDialog(this)
                        mProgressDialog.setTitle("Wait...")
                        //.setMessage("This is MESSAGE")
                        mProgressDialog.show()

                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show()

                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun saveUserIdToSharedPreferences(userId: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.putString("user_id_key", userId)
        editor.apply()

    }

    private fun compareEmail(email:EditText){

        if (email.text.toString().isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        firebaseAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener {task->
            if(task.isSuccessful){
                Toast.makeText(this,"Check Your Email",Toast.LENGTH_SHORT).show()
            }

        }

    }


    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        showCustomDialogBox()
    }

    private fun showCustomDialogBox() {
        val dialog= Dialog(this)
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

