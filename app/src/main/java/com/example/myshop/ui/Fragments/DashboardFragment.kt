package com.example.myshop.ui.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myshop.R
import com.example.myshop.SettingsActivity

class DashboardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
       // dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
            textView.text = " "

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashbord_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id=item.itemId
        when(id){
            R.id.action_settings->{
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}