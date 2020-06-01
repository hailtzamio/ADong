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
class TitleAdapter(private val topicDetails: ArrayList<String>, val type: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    inner class MyViewHolderLine(view: View) : RecyclerView.ViewHolder(view) {


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var itemView: View? = null
        if (viewType == VIEW_TYPE_NORMAL) {
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_title_layout, parent, false)

            return MyViewHolder(itemView)
        } else {
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_line, parent, false)
            return MyViewHolderLine(itemView)
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (topicDetails[position] == "Line") {
            VIEW_TYPE_LINE
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val topic = topicDetails[position]

        when (getItemViewType(position)) {

            VIEW_TYPE_NORMAL -> {
                val cellViewHolder = holder as MyViewHolder
                cellViewHolder.tvTitle.text = topic
                if (type == 1) {
                    when (position) {
                        2 -> holder.imvAva.setImageResource(R.drawable.print)
                        3 -> holder.imvAva.setImageResource(R.drawable.drawing)
                        5 -> holder.imvAva.setImageResource(R.drawable.healthcare)
                        6 -> holder.imvAva.setImageResource(R.drawable.hospital)
                        10 -> holder.imvAva.setImageResource(R.drawable.history)
                        8 -> holder.imvAva.setImageResource(R.drawable.add_worker)
                        9 -> holder.imvAva.setImageResource(R.drawable.picture)
                    }
                } else if (type == 2) {
                    when (position) {
                        2 -> holder.imvAva.setImageResource(R.drawable.print)
                        3 -> holder.imvAva.setImageResource(R.drawable.history)
                        5 -> holder.imvAva.setImageResource(R.drawable.healthcare)
                        6 -> holder.imvAva.setImageResource(R.drawable.hospital)
                    }
                }
            }
        }
    }
}