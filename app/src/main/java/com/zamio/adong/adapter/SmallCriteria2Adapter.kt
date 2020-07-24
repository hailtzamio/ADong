


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.Detail
import de.hdodenhof.circleimageview.CircleImageView


/**
 * Created by Hailpt on 4/24/2018.
 */
class SmallCriteria2Adapter(private val topicDetails: List<Detail>) : RecyclerView.Adapter<SmallCriteria2Adapter.MyViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv2: TextView = view.findViewById(R.id.tv2)
        var imvAva: CircleImageView = view.findViewById(R.id.imvAva)
        var rating: AppCompatRatingBar = view.findViewById(R.id.rating)
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_small_critea2_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.tv2.text = topic.criterionName
        holder.rating.rating = topic.point ?: 0.0f
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}