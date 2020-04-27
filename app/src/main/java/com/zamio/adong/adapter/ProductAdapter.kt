


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Product


/**
 * Created by Hailpt on 4/24/2018.
 */
class ProductAdapter(private val topicDetails: List<Product>) : RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    var onItemClick: ((Product) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var unit: TextView = view.findViewById(R.id.tvUnit)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)
        var imvAva: ImageView = view.findViewById(R.id.imvAva)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.name
        holder.unit.text = topic.unit
        holder.quantity.text = topic.quantity.toString()

        when (topic.type) {
            "buy" ->  holder.type.text = "Mua tại công trình"
            "manufacture" ->  holder.type.text = "Sản xuất"
            "tool" ->  holder.type.text = "Công cụ"
        }

        if(topic.thumbnailUrl != null){
            Picasso.get().load(topic.thumbnailUrl).error(R.drawable.ava).into(holder.imvAva)
        }else {
            holder.imvAva.setImageResource(R.drawable.ava);
        }

    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}