


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.Product


/**
 * Created by Hailpt on 4/24/2018.
 */
class ProductRequirementDetailAdapter(private val topicDetails: List<Product>) : RecyclerView.Adapter<ProductRequirementDetailAdapter.MyViewHolder>() {
    var onItemClick: ((Product) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var unit: TextView = view.findViewById(R.id.tvUnit)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)
        var tvNote: TextView = view.findViewById(R.id.tvNote)


        init {
            itemView.setOnClickListener {
                topicDetails[adapterPosition].isSelected = !topicDetails[adapterPosition].isSelected
                onItemClick?.invoke(topicDetails[adapterPosition])
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_project_requirement_detail_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.productName
        holder.unit.text = topic.productUnit
        holder.quantity.text = topic.quantity.toString()
        holder.tvNote.text = topic.note
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}