package com.example.myshop.Firestore
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myshop.*
import com.example.myshop.Models.Product

import com.example.myshop.Models.User
import com.example.myshop.ui.Fragments.BaseFragment
import com.example.myshop.ui.Fragments.ProductFragment
import com.example.myshop.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Firestore {
    private val mFirestore = FirebaseFirestore.getInstance()
    fun registerUser(activity: RegisterActivity, userinfo: User) {
        mFirestore.collection(Constants.USERS).document(userinfo.id).set(userinfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.UserRegistrationSuccess()
            }.addOnFailureListener {
                activity.hideProgressDialog()
                error("Error while Registring")

            }
    }
    fun uploadproductdetails(activity: AddproductActivity,productinfo: Product){
        mFirestore.collection(Constants.PRODUCTS).document()
                .set(productinfo, SetOptions.merge())
                .addOnSuccessListener {
                    activity.productUploadSuccess()
                }.addOnFailureListener{
                    activity.hideProgressDialog()
                }
    }

    fun getCurrentUserID():String {
       val CurrentUser=FirebaseAuth.getInstance().currentUser
        var  currentUserId=""
        if (CurrentUser!=null){
            currentUserId=CurrentUser.uid
        }
        return currentUserId
    }
    fun getUserDetails(activity: Activity)
    {
        mFirestore.collection(Constants.USERS).document(getCurrentUserID())
            .get().addOnSuccessListener {document ->
                Log.i(activity.javaClass.simpleName,document.toString())
                val user=document.toObject(User::class.java)!!
                val sharedPreference=
                    activity.getSharedPreferences(
                        Constants.MYSHOP_PREFERNCES,
                        Context.MODE_PRIVATE

                    )
                val editor: SharedPreferences.Editor =sharedPreference.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstname}${user.lastname}"
                )
                editor.apply()
                when(activity)
                {
                    is LoginActivity->{
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity->{
                        activity.userdetailsuccess(user)

                    }
                }
            }.addOnFailureListener{e->
                    when(activity){
                        is LoginActivity->{
                            activity.hideProgressDialog()

                        }
                        is SettingsActivity->{
                            activity.hideProgressDialog()
                        }
                    }
                }

    }
    fun updateUserProfileData(activity: Activity,userHashMap: HashMap<String, Any>)
    {
        mFirestore.collection(Constants.USERS).document(getCurrentUserID()).update(userHashMap).addOnSuccessListener {
            when(activity){
                is UserProfileActivity ->{
                    activity.UserProfileActivityUpdateSuccess()

                }
            }

        }.addOnFailureListener{
            e->
            when(activity){
                is UserProfileActivity ->{
                    activity.hideProgressDialog()
                }

            }
            Log.e(activity.javaClass.simpleName,"Error While Updating the User Details",e)
        }

    }
    fun uploadImageToCloud(activity: Activity,imageFileURI:Uri?,imageType:String){
        val sRef:StorageReference=FirebaseStorage.getInstance().reference.child(
                imageType+System.currentTimeMillis()+"."+Constants.getfileExtension(activity,imageFileURI)

        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase Image URL"
            ,taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.e("Downloadable Image URL",uri.toString())
                when(activity){
                    is UserProfileActivity ->{
                        activity.ImageUploadSuccess(uri.toString())
                    }
                    is AddproductActivity->{
                        activity.ImageUploadSuccess(uri.toString())

                    }
                }

            }
        }.addOnFailureListener{
           when(activity){
               is UserProfileActivity ->{
                   activity.hideProgressDialog()
               }
               is AddproductActivity ->{
                   activity.hideProgressDialog()
               }
        }


        }

    }
    //getingProductFromfirestore
    fun getProductList(fragment:Fragment)
    {
        mFirestore.collection(Constants.PRODUCTS)
                .whereEqualTo(Constants.USER_ID,getCurrentUserID()).get()
                .addOnSuccessListener { docment ->
                    Log.e("Product List",docment.documents.toString())
                    val productilist:ArrayList<Product> = ArrayList()
                    for (i in docment.documents)
                    {
                        val product=i.toObject(Product::class.java)
                        product!!.product_id=i.id
                        productilist.add(product)

                    }
                    when(fragment){
                        is ProductFragment ->{
                            fragment.successProductlistFromFireStore(productilist)
                        }
                    }


                }

    }


}