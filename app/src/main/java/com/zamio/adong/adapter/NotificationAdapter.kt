


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elcom.com.quizupapp.ui.network.AppColor
import com.zamio.adong.R
import com.zamio.adong.model.NotificationOb


/**
 * Created by Hailpt on 4/24/2018.
 */
class NotificationAdapter(private val topicDetails: List<NotificationOb>) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {
    var onItemClick: ((NotificationOb) -> Unit)? = null
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
                .inflate(R.layout.item_notification_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.title
        holder.unit.text = topic.content ?: "---"

        if(topic.seen == true) {
            holder.quantity.text = "Đã đọc"
            holder.quantity.setTextColor(Color.parseColor(AppColor.Green.hex))
        } else {
            holder.quantity.text = "Chưa đọc"
            holder.quantity.setTextColor(Color.parseColor(AppColor.Red.hex))
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}