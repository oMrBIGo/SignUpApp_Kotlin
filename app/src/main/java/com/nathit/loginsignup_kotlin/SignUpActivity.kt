package com.nathit.loginsignup_kotlin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nathit.loginsignup_kotlin.databinding.ActivitySignUpBinding
import com.nathit.loginsignup_kotlin.model.UserModel

class SignUpActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivitySignUpBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private var email = ""
    private var  password = ""

    //FirebaseRealtime
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account In...")
        progressDialog.setCanceledOnTouchOutside(false) //ปิดการสัมผัสหน้าจอแล้วเด้ง progressDialog ออก

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        //init firebaseRealtime
        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        //handle click, begin sign up
        binding.SignUpBtn.setOnClickListener {
            validateData()
        }

    }

    private fun validateData() {
        ///TODO("validateData||ตรวจสอบข้อมูลสมาชิก")
        //get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //invalid email format
            binding.emailEt.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            //password isn't entered
            binding.passwordEt.error = "Please enter password"
        } else if (password.length <6){
            //password length is less than 6
            binding.passwordEt.error = "Password must atlas 6 characters long"
        } else {
            //date is valid, continue signup
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        ///TODO("firebaseSignUp||ลงทะเบียนผู้ใช้งานFirebase")
        //show progress
        progressDialog.show()

        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //signup success
                progressDialog.dismiss()
                //get current user
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()

                //add Data to firebase realtime
                realtimeAddUser()

                //open profile
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener {e->
                //signup failed
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun realtimeAddUser() {
        ///TODO("realtimeAddUser||เพิ่มผู้ใช้งานลงในฐานข้อมูล firebase Realtime")
        //create id user to firebaseRealtime
        val id = firebaseAuth.uid!!
        //connect Model (UserModel)
        val userModel = UserModel(id, email, password)

        dbRef.child(id).setValue(userModel)
            .addOnCompleteListener {
                Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener { err ->
                Toast.makeText(this, "Login Error ${err.message} ", Toast.LENGTH_LONG).show()
            }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() //go back to previous activity, when back button of actionbar clicked
        return super.onSupportNavigateUp()
    }
}