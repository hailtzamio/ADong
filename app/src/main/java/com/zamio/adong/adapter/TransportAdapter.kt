import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.Transport


/**
 * Created by Hailpt on 4/24/2018.
 */
class TransportAdapter(private val topicDetails: List<Transport>) :
    RecyclerView.Adapter<TransportAdapter.MyViewHolder>() {


    var onItemClick: ((Transport) -> Unit)? = null
    var onItemSelected: ((Int, Boolean) -> Unit)? = null

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
        var cb: CheckBox = view.findViewById(R.id.cb)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }

            cb.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                onItemSelected?.invoke(adapterPosition, isChecked)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transport_request_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]

        if (topic.code != null && topic.code != "") {
            holder.tv1.text = topic.code
        }

        if (topic.tripName != null && topic.tripName !== "") {
            holder.tv2.text = topic.tripName
        }

        if (topic.warehouseName != null && topic.warehouseName != "") {
            holder.tv3.text = topic.warehouseName
        }

        if (topic.plannedDatetime != null && topic.plannedDatetime != "") {
            holder.tv4.text = topic.plannedDatetime
        }

        if (topic.note != null && topic.note != "") {
            holder.tv5.text = topic.note
        }

        if (topic.status != null) {
            when (topic.status) {
                1 -> holder.tv6.text = "Mới"
                2 -> holder.tv6.text = "Đã hủy"
                3 -> holder.tv6.text = "Hoàn thành"
                4 -> {
                    holder.tv6.text = "Đang đi"
                    holder.tv6.setTextColor(Color.RED)
                }
                5 -> {
                    holder.tv6.text = "Đã nhận hàng"
                    holder.tv6.setTextColor(Color.GREEN)
                }
            }
        }

        if (topic.status == 1) {
            holder.cb.visibility = View.VISIBLE
        } else {
            holder.cb.visibility = View.GONE
        }



        holder.cb.isSelected = topic.isSelected ?: false
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}
