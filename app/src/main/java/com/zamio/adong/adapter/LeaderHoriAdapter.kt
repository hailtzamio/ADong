import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Worker


/**
 * Created by Hailpt on 4/24/2018.
 */
class LeaderHoriAdapter(private val topicDetails: MutableList<Worker>) :
    RecyclerView.Adapter<LeaderHoriAdapter.MyViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //        var name: TextView = view.findViewById(R.id.tvName)
//        var type: TextView = view.findViewById(R.id.tvType)
//        var unit: TextView = view.findViewById(R.id.tvUnit)
//        var quantity: TextView = view.findViewById(R.id.tvQuantity)
        var imvAva: ImageView = view.findViewById(R.id.imvAva)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_team_leader_hori_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
//        holder.name.text = topic.brand
//        holder.type.text = topic.model
//        holder.unit.text = topic.plateNumber
//        holder.quantity.text = topic.capacity
        if (topic.avatarUrl != null) {
            Picasso.get().load(topic.avatarUrl).into(holder.imvAva)
        } else {
            holder.imvAva.setImageResource(R.drawable.ava);
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}