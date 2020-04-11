


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Worker2
import de.hdodenhof.circleimageview.CircleImageView


/**
 * Created by Hailpt on 4/24/2018.
 */
class MemberTeamAdapter(private val topicDetails: List<Worker2>, private var isShowRemoveButton: Boolean) : RecyclerView.Adapter<MemberTeamAdapter.MyViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var phone: TextView = view.findViewById(R.id.tvPhone)
        var imvAva: CircleImageView = view.findViewById(R.id.imvAva)
        var imvRemove: CircleImageView = view.findViewById(R.id.imvRemove)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }

            if(isShowRemoveButton) {
                imvRemove.visibility = View.VISIBLE
            } else {
                imvRemove.visibility = View.GONE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_member_team_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.fullName
        holder.phone.text = topic.phone

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