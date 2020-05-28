import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.Information


/**
 * Created by Hailpt on 4/24/2018.
 */
class BaseInformationAdapter(private val topicDetails: List<Information>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null

    val VIEW_TYPE_NORMAL = 1
    val VIEW_TYPE_LINE = 2
    val VIEW_TYPE_LINE_PROFILE = 3


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
//        var tvName: TextView = view.findViewById(R.id.tvName)

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

    inner class MyViewHolderLineProfile(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
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
                .inflate(R.layout.item_information_layout, parent, false)

            return MyViewHolder(itemView)
        } else if (viewType == VIEW_TYPE_LINE_PROFILE) {

            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_line_title, parent, false)

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
        return if (topicDetails[position].type == "Line") {
            VIEW_TYPE_LINE
        } else if (topicDetails[position].type == "Profile") {
            VIEW_TYPE_LINE_PROFILE
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val topic = topicDetails[position]

        when (getItemViewType(position)) {

            VIEW_TYPE_NORMAL -> {
                val cellViewHolder = holder as MyViewHolder
                cellViewHolder.tvTitle.text = topic.title
//                cellViewHolder.tvName.text = topic.name
            }

            VIEW_TYPE_LINE_PROFILE -> {
                val cellViewHolder = holder as MyViewHolderLineProfile
                cellViewHolder.tvTitle.text = topic.title
            }
        }
    }
}