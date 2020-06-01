import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.Project
import com.zamio.adong.utils.Utils
import java.text.SimpleDateFormat


/**
 * Created by Hailpt on 4/24/2018.
 */
class ProjectAdapter(private val topicDetails: ArrayList<Project>) :
    RecyclerView.Adapter<ProjectAdapter.MyViewHolder>() {
    var onItemClick: ((Project) -> Unit)? = null
    val formatToShow =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var unit: TextView = view.findViewById(R.id.tvUnit)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.name
        holder.type.text = topic.address
//        val plannedStartDate = formatToShow.format(topic.plannedStartDate).toString()
//        val plannedEndDate = formatToShow.format(topic.plannedEndDate).toString()

        holder.unit.text = Utils.convertDate(topic.plannedStartDate)
        holder.quantity.text = Utils.convertDate(topic.plannedEndDate)
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }

    fun addItem(name: Project) {
        topicDetails.add(name)
        notifyItemInserted(topicDetails.size)
    }

    fun removeAt(position: Int) {
        topicDetails.removeAt(position)
        notifyItemRemoved(position)
    }
}