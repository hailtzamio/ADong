


import android.view.LayoutInflater
import android.view.ViewGroup

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.Lorry
import com.zamio.adong.model.Product


/**
 * Created by Hailpt on 4/24/2018.
 */
class LorryAdapter(private val topicDetails: List<Lorry>) : RecyclerView.Adapter<LorryAdapter.MyViewHolder>() {
    var onItemClick: ((Lorry) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var unit: TextView = view.findViewById(R.id.tvUnit)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.brand
        holder.type.text = topic.model
        holder.unit.text = topic.plateNumber
        holder.quantity.text = topic.capacity
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}