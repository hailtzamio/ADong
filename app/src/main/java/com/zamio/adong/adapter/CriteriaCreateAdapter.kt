import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.zamio.adong.R
import com.zamio.adong.model.CriteriaSmall


/**
 * Created by Hailpt on 4/24/2018.
 */
class CriteriaCreateAdapter(private val topicDetails: List<CriteriaSmall>) :
    RecyclerView.Adapter<CriteriaCreateAdapter.MyViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var edtCriteria: EditText = view.findViewById(R.id.edtCriteria)
        var edtFactor: EditText = view.findViewById(R.id.edtFactor)
        var imvStatus: ImageView = view.findViewById(R.id.imvAdd)
        var imvRemove: ImageView = view.findViewById(R.id.imvRemove)

        init {
            imvStatus.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }

            imvRemove.setOnClickListener {
                edtCriteria.setText("")
            }

            edtCriteria.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(mEdit: Editable) {

                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (topicDetails.size > adapterPosition) {
                        topicDetails[adapterPosition].name = s.toString()
                    }
                }
            })


            edtFactor.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(mEdit: Editable) {

                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (topicDetails.size > adapterPosition) {
                        if (s.toString() != "") {
                            topicDetails[adapterPosition].factor = s.toString().toInt()
                        } else {
                            topicDetails[adapterPosition].factor = 0
                        }
                    }

                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cretiria_create_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.edtCriteria.setText(topic.name)

        if (topic.factor != 0) {
            holder.edtFactor.setText(topic.factor.toString())
        } else {
            holder.edtFactor.setText("")
        }

        if (position == topicDetails.size - 1) {
            holder.imvStatus.visibility = View.VISIBLE
        } else {
            holder.imvStatus.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}