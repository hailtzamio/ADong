


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.Contractor


/**
 * Created by Hailpt on 4/24/2018.
 */
class ContructorAdapter(private val topicDetails: List<Contractor>) : RecyclerView.Adapter<ContructorAdapter.MyViewHolder>() {
    var onItemClick: ((Contractor) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var unit: TextView = view.findViewById(R.id.tvUnit)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)
        var imvAva: ImageView = view.findViewById(R.id.imvAva)
        var imvStatus: ImageView = view.findViewById(R.id.imvStatus)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contructor_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.name
        holder.type.text = topic.phone
        holder.unit.text = topic.email
        holder.quantity.text = topic!!.address + " - " + topic!!.districtName + " - " + topic!!.provinceName

        if(topic.workingStatus == "idle") {
            holder.imvStatus.setImageResource(R.drawable.free_dot)
        } else {
            holder.imvStatus.setImageResource(R.drawable.green_dot)
        }

    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}