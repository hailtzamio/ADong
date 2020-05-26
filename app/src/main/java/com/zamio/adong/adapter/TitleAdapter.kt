


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R


/**
 * Created by Hailpt on 4/24/2018.
 */
class TitleAdapter(private val topicDetails: ArrayList<String>) : RecyclerView.Adapter<TitleAdapter.MyViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null

    val VIEW_TYPE_NORMAL = 1
    val VIEW_TYPE_LINE = 2


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var imvAva: ImageView = view.findViewById(R.id.imvAva)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var itemView:View? = null
        if (viewType == VIEW_TYPE_NORMAL) {
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_title_layout, parent, false)
        } else {
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_line, parent, false)
        }

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.tvTitle.text = topic

        when(position) {
            1-> holder.imvAva.setImageResource(R.drawable.drawing)
        }

//        if(topic.status == "idle") {
//            holder.imvStatus.setImageResource(R.drawable.free_dot)
//        } else if (topic.status == "working") {
//            holder.imvStatus.setImageResource(R.drawable.green_dot)
//        } else {
//            holder.imvStatus.visibility = View.GONE
//        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }

    override fun getItemViewType(position: Int): Int {

        when(position) {
            0 -> return VIEW_TYPE_NORMAL
            1 -> return VIEW_TYPE_LINE
        }

        return VIEW_TYPE_NORMAL
    }
}