


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Worker


/**
 * Created by Hailpt on 4/24/2018.
 */
class WorkerAdapter(private val topicDetails: List<Worker>) : RecyclerView.Adapter<WorkerAdapter.MyViewHolder>() {
    var onItemClick: ((Worker) -> Unit)? = null
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
                .inflate(R.layout.item_product_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.fullName
        holder.unit.text = topic.phone
        holder.quantity.text = topic.address

        if (topic!!.isTeamLeader){
            holder.type.text = "Đội trưởng"
        } else {
            holder.type.text = "Công nhân"
        }

        if(topic.workingStatus == "idle") {
            holder.imvStatus.setImageResource(R.drawable.free_dot)
        } else {
            holder.imvStatus.setImageResource(R.drawable.busy_dot)
        }

        if(topic.avatarUrl != null){
            Picasso.get().load(topic.avatarUrl).into(holder.imvAva)
        }else {
            holder.imvAva.setImageResource(R.drawable.ava);
        }

    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}