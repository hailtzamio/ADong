


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Worker


/**
 * Created by Hailpt on 4/24/2018.
 */
class ChooseTeamLeaderAdapter(private val topicDetails: List<Worker>) : RecyclerView.Adapter<ChooseTeamLeaderAdapter.MyViewHolder>() {
    var onItemClick: ((Worker) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var phone: TextView = view.findViewById(R.id.tvPhone)
        var phone2: TextView = view.findViewById(R.id.tvPhone2)
        var address: TextView = view.findViewById(R.id.tvAddress)
        var imvAva: ImageView = view.findViewById(R.id.imvAva)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_choose_teamleader_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.fullName
        holder.phone.text = topic.phone
        holder.phone2.text = topic.address
//        holder.address.text = topic.address
        if(topic.avatarUrl != null){
            Picasso.get().load(topic.avatarUrl).into(holder.imvAva)
        }else {
            holder.imvAva.setImageResource(R.drawable.ava);
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}