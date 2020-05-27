


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Driver


/**
 * Created by Hailpt on 4/24/2018.
 */
class DriverAdapter(private val topicDetails: List<Driver>) : RecyclerView.Adapter<DriverAdapter.MyViewHolder>() {
    var onItemClick: ((Driver) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var unit: TextView = view.findViewById(R.id.tvUnit)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)
        var imvStatus: ImageView = view.findViewById(R.id.imvStatus)
        var imvAva: ImageView = view.findViewById(R.id.imvAva)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }

            unit.visibility = View.GONE

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_driver_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.fullName
        holder.type.text = topic.phone
        holder.unit.text = topic.email
        holder.quantity.text = topic.tripName

        if(topic.workingStatus == "idle") {
            holder.imvStatus.setImageResource(R.drawable.free_dot)
        } else if (topic.workingStatus == "working") {
            holder.imvStatus.setImageResource(R.drawable.green_dot)
        } else {
            holder.imvStatus.visibility = View.GONE
        }

        if(topic.avatarUrl != null){
            Picasso.get().load(topic.avatarUrl).error(R.drawable.ava).into(holder.imvAva)
        }else {
            holder.imvAva.setImageResource(R.drawable.ava);
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}