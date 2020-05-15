


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.WorkOutline


/**
 * Created by Hailpt on 4/24/2018.
 */
class WorkOutlineProjectAdapter(private val topicDetails: List<WorkOutline>) : RecyclerView.Adapter<WorkOutlineProjectAdapter.MyViewHolder>() {
    var onItemClick: ((WorkOutline) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)
        var imvCheck: ImageView = view.findViewById(R.id.icCheck)



        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }

            name.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_project_workoutline_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.projectName
        holder.type.text = topic.workOutlineName.toString()


        if(topic.finishDatetime == null ) {
            holder.quantity.text = "Chưa hoàn thành"
            holder.imvCheck.setImageResource(R.drawable.icon_check)
        } else {
            holder.quantity.text = topic.finishDatetime
            holder.imvCheck.setImageResource(R.drawable.check_green)
        }

//        holder.quantity.text = Utils.convertDate(topic.createdTime)
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}