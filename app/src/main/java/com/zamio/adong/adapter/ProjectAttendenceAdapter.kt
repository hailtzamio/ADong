


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.AttendanceCheckout
import com.zamio.adong.model.Worker


/**
 * Created by Hailpt on 4/24/2018.
 */
class ProjectAttendenceAdapter(private val topicDetails: List<AttendanceCheckout>) : RecyclerView.Adapter<ProjectAttendenceAdapter.MyViewHolder>() {
    var onItemClick: ((AttendanceCheckout) -> Unit)? = null
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
                .inflate(R.layout.item_project_attendence_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.workerFullName
        holder.unit.text = topic.checkinTime

        if(topic.checkoutTime != null) {
            holder.quantity.text = topic.checkoutTime
        } else {
            holder.quantity.text = "Chưa điểm danh ra"
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}