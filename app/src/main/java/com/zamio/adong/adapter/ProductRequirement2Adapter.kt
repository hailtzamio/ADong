
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Product


/**
 * Created by Hailpt on 4/24/2018.
 */
class ProductRequirement2Adapter(private val topicDetails: List<Product>, val isShowCheckBox: Boolean) :
    RecyclerView.Adapter<ProductRequirement2Adapter.MyViewHolder>() {
    var onItemClick: ((Product) -> Unit)? = null
    var onItemSelected: ((Int, Boolean) -> Unit)? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName)
        var type: TextView = view.findViewById(R.id.tvType)
        var unit: TextView = view.findViewById(R.id.tvUnit)
        var quantity: TextView = view.findViewById(R.id.tvQuantity)
        var imvAva: ImageView = view.findViewById(R.id.imvAva)
        var cbWorker: EditText = view.findViewById(R.id.cbWorker)
        var edtNote: EditText = view.findViewById(R.id.edtNote)


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(topicDetails[adapterPosition])
            }


            if (isShowCheckBox) {
                cbWorker.visibility = View.VISIBLE
            } else {
                cbWorker.visibility = View.GONE
            }


            cbWorker.addTextChangedListener(object : TextWatcher {

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
                            topicDetails[adapterPosition].quantityChoose = s.toString().toInt()
                        } else {
                            topicDetails[adapterPosition].quantityChoose = 0
                        }
                    }

                }
            })


            edtNote.addTextChangedListener(object : TextWatcher {

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
                            topicDetails[adapterPosition].note = s.toString()
                        } else {
                            topicDetails[adapterPosition].note = ""
                        }
                    }

                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_requirement_layout, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = topicDetails[position]
        holder.name.text = topic.name
        holder.unit.text = topic.unit
        holder.quantity.text = topic.quantity.toString()

        when (topic.type) {
            "buy" -> holder.type.text = "Mua"
            "manufacture" -> holder.type.text = "Sản xuất"
            "tool" -> holder.type.text = "Xuất kho"
        }

        //in some cases, it will prevent unwanted situations
//        holder.cbWorker.setOnCheckedChangeListener(null);

        if (topic.thumbnailUrl != null) {
            Picasso.get().load(topic.thumbnailUrl).error(R.drawable.ava).into(holder.imvAva)
        } else {
            holder.imvAva.setImageResource(R.drawable.ava);
        }

        if (topic.quantityChoose != 0) {
            holder.cbWorker.setText(topic.quantityChoose.toString())
        } else {
            holder.cbWorker.setText("")
        }

        if (topic.note != "") {
            holder.edtNote.setText(topic.note ?: "")
        } else {
            holder.edtNote.setText("")
        }

//        holder.cbWorker.setOnCheckedChangeListener { buttonView, isChecked ->
//            topicDetails[position].isSelected = isChecked
//        }
//
//
//        holder.cbWorker.isChecked = topic.isSelected
    }

    override fun getItemCount(): Int {
        return topicDetails.size
    }
}