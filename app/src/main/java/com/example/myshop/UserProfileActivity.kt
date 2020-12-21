package com.example.myshop

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.Firestore.Firestore
import com.example.myshop.Models.User
import com.example.myshop.utils.Constants
import com.example.myshop.utils.Glide
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.et_first_name
import kotlinx.android.synthetic.main.activity_register.et_last_name
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.iv_user_photo
import java.io.IOException

class UserProfileActivity : BaseActivity(),View.OnClickListener {
    private  lateinit var  mUserDetails: User
    private var mselectedImageFileuri: Uri?=null
    private var muserProfileImageURL:String=" "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        if(intent.hasExtra(Constants.Extra_User_Details))
        {
            mUserDetails=intent.getParcelableExtra(Constants.Extra_User_Details)!!
        }

        if (mUserDetails.profileCompleted == 0) {
            et_first_name.isEnabled = false
            et_first_name.setText(mUserDetails.firstname)

            et_last_name.isEnabled = false
            et_last_name.setText(mUserDetails.lastname)

            et_email.isEnabled = false
            et_email.setText(mUserDetails.email)
        }


        if(mUserDetails.profileCompleted==1)
        {
            setUpActionBar()


            Glide(this@UserProfileActivity).loadUserPicture(mUserDetails.image, iv_user_photo)

            et_first_name.setText(mUserDetails.firstname)
            et_last_name.setText(mUserDetails.lastname)

            et_email.isEnabled = false
            et_email.setText(mUserDetails.email)

            if (mUserDetails.mobile != 0L) {
                et_mobile_number.setText(mUserDetails.mobile.toString())
            }
            if (mUserDetails.gender == Constants.MALE) {
                rb_male.isChecked = true
            } else {
                rb_female.isChecked = true
            }

        }
        iv_user_photo.setOnClickListener(this@UserProfileActivity)
        btn_submit.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if(v!=null)
        {
            when(v.id)
            {
                R.id.iv_user_photo ->{
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        //Toast.makeText(this,"You already have the storage permission",Toast.LENGTH_LONG).show()
                        Constants.showImageChooser(this)
                    }
                else
                {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE)
                }

                }
                R.id.btn_submit ->{

                    if(validateUserProfileDetails())
                    {
                        showProgressDialog(resources.getString(R.string.Please_wait))
                        if(mselectedImageFileuri!=null){
                            Firestore().uploadImageToCloud(this,mselectedImageFileuri,Constants.User_profileImage)
                        }
                        else{
                            updateUserProfile()

                        }
                    }


                }
            }

        }
    }
    fun updateUserProfile(){
        val userHashMap=HashMap<String,Any>()
        val mobiliNumber=et_mobile_number.text.toString().trim{it<=' '}
        val firstName = et_first_name.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstname) {

            userHashMap[Constants.FIRST_NAME] = firstName
        }
        val lastName = et_last_name.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.lastname) {

            userHashMap[Constants.LAST_NAME] = lastName
        }
        val gender=if(rb_male.isChecked){
            Constants.MALE
        }
        else{
            Constants.FEMALE
        }
        if(mobiliNumber.isNotEmpty()){
            userHashMap[Constants.MOBILE]=mobiliNumber.toLong()
        }
        if(muserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE]=muserProfileImageURL
        }
        userHashMap[Constants.GENDER]=gender
        userHashMap[Constants.COMPLETE_PROFILE]=1
        Firestore().updateUserProfileData(this,userHashMap)

        //Toast.makeText(this,"Your Details are valid",Toast.LENGTH_LONG).show()

    }

    fun UserProfileActivityUpdateSuccess()
    {
        hideProgressDialog()
        Toast.makeText(this,resources.getString(R.string.msg_profile_update_success),Toast.LENGTH_LONG).show()
        startActivity(Intent(this@UserProfileActivity,DashboardActivity::class.java))
        finish()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                //showErrorSnackBar("The stroage permission is granted",false)
                Constants.showImageChooser(this)
            }
            else
            {
                Toast.makeText(this,resources.getString(R.string.read_stroage_permission_denied),Toast.LENGTH_LONG).show()

            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        // The uri of selected image from phone storage.
                       mselectedImageFileuri = data.data!!

                        //iv_user_photo.setImageURI(selectedImageFileUri)
                        Glide(this).loadUserPicture(mselectedImageFileuri!!,iv_user_photo)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                                this@UserProfileActivity,
                                resources.getString(R.string.Image_selection_failed),
                                Toast.LENGTH_SHORT
                        )
                                .show()
                    }
                }
            }
        }
    }
    private fun validateUserProfileDetails():Boolean
    {
        return when{
            TextUtils.isEmpty(et_mobile_number.text.toString().trim{it<=' '})->{
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_mobile_number),Toast.LENGTH_SHORT).show()
                false

            }
            else ->{
                true
            }

        }
    }
    fun ImageUploadSuccess(imageURL:String){
        //hideProgressDialog()
        muserProfileImageURL=imageURL
        updateUserProfile()
    }
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_user_profile_activity)
        val actionBar=supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_colot_back_24dp)
        }
        toolbar_user_profile_activity.setNavigationOnClickListener{onBackPressed()}

    }
}