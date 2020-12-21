package com.example.myshop

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.example.myshop.Firestore.Firestore
import com.example.myshop.Models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        tv_login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        btn_register.setOnClickListener {
            registerUser()
        }
        setupactionBar()

    }

    private fun setupactionBar() {
        setSupportActionBar(toolbar_register_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        toolbar_register_activity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * Function to validate the entries of a new user
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(et_last_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(et_mail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(et_confirm_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            et_password.text.toString().trim { it <= ' ' } != et_confirm_password.text.toString()
                    .trim    { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !cb_terms_and_condition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
                //showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }

    private fun registerUser() {
        if (validateRegisterDetails()) {
            showProgressDialog(resources.getString(R.string.Please_wait))
            val email: String = et_mail.text.toString().trim { it <= ' ' }
            val password: String = et_password.text.toString().trim { it <= ' ' }
            //create an instance and create a register user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    OnCompleteListener { task ->

                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val user=User(
                                firebaseUser.uid,
                                et_first_name.text.toString().trim{it <=' '},
                                et_last_name.text.toString().trim{it <=' '},
                                et_mail.text.toString().trim{it <=' '}


                            )
                            Firestore().registerUser(this@RegisterActivity,user)

                            //FirebaseAuth.getInstance().signOut()
                            //finish()


                        }else
                        {
                            hideProgressDialog()
                            Toast.makeText(
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                            ).show()                        }
                    }
            )
        }
    }
    fun UserRegistrationSuccess()
    {
        hideProgressDialog()
        Toast.makeText(this,resources.getString(R.string.registery_successfull),Toast.LENGTH_SHORT).show()
    }
}




