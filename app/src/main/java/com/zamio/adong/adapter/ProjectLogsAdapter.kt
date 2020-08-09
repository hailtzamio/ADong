import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.LogsProject
import com.zamio.adong.model.ProductRequirement
import com.zamio.adong.utils.Utils


/**
 * Created by Hailpt on 4/24/2018.
 */
class ProjectLogsAdapter(private val topicDetails: ArrayList<LogsProject>) :
    RecyclerView.Adapter<ProjectLogsAdapter.MyViewHolder>() {
    var onItemClick: ((LogsProject) -> Unit)? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var unit: TextView = view.findViewById(R.id.tvUnit)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }

        }
    }


    fun removeAt(position: Int) {
        topicDetails.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project_logs_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.content
        holder.unit.text = Utils.convertDate(topic.createdTime ?: "")


    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}