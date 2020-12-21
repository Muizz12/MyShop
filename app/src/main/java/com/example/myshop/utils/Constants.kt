package com.example.myshop.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.example.myshop.BaseActivity

object Constants {
    //Colections
    const val USERS:String="users"
    const val PRODUCTS:String="products"



    const val MYSHOP_PREFERNCES:String="MyShopPrefs"
    const val LOGGED_IN_USERNAME:String="Logged_in_username"
    const val Extra_User_Details:String="extra_user_detail"
    const val READ_STORAGE_PERMISSION_CODE=2
    const val PICK_IMAGE_REQUEST_CODE=1
    const val MALE:String="Male"
    const val FEMALE:String="Female"
    const val FIRST_NAME:String="firstname"
    const val LAST_NAME:String="lastname"
    const val MOBILE:String="mobile"
    const val GENDER:String="gender"
    const val IMAGE:String="image"
    const val User_profileImage:String="User_profile_image"
    const val COMPLETE_PROFILE:String="profileCompleted"
    const val PRODUCT_IMAGE:String="Product_Image"
    const val USER_ID:String="user_id"



    fun showImageChooser(activity:Activity)
    {
        val galleryIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    fun getfileExtension(activity: Activity,uri: Uri?):String?
    {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))

    }




}