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
import com.nathit.loginsignup_kotlin.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    //ViewBinding การผูกView
    private lateinit var binding: ActivityLoginBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""          //[Java] private String email = "";
    private var password = ""       //[Java] private String password = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure = กำหนดค่า
        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false) //ปิดการสัมผัสหน้าจอแล้วเด้ง progressDialog ออก

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click, open register activity
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        //handle click, begin login
        binding.loginBtn.setOnClickListener {
            //before logging in, validate data
            validateData()
        }
        /*
        [Java] ยาวม๊วกกก
        Button loginBtn = (Button) findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener(new OnClickListener()) {
            public void onClick(View v) {
                //กำหนด event
            }
         */

    }

    private fun validateData() {
        ///TODO("validateData||ตรวจสอบข้อมูลผู้ใช้งาน")
        //get data
        email = binding.emailEt.text.toString()
            .trim()  //[Java] email = emailEt.setText.toString.trim();
        password = binding.passwordEt.text.toString()
            .trim() //[Java] password = passwordEt.setText.toString.trim();

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //invalid email format
            binding.emailEt.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            //no password entered
            binding.passwordEt.error = "Please enter password"
        } else {
            //data is validated, begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        ///TODO("firebaseLogin||เข้าสู่ระบบFirebase")
        //show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email    //รับค่า email จาก firebase มาแสดงใน Toast
                Toast.makeText(this, "LoggedIn as $email", Toast.LENGTH_SHORT)
                    .show() //Show Error message

                //open profile
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT)
                    .show() //Show Error message
            }
    }

    private fun checkUser() {
        ///TODO("checkUser already login||เช็กผู้ใช้งานถ้าเข้าสู่ระบบอยู่ในไปหน้า ProfileActivity")
        //if user is already logged in go to profile activity
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            //user is already logged in
            startActivity(Intent(this, ProfileActivity::class.java))
            //[Java] startActivity(new Intent(LoginActivity.this, ProfileActivity.class)) :)
        }
    }
}