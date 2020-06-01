import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.*


/**
 * Created by Hailpt on 4/24/2018.
 */
class ManuFactureRequestAdapter(private val topicDetails: List<ManuFactureRes>) :
    RecyclerView.Adapter<ManuFactureRequestAdapter.MyViewHolder>() {
    var onItemClick: ((ManuFactureRes) -> Unit)? = null

    companion object {
        private const val VIEW_TYPE_ONE = 0
        private const val VIEW_TYPE_TWO = 1
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv1: TextView = view.findViewById(R.id.tv1)
        var tv2: TextView = view.findViewById(R.id.tv2)
        var tv3: TextView = view.findViewById(R.id.tv3)
        var tv4: TextView = view.findViewById(R.id.tv4)
        var tv5: TextView = view.findViewById(R.id.tv5)
        var tv6: TextView = view.findViewById(R.id.tv6)

        init {
            itemView.setOnClickListener {
                if(topicDetails.size > 0 ) {
                    onItemClick?.invoke(topicDetails[adapterPosition])
                }
            }

            tv6.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_goods_received_note_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]

        if (topic.code != null && topic.code != "") {
            holder.tv1.text = topic.code
        }

        if (topic.warehouseName != null && topic.warehouseName !== "") {
            holder.tv2.text = topic.warehouseName
        }

        if (topic.status != null) {
            holder.tv3.text = topic.status.toString()
        }

        if (topic.plannedDatetime != null && topic.plannedDatetime != "") {
            holder.tv4.text = topic.plannedDatetime
        }

        if (topic.note != null && topic.note != "") {
            holder.tv5.text = topic.note
        }

//        if (topic.status != null && topic.status == "DONE") {
//            holder.tv6.text = "Đã giao"
//        } else {
//            holder.tv6.text = "Nháp"
//        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}
