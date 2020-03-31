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
        var view = inflator.inflate(R.layout.item_permission_layout, null)

        var name: TextView = view.findViewById(R.id.tvName)
        var imvAva: ImageView = view.findViewById(R.id.imvAva)

        val topic = topicDetails[position]
        name.text = topic.appEntityCode

        if(topic.appEntityCode == "Xe"){
          imvAva.setImageResource(R.drawable.lorry)
        }

        if(topic.appEntityCode == "Công Nhân"){
         imvAva.setImageResource(R.drawable.worker)
        }

        if(topic.appEntityCode == "Vật Tư"){
          imvAva.setImageResource(R.drawable.materials)
        }

        return view
    }
}
