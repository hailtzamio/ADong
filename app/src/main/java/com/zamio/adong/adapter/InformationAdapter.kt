


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
class InformationAdapter(private val topicDetails: ArrayList<Information>) : RecyclerView.Adapter<InformationAdapter.MyViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null
    var onLongItemClick: ((Int) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView = view.findViewById(R.id.tvName)
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var imvNext: ImageView = view.findViewById(R.id.imvNext)
        var view: View = view.findViewById(R.id.view)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }

            itemView.setOnLongClickListener {
                onLongItemClick?.invoke(adapterPosition)
                return@setOnLongClickListener true
            }
        }
    }

    fun removeAt(position: Int) {
        topicDetails.removeAt(position)
        notifyItemRemoved(position)
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

        if(topic.type == "Show") {
            holder.imvNext.visibility = View.VISIBLE
        } else {
            holder.imvNext.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}