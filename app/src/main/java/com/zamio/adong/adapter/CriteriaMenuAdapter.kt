


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.CriteriaMenu


/**
 * Created by Hailpt on 4/24/2018.
 */
class CriteriaMenuAdapter(private val topicDetails: List<CriteriaMenu>) : RecyclerView.Adapter<CriteriaMenuAdapter.MyViewHolder>() {
    var onItemClick: ((CriteriaMenu) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var rating: AppCompatRatingBar = view.findViewById(R.id.rating)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)
        var tv4: TextView = view.findViewById(R.id.tv4)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cretiria_menu_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.description
        holder.rating.rating = 3.5F
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}