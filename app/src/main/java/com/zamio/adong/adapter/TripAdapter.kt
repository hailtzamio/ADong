
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elcom.com.quizupapp.ui.network.AppColor
import com.zamio.adong.R
import com.zamio.adong.model.Trip
import com.zamio.adong.utils.Utils


/**
 * Created by Hailpt on 4/24/2018.
 */
class TripAdapter(private val topicDetails: List<Trip>) :
    RecyclerView.Adapter<TripAdapter.MyViewHolder>() {


    var onItemClick: ((Trip) -> Unit)? = null

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

        if (topic.driverFullName != null && topic.driverFullName !== "") {
            holder.tv2.text = topic.driverFullName
        }

        if (topic.driverPhone != null && topic.driverPhone != "") {
            holder.tv3.text = topic.driverPhone
        }

        if (topic.lorryPlateNumber != null && topic.lorryPlateNumber != "") {
            holder.tv4.text = topic.lorryPlateNumber
        }

        if (topic.plannedDatetime != null && topic.plannedDatetime != "") {
            holder.tv5.text =  Utils.convertDate(topic.plannedDatetime)
        }

        if (topic.status != null) {

            when (topic.status) {
                1 -> {
                    holder.tv6.text = "Mới"
                    holder.tv6.setTextColor(Color.parseColor(AppColor.Red.hex))
                }
                2 -> {
                    holder.tv6.text = "Đã hủy"
                    holder.tv6.setTextColor(Color.parseColor(AppColor.Red.hex))
                }
                3 -> {
                    holder.tv6.text = "Hoàn thành"
                    holder.tv6.setTextColor(Color.parseColor(AppColor.Orange.hex))
                }

                4 -> {
                    holder.tv6.text = "Đang xử lý"
                    holder.tv6.setTextColor(Color.parseColor(AppColor.Green.hex))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}
