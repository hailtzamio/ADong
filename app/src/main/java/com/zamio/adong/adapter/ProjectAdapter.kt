import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elcom.com.quizupapp.ui.network.AppColor
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
        var tv1: TextView = view.findViewById(R.id.tv1)
        var tv2: TextView = view.findViewById(R.id.tv2)
        var tv3: TextView = view.findViewById(R.id.tv3)
        var tv4: TextView = view.findViewById(R.id.tv4)
        var tv5: TextView = view.findViewById(R.id.tv5)
        var tv6: TextView = view.findViewById(R.id.tv6)


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
        holder.tv1.text = topic.name
        holder.tv2.text = topic.address
//        val plannedStartDate = formatToShow.format(topic.plannedStartDate).toString()
//        val plannedEndDate = formatToShow.format(topic.plannedEndDate).toString()

        holder.tv3.text = Utils.convertDate(topic.plannedStartDate)
        holder.tv4.text = Utils.convertDate(topic.plannedEndDate)

        if(topic.teamType == "ADONG") {
//            holder.tv5.text = topic.teamName ?: "---"
            holder.tv5.text = "Đội Á đông"
        }    else {
            holder.tv5.text = "Nhà thầu phụ"
        }

        when (topic.status) {
            "NEW" -> {
                holder.tv6.text = "Mới"
                holder.tv6.setTextColor(Color.parseColor(AppColor.Red.hex))
            }
            "PROCESSING" -> {
                holder.tv6.text = "Đang thi công"
                holder.tv6.setTextColor(Color.parseColor(AppColor.Green.hex))
            }
            "DONE" -> {
                holder.tv6.text = "Hoàn thành"
                holder.tv6.setTextColor(Color.parseColor(AppColor.Gray.hex))
            }
            "PAUSED" -> {
                holder.tv6.text = "Tạm dừng"
                holder.tv6.setTextColor(Color.parseColor(AppColor.Blue.hex))
            }
        }

        if(topic.status == null) {
            holder.tv6.text = "Đấu thầu"
        }


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