package com.nathit.loginsignup_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nathit.loginsignup_kotlin.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivityProfileBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    //FirebaseRealtime
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "Profile"

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init firebaseRealtime
        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        //checkUser login
        checkUser()

        //handle click, logout
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
        ///TODO("checkUser||เซ็กผู้ใช้งาน")
        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        //create id user to firebaseRealtime
        val id = firebaseAuth.uid!!

        if (firebaseUser != null) {
            //user not null, user in logged in
            dbRef.child(id).get().addOnSuccessListener {

                if (it.exists()) {

                    val id = it.child("id").value
                    val email = it.child("email").value
                    val password = it.child("password").value

                    //set to text view
                    binding.idTv.text = "UID : " + id.toString()
                    binding.emailTv.text = "EMAIL : " + email.toString()
                    binding.passwordTv.text = "PASSWORD : " + password.toString()

                } else {
                    Toast.makeText(this, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
                }
            } .addOnFailureListener { err ->
                Toast.makeText(this, " Error ${err.message}", Toast.LENGTH_SHORT).show()
            }


        } else {
            //user is null, user is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}