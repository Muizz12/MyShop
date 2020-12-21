package com.example.myshop

import android.app.Activity
import android.content.Context
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
import com.example.myshop.Models.Product
import com.example.myshop.utils.Constants
import com.example.myshop.utils.Glide
import kotlinx.android.synthetic.main.activity_addproduct.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class AddproductActivity : BaseActivity(),View.OnClickListener {
    private var selectedImageFileURI:Uri?=null
    private var mproductImageURL:String=" "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_addproduct)
       //setUpActionBar()
        iv_add_update_product.setOnClickListener(this)
        btn_submit_add_product.setOnClickListener(this)
    }
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_add_product_activity)
        val actionBar=supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_colot_back_24dp)
        }
        toolbar_add_product_activity.setNavigationOnClickListener{onBackPressed()}

    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id)
            { R.id.iv_add_update_product -> {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Constants.showImageChooser(this@AddproductActivity)
                }else{
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),Constants.READ_STORAGE_PERMISSION_CODE)
                }

            }
                R.id.btn_submit_add_product->{
                    if(validateProductDetail())
                    {
                        uploadProductImage()
                  }
                }
            }
        }
    }
    fun productUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(this,resources.getString(R.string.Your_Product_Have_Been_Uploaded),Toast.LENGTH_LONG).show()
        finish()
    }
    private fun uploadProductImage(){
        showProgressDialog(resources.getString(R.string.Please_wait))
        Firestore().uploadImageToCloud(this,selectedImageFileURI,Constants.PRODUCT_IMAGE)
    }
    fun ImageUploadSuccess(imageURL:String){
        //hideProgressDialog()
        //Toast.makeText(this,resources.getString(R.string.Product_image_uploaded_successfully),Toast.LENGTH_SHORT).show()
        mproductImageURL=imageURL
        uploadProductDetails()
    }
    private fun uploadProductDetails(){
        val username =
                this.getSharedPreferences(Constants.MYSHOP_PREFERNCES, Context.MODE_PRIVATE)
                        .getString(Constants.LOGGED_IN_USERNAME, "")!!
        val product = Product(
                Firestore().getCurrentUserID(),
                username,
                et_product_title.text.toString().trim { it <= ' ' },
                et_product_price.text.toString().trim { it <= ' ' },
                et_product_description.text.toString().trim { it <= ' ' },
                et_product_quantity.text.toString().trim { it <= ' ' },
                mproductImageURL
        )
        Firestore().uploadproductdetails(this
        ,product)


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
                Toast.makeText(this,resources.getString(R.string.read_stroage_permission_denied), Toast.LENGTH_LONG).show()

            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                   iv_add_update_product.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                    try{
                         selectedImageFileURI=data.data!!
                        Glide(this).loadUserPicture(selectedImageFileURI!!,iv_product_image)
                    }
                    catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this,"Image Upload Fail",Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }
    private fun validateProductDetail():Boolean{
        return when{
            selectedImageFileURI==null->{
                Toast.makeText(this,resources.getString(R.string.err_msg_select_product_image),Toast.LENGTH_SHORT).show()
                false

            }
            TextUtils.isEmpty(et_product_title.text.toString().trim{it <=' '})->{
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_product_title),Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(et_product_price.text.toString().trim { it<= ' ' })->{
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_product_price),Toast.LENGTH_SHORT).show()

                false
            }

            TextUtils.isEmpty(et_product_description.text.toString().trim { it<= ' ' })->{
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_product_description),Toast.LENGTH_SHORT).show()

                false
            }
            TextUtils.isEmpty(et_product_quantity.text.toString().trim { it<= ' ' })->{
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_product_quantity),Toast.LENGTH_SHORT).show()
                false
            }
            else->{
                true
            }


        }
    }

}