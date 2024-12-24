import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.zhair.stitchwell.DetailsOfOrder
import com.zhair.stitchwell.Order
import com.zhair.stitchwell.OrderViewHolder
import com.zhair.stitchwell.Size
import com.zhair.stitchwell.databinding.OrderBinding

class OrderAdapter(val items: ArrayList<Order>) : RecyclerView.Adapter<OrderViewHolder>() {

    // Use sizes as a mutable list to hold the data for the adapter
    private var sizes: List<Order> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        Log.i("SizeAdapter", "onCreateViewHolder")
        return OrderViewHolder(
            OrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return sizes.size // Return the size of the sizes list
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        Log.i("SizeAdapter", "onBindViewHolder")
        val item = sizes[position]
        holder.binding.namee.text = item.status
//        holder.binding.number.text = item.expectedDate
//        holder.binding.collar.text = item.size?.collar.toString()
//        holder.binding.length.text = item.size?.length.toString()
//        holder.binding.waist.text = item.size?.waist.toString()
//        holder.binding.shoulder.text = item.size?.shoulder.toString()
//        holder.binding.sleeve.text = item.size?.sleeve.toString()
//        holder.binding.chest.text = item.size?.chest.toString()
//        holder.binding.shirt.text = item.size?.type
//
//        holder.binding.cuff.text = item.size?.cuff.toString()
//        holder.binding.length1.text = item.size?.length1.toString()
//        holder.binding.bottom.text = item.size?.bottom.toString()
//        holder.binding.kunda.text = item.size?.kunda.toString()
//        holder.binding.asan.text = item.size?.asan.toString()

          holder.binding.textView16.text = item.date?.toString()
          holder.binding.textView30.text = item.customInstr?.toString()
        holder.binding.textView32.text = item.tailorComment?.toString()
        holder.binding.textView28.text = item.expectedDate?.toString()
        holder.binding.textView7.text = item.price?.toString()



        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsOfOrder::class.java)
            intent.putExtra("data", Gson().toJson(item))
            holder.itemView.context.startActivity(intent)
        }
    }

    // Update the list of sizes with new data and refresh the RecyclerView
//    fun updateList(newSizes: List<Size>) {
//        sizes = newSizes
//        notifyDataSetChanged()
//    }
}
