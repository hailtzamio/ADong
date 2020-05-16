


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.WorkOutline


/**
 * Created by Hailpt on 4/24/2018.
 */
class WorkOutlineAdapter(private val topicDetails: List<WorkOutline>) : RecyclerView.Adapter<WorkOutlineAdapter.MyViewHolder>() {
    var onItemClick: ((WorkOutline) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
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
                .inflate(R.layout.item_work_outline_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.name
        holder.unit.text = topic.createdByFullName
        holder.quantity.text = topic.createdTime
//        holder.quantity.text = Utils.convertDate(topic.createdTime)
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}