


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.Information
import com.zamio.adong.model.Lorry


/**
 * Created by Hailpt on 4/24/2018.
 */
class InformationAdapter(private val topicDetails: List<Information>) : RecyclerView.Adapter<InformationAdapter.MyViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView = view.findViewById(R.id.tvName)
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var view: View = view.findViewById(R.id.view)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_information_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.tvName.text = topic.name
        holder.tvTitle.text = topic.title

        if(position == topicDetails.size -1) {
            holder.view.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}