import android.graphics.Color
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
class WorkerCheckinOutAdapter(private val topicDetails: List<Worker>) :
    RecyclerView.Adapter<WorkerCheckinOutAdapter.MyViewHolder>() {
    var onItemClick: ((Worker) -> Unit)? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var unit: TextView = view.findViewById(R.id.tvUnit)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)
        var imvAva: ImageView = view.findViewById(R.id.imvAva)
        var imvStatus: TextView = view.findViewById(R.id.imvStatus)
        var imvStatusRed: TextView = view.findViewById(R.id.imvStatusRed)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }

            imvStatus.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }

            imvStatusRed.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_worker_checkin_out_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.fullName
        holder.unit.text = topic.phone
        holder.quantity.text = topic.address

        if (topic.isTeamLeader) {
            holder.type.text = "Đội trưởng"
        } else {
            holder.type.text = "Công nhân"
        }

        if(topic.workingStatus == "idle") {
            holder.imvStatus.text = "Giờ vào"
            holder.imvStatus.visibility = View.VISIBLE
            holder.imvStatusRed.visibility = View.GONE
        } else {
            holder.imvStatus.visibility = View.GONE
            holder.imvStatusRed.visibility = View.VISIBLE
            holder.imvStatus.text = "Giờ ra"
        }

        if (topic.avatarUrl != null) {
            Picasso.get().load(topic.avatarUrl).error(R.drawable.ava).into(holder.imvAva)
        } else {
            holder.imvAva.setImageResource(R.drawable.ava);
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}