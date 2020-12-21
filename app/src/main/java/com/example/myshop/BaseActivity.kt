package com.example.myshop

import android.app.Dialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {
    private lateinit var mProgressDialog:Dialog
    private var doublebackToExitPressedOnce=false


    fun showErrorSnackBar(message: String,errormessage: Boolean)
    {
        val snackbar=
            Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackbarView=snackbar.view
        if (errormessage)
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarError))
        }
        else
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarSuccess))
        }

    }
    fun showProgressDialog(text:String)
    {
        mProgressDialog=Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.tv_progress_text.text=text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()

    }
    fun hideProgressDialog()
    {
        mProgressDialog.dismiss()
    }
    fun doubleBackToExit(){
        if(doublebackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doublebackToExitPressedOnce=true
        Toast.makeText(this, resources.getString(R.string.please_click_back_again_to_exit),Toast.LENGTH_LONG).show()
        @Suppress("DEPRECATION")
        android.os.Handler().postDelayed({ doublebackToExitPressedOnce = false }, 2000)
    }
}