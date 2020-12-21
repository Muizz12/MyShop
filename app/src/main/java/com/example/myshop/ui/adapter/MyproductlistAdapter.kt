package com.example.myshop.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.Models.Product
import com.example.myshop.R
import com.example.myshop.utils.Glide
import kotlinx.android.synthetic.main.item_list_layout.view.*

open class MyproductlistAdapter (
        private val context:Context,
        private var list:ArrayList<Product>
        ):RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_list_layout,parent,false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if(holder is MyViewHolder){
            Glide(context).loadProductPicture(model.image,holder.itemView.iv_item_image)
            holder.itemView.tv_item_name.text=model.title
            holder.itemView.tv_item_price.text="${model.price}"

        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view)





}