package com.example.myshop

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setupactionBar()
        btn_submit.setOnClickListener{
            val email:String=et_mail_forgot_pw.text.toString().trim { it<=' ' }

            if (email.isEmpty()){
                Toast.makeText(this, "Please Enter The Email.", Toast.LENGTH_SHORT).show()

            }
            else{
                showProgressDialog(resources.getString(R.string.Please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener() { task ->
                    hideProgressDialog()
                    if (task.isSuccessful){
                        Toast.makeText(this, "Resest Link Send To The Email.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)

                    }
                    else{
                        Toast.makeText(
                            this,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun setupactionBar() {
        setSupportActionBar(toolbar_forgot_password_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        toolbar_forgot_password_activity.setNavigationOnClickListener { onBackPressed() }

    }



}