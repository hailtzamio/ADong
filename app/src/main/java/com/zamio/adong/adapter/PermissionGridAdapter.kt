package com.zamio.adong.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.zamio.adong.R
import com.zamio.adong.model.Permission

class PermissionGridAdapter : BaseAdapter {
    var topicDetails = ArrayList<Permission>()
    var context: Context? = null
    var onItemClick: ((Permission) -> Unit)? = null
    constructor(context: Context, topicDetails: ArrayList<Permission>) : super() {
        this.context = context
        this.topicDetails = topicDetails
    }

    override fun getCount(): Int {
        return topicDetails.size
    }

    override fun getItem(position: Int): Any {
        return topicDetails[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val food = this.topicDetails[position]

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflator.inflate(R.layout.item_permission_grid_layout, null)

        var name: TextView = view.findViewById(R.id.tvName)
        var imvAva: ImageView = view.findViewById(R.id.imvAva)

        val topic = topicDetails[position]
        name.text = topic.name

        if(topic.appEntityCode == "Lorry"){
          imvAva.setImageResource(R.drawable.truck)
        }

        if(topic.appEntityCode == "Worker"){
         imvAva.setImageResource(R.drawable.worker)
        }

        if(topic.appEntityCode == "Product"){
          imvAva.setImageResource(R.drawable.materials)
        }

        if(topic.appEntityCode == "Driver"){
            imvAva.setImageResource(R.drawable.steering)
        }

        if(topic.appEntityCode == "Team"){
            imvAva.setImageResource(R.drawable.team)
        }

        if(topic.appEntityCode == "Contractor"){
            imvAva.setImageResource(R.drawable.labor)
        }

        if(topic.appEntityCode == "Warehouse"){
            imvAva.setImageResource(R.drawable.factory)
        }

        if(topic.appEntityCode == "Project"){
            imvAva.setImageResource(R.drawable.contruction)
        }



        return view
    }
}
