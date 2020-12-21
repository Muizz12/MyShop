package com.example.myshop.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myshop.R

class OrdersFragment : Fragment() {

    //private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_orders, container, false)
        //val textView: TextView = root.findViewById(R.id.text_notifications)
            //textView.text = "Notification Fragment"
        return root
    }
}