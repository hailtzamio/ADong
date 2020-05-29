import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.GoodsNote
import com.zamio.adong.model.WareHouse


/**
 * Created by Hailpt on 4/24/2018.
 */
class GoodsReceivedNoteAdapter(private val topicDetails: List<GoodsNote>, private val type: Type) :
    RecyclerView.Adapter<GoodsReceivedNoteAdapter.MyViewHolder>() {
    var onItemClick: ((GoodsNote) -> Unit)? = null

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

        when (type) {
            Type.GOODSRECEIEDNOTE -> {
                if (topic.code != null && topic.code != "") {
                    holder.tv1.text = topic.code
                }

                if (topic.ref != null && topic.ref !== "") {
                    holder.tv2.text = topic.ref
                }

                if (topic.deliveredBy != null && topic.deliveredBy != "") {
                    holder.tv3.text = topic.deliveredBy
                }

                if (topic.note != null && topic.note != "") {
                    holder.tv4.text = topic.note
                }

                if (topic.warehouseName != null && topic.warehouseName != "") {
                    holder.tv5.text = topic.warehouseName
                }

                if (topic.status == "DONE") {
                    holder.tv6.text = "Hoàn thành"
                } else {
                    holder.tv6.text = "Nháp"
                }
            }

            Type.GOODSRECEIEDNOTE2 -> {

            }

            Type.GOODSRECEIEDNOTE3 -> {

            }

        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}

sealed class Data

data class DataOne(val list: List<Any>): Data()
data class DataTwo(val list: List<Any>): Data()

enum class Type(val value: Int) {
    GOODSRECEIEDNOTE(1),
    GOODSRECEIEDNOTE2(2),
    GOODSRECEIEDNOTE3(3),
}