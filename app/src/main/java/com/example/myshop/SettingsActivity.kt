package com.example.myshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.myshop.Firestore.Firestore
import com.example.myshop.Models.User
import com.example.myshop.utils.Constants
import com.example.myshop.utils.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : BaseActivity(), View.OnClickListener{
    private lateinit var mUserdetail: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setUpActionBar()
        btn_logout.setOnClickListener(this)
        tv_edit.setOnClickListener(this)


    }
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_settings_activity)
            val actionBar=supportActionBar
            if(actionBar!=null)
            {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setHomeAsUpIndicator(R.drawable.ic_white_colot_back_24dp)
            }
            toolbar_settings_activity.setNavigationOnClickListener{onBackPressed()}

    }
    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.Please_wait))
        Firestore().getUserDetails(this)
    }
    fun userdetailsuccess(user: User){
        mUserdetail=user
        hideProgressDialog()
        Glide(this@SettingsActivity).loadUserPicture(user.image,iv_user_photo)
        tv_name.text="${user.firstname}${user.lastname}"
        tv_gender.text=user.gender
        tv_email.text=user.email
        tv_mobile_number.text="${user.mobile}"

    }
    override fun onResume(){
        super.onResume()
        getUserDetails()

    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.btn_logout->{
                    FirebaseAuth.getInstance().signOut()
                    val intent= Intent(this,LoginActivity::class.java)
                    intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.tv_edit->{
                    val intent=Intent(this,UserProfileActivity::class.java)
                    intent.putExtra(Constants.Extra_User_Details,mUserdetail)
                    startActivity(intent)


                }
            }
        }
    }
}