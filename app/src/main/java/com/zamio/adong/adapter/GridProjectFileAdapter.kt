package com.zamio.adong.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.zamio.adong.R
import com.zamio.adong.model.Project

internal class GridProjectFileAdapter internal constructor(context: Context, private val resource: Int, private val itemList: List<Project>?) : ArrayAdapter<GridProjectFileAdapter.ItemHolder>(context, resource) {

    override fun getCount(): Int {
        return if (this.itemList != null) this.itemList.size else 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ItemHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null)
            holder = ItemHolder()
            holder.name = convertView!!.findViewById(R.id.textView)
            holder.tvCreatedDate = convertView!!.findViewById(R.id.tvCreatedDate)
            holder.icon = convertView.findViewById(R.id.icon)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemHolder
        }

        val projectItem =  this.itemList!![position]

        holder.name!!.text = projectItem.createdByFullName
        holder.tvCreatedDate!!.text = projectItem.createdTime


        return convertView
    }

    internal class ItemHolder {
        var name: TextView? = null
        var tvCreatedDate: TextView? = null
        var icon: ImageView? = null
    }
}
