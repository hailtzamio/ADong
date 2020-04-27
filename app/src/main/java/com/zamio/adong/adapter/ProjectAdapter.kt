


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.Project


/**
 * Created by Hailpt on 4/24/2018.
 */
class ProjectAdapter(private val topicDetails: List<Project>) : RecyclerView.Adapter<ProjectAdapter.MyViewHolder>() {
    var onItemClick: ((Project) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var unit: TextView = view.findViewById(R.id.tvUnit)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)
        var imvStatus: ImageView = view.findViewById(R.id.imvStatus)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }

            imvStatus.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_team_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.name
        holder.type.text = topic.address
        holder.unit.text = topic.plannedStartDate
        holder.quantity.text = topic.plannedEndDate
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}