package com.example.myshop.ui.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.AddproductActivity
import com.example.myshop.Firestore.Firestore
import com.example.myshop.Models.Product
import com.example.myshop.R
import com.example.myshop.ui.adapter.MyproductlistAdapter
import kotlinx.android.synthetic.main.fragment_product.*

class ProductFragment : BaseFragment() {

    // private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    fun successProductlistFromFireStore(productilist:ArrayList<Product>){
        hideProgressDialog()
        if(productilist.size>0){
            rv_my_product_items.visibility=View.VISIBLE
            tv_no_products_found.visibility=View.GONE
            rv_my_product_items.layoutManager=LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)
            val adapterProduct= MyproductlistAdapter(requireActivity(),productilist)
            rv_my_product_items.adapter=adapterProduct
        }
        else{
            rv_my_product_items.visibility=View.GONE
            tv_no_products_found.visibility=View.VISIBLE


        }
    }
    fun getproductlistfromFirestore(){
        showProgressDialog(resources.getString(R.string.Please_wait))
        Firestore().getProductList(this)
    }

    override fun onResume() {
        super.onResume()
        getproductlistfromFirestore()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_product, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)
        //textView.text = " Home Fragment"
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_add_product) {

            startActivity(Intent(activity, AddproductActivity::class.java))
            // END
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}