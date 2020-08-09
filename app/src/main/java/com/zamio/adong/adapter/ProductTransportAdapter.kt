import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.ProductRequirement


/**
 * Created by Hailpt on 4/24/2018.
 */
class ProductTransportAdapter(private val topicDetails: ArrayList<ProductRequirement>) :
    RecyclerView.Adapter<ProductTransportAdapter.MyViewHolder>() {
    var onItemClick: ((ProductRequirement) -> Unit)? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var unit: TextView = view.findViewById(R.id.tvUnit)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }

        }
    }


    fun removeAt(position: Int) {
        topicDetails.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project_requirement_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.code
//        holder.unit.text = topic.warehouseName ?: ""


        when (topic.status as Double) {
            1.0 -> holder.unit.text = "Mới"
            3.0 -> holder.unit.text = "Đang xử lý"
            2.0 -> holder.unit.text = "Hoàn thành"
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}