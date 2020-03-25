


import android.view.LayoutInflater
import android.view.ViewGroup

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.Permission
import com.zamio.adong.model.Product


/**
 * Created by Hailpt on 4/24/2018.
 */
class PermissionAdapter(private val topicDetails: List<Permission>) : RecyclerView.Adapter<PermissionAdapter.MyViewHolder>() {
    var onItemClick: ((Permission) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)



        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_permission_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.appEntityCode

    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}