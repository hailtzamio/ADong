import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elcom.com.quizupapp.ui.network.AppColor
import com.zamio.adong.R
import com.zamio.adong.model.Transport
import com.zamio.adong.utils.Utils


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
        var icNext: ImageView = view.findViewById(R.id.icNext)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }

            cb.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                onItemSelected?.invoke(adapterPosition, isChecked)
                Log.e("hailpt", " setOnCheckedChangeListener")
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

        if (topic.warehouseName != null && topic.warehouseName !== "") {
            holder.tv2.text = topic.warehouseName
        }

        if (topic.projectName != null && topic.projectName != "") {
            holder.tv3.text = topic.projectName
        }

        if (topic.plannedDatetime != null && topic.plannedDatetime != "") {
            holder.tv4.text = topic.plannedDatetime
        }

        if (topic.note != null && topic.note != "") {
            holder.tv5.text = topic.note
        }

        if (topic.status != null) {
            when (topic.status) {
                1 ->  {
                    holder.tv6.text = "Mới"
                    holder.tv6.setTextColor(Color.parseColor(AppColor.Red.hex))
                }
                2 -> {
                    holder.tv6.text = "Đã hủy"
                    holder.tv6.setTextColor(Color.parseColor(AppColor.Red.hex))
                }
                3 -> {
                    holder.tv6.text = "Hoàn thành"
                    holder.tv6.setTextColor(Color.parseColor(AppColor.Green.hex))
                }
                4 -> {
                    holder.tv6.text = "Đã ghép xe"
                    holder.tv6.setTextColor(Color.parseColor(AppColor.Orange.hex))
                }
                5 -> {
                    holder.tv6.text = "Đã nhận hàng"
                    holder.tv6.setTextColor(Color.parseColor(AppColor.Orange.hex))
                }
            }
        }

        if (topic.status == 1) {
            holder.cb.visibility = View.VISIBLE
            holder.icNext.visibility = View.GONE
        } else {
            holder.cb.visibility = View.GONE
            holder.icNext.visibility = View.VISIBLE
        }

        holder.cb.isChecked = topic.isSelected
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}
