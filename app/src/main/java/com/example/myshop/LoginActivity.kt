package com.example.myshop

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.myshop.Firestore.Firestore
import com.example.myshop.Models.User
import com.example.myshop.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_splash.*


class LoginActivity : BaseActivity() , View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        tv_forgot_password.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)


    }
    override fun onClick(view:View)
    {
        if(view!=null){
            when(view.id){
                R.id.tv_forgot_password ->{
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)

                }
                R.id.btn_login->{
                    logInRegisteredUser()
                }
                R.id.tv_register ->{
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)

                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when
        {
            TextUtils.isEmpty(et_mail.text.toString().trim{it<=' '}) ->
            {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
            false
            }
            TextUtils.isEmpty(et_password.text.toString().trim{it<=' '}) ->
            {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
                //showErrorSnackBar("your details are valid", false)
                true

            }
        }

    }
    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.Please_wait))

            // Get the text from editText and trim the space
            val email = et_mail.text.toString().trim { it <= ' ' }
            val password = et_password.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    // Hide the progress dialog

                    if (task.isSuccessful) {

                        Firestore().getUserDetails(this@LoginActivity)
                    } else {
                        hideProgressDialog()

                        Toast.makeText(
                            this,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()                    }
                }
        }
    }
    fun userLoggedInSuccess(user: User){
        hideProgressDialog()

        if(user.profileCompleted==0)
        {
            startActivity(Intent(this@LoginActivity,UserProfileActivity::class.java).putExtra(Constants.Extra_User_Details,user))


        }
        else{
            startActivity(Intent(this@LoginActivity,DashboardActivity::class.java))

        }
        finish()


    }


}