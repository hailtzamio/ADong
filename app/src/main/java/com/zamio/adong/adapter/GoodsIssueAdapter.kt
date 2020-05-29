import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.GoodsIssue
import com.zamio.adong.model.GoodsNote
import com.zamio.adong.model.WareHouse


/**
 * Created by Hailpt on 4/24/2018.
 */
class GoodsIssueAdapterr(private val topicDetails: List<GoodsIssue>) :
    RecyclerView.Adapter<GoodsIssueAdapterr.MyViewHolder>() {
    var onItemClick: ((GoodsIssue) -> Unit)? = null

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
                onItemClick?.invoke(topicDetails[adapterPosition])
            }
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

        if (topic.projectName != null && topic.projectName !== "") {
            holder.tv2.text = topic.projectName
        }

        if (topic.receiver != null && topic.receiver != "") {
            holder.tv3.text = topic.receiver
        }

        if (topic.note != null && topic.note != "") {
            holder.tv4.text = topic.note
        }

        if (topic.warehouseName != null && topic.warehouseName != "") {
            holder.tv5.text = topic.warehouseName
        }

        if (topic.status != null && topic.status == "DONE") {
            holder.tv6.text = "Hoàn thành"
        } else {
            holder.tv6.text = "Nháp"
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}
