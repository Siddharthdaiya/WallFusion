package com.superelement.wallfusion

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.SigningInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.superelement.wallfusion.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseAuth:FirebaseAuth

    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val mProgressDialog = ProgressDialog(this)
            mProgressDialog.setTitle("Wait...")
            //.setMessage("This is MESSAGE")
            mProgressDialog.show()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.AlreadyRegistered.setOnClickListener {
            val mProgressDialog = ProgressDialog(this)
            mProgressDialog.setTitle("Wait...")
            //.setMessage("This is MESSAGE")
            mProgressDialog.show()
            val intent=Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignUp.setOnClickListener {
            val email = binding.etupEmail.text.toString()
            val pass = binding.etupPassword.text.toString()
            val confirmPass = binding.etupConfirmPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    val mProgressDialog = ProgressDialog(this)
                    mProgressDialog.setTitle("Wait...")
                    //.setMessage("This is MESSAGE")
                    mProgressDialog.show()

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val mProgressDialog = ProgressDialog(this)
                            mProgressDialog.setTitle("Wait...")
                            //.setMessage("This is MESSAGE")
                            mProgressDialog.show()
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}