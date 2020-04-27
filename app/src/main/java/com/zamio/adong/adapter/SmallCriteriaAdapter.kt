


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.CriteriaSmall
import de.hdodenhof.circleimageview.CircleImageView


/**
 * Created by Hailpt on 4/24/2018.
 */
class SmallCriteriaAdapter(private val topicDetails: List<CriteriaSmall>) : RecyclerView.Adapter<SmallCriteriaAdapter.MyViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var phone: TextView = view.findViewById(R.id.tvPhone)
        var imvAva: CircleImageView = view.findViewById(R.id.imvAva)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_small_criteria_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.name
        holder.phone.text = topic.factor.toString()
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}