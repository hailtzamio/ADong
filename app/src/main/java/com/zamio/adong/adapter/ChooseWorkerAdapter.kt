import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Worker2


/**
 * Created by Hailpt on 4/24/2018.
 */
class ChooseWorkerAdapter(private val topicDetails: List<Worker2>) :
    RecyclerView.Adapter<ChooseWorkerAdapter.MyViewHolder>() {
    var onItemClick: ((Worker2) -> Unit)? = null
    var onItemSelected: ((Int, Boolean) -> Unit)? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var unit: TextView = view.findViewById(R.id.tvUnit)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)
        var imvAva: ImageView = view.findViewById(R.id.imvAva)
        var cbWorker: CheckBox = view.findViewById(R.id.cbWorker)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }

            cbWorker.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                onItemSelected?.invoke(adapterPosition, isChecked)
            }
            )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_choose_worker_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val topic = topicDetails[position]
        holder.name.text = topic.fullName
        holder.type.text = topic.phone
        holder.unit.text = topic.address
//        holder.quantity.text = topic.address
        if (topic.avatarUrl != null) {
            Picasso.get().load(topic.avatarUrl).error(R.drawable.ava).into(holder.imvAva)
        } else {
            holder.imvAva.setImageResource(R.drawable.ava);
        }

        holder.cbWorker.isChecked = topic.isSelected

    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }

//    override fun onViewRecycled(holder: MyViewHolder) {
//        super.onViewRecycled(holder)
//        holder.cbWorker.isChecked = false
//    }
}