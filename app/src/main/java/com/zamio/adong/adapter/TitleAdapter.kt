import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elcom.com.quizupapp.ui.network.Team
import com.zamio.adong.R


/**
 * Created by Hailpt on 4/24/2018.
 */
class TitleAdapter(private val topicDetails: ArrayList<String>, val type: String) :
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
                if (type == Team.ADONG.type) {
                    when (topic) {
                        "Thêm công nhân" -> holder.imvAva.setImageResource(R.drawable.add_worker)
                        "Danh sách yêu cầu vật tư" -> holder.imvAva.setImageResource(R.drawable.print)
                        "Bản thiết kế" -> holder.imvAva.setImageResource(R.drawable.drawing)
                        "Đánh giá công trình" -> holder.imvAva.setImageResource(R.drawable.healthcare)
                        "An toàn lao động" -> holder.imvAva.setImageResource(R.drawable.hospital)
                        "Kho ảnh" -> holder.imvAva.setImageResource(R.drawable.picture)
                        "Lịch sử điểm danh" -> holder.imvAva.setImageResource(R.drawable.history)
                        "Lịch sử thay đổi" -> holder.imvAva.setImageResource(R.drawable.update_ic)
                        "Báo cáo quyết toán" -> holder.imvAva.setImageResource(R.drawable.chart_ic)
                    }
                } else if (type == Team.CONTRACTOR.type) {
                    when (topic) {
                        "Danh sách đăng ký thi công" -> holder.imvAva.setImageResource(R.drawable.law)
                        "Danh sách yêu cầu vật tư" -> holder.imvAva.setImageResource(R.drawable.print)
                        "Bản thiết kế" -> holder.imvAva.setImageResource(R.drawable.drawing)
                        "Đánh giá công trình" -> holder.imvAva.setImageResource(R.drawable.healthcare)
                        "An toàn lao động" -> holder.imvAva.setImageResource(R.drawable.hospital)
                        "Kho ảnh" -> holder.imvAva.setImageResource(R.drawable.picture)
                        "Lịch sử điểm danh" -> holder.imvAva.setImageResource(R.drawable.history)
                        "Báo cáo quyết toán" -> holder.imvAva.setImageResource(R.drawable.chart_ic)
                        "Lịch sử thay đổi" -> holder.imvAva.setImageResource(R.drawable.update_ic)
                    }
                } else {
                    when (position) {
                        0 -> holder.imvAva.setImageResource(R.drawable.import11)
                        1 -> holder.imvAva.setImageResource(R.drawable.export22)
                        3 -> holder.imvAva.setImageResource(R.drawable.export11)
                        6 -> holder.imvAva.setImageResource(R.drawable.hospital)
                    }
                }
            }
        }
    }
}