package com.example.hotelapplication.Adapter

import android.text.TextPaint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapplication.data.ItemResponse
import com.example.hotelapplication.databinding.MylistBinding
import com.example.hotelapplication.viewmodel.SharedViewModel


class MyCardAdapter(private var serviceList: List<ItemResponse>, private val viewModel: SharedViewModel) :
    RecyclerView.Adapter<MyCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(serviceList[position], viewModel)
        holder.binding.deleteImage.setOnClickListener {
            val deletedItem = serviceList[position]
            viewModel.deleteItem(deletedItem)
            notifyItemRemoved(position)
            viewModel.updateTotalPrice()
        }
    }

    override fun getItemCount() = serviceList.size

    fun updateData(newData: MutableList<ItemResponse>) {
        serviceList = newData
        notifyDataSetChanged()

    }

    class ViewHolder( val binding: MylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemResponse, viewModel: SharedViewModel) {
            var isGridMode = false

            binding.text1name.text = item.name
            binding.text2price.text = item.getPriceAsString()
            Glide.with(binding.displayImage)
                .load(item.photo)
                .into(binding.displayImage)
            binding.executePendingBindings()


            val maxLines = 1
            val maxLengthPerLine = 15 // Adjust this value as needed
            val maxTotalLength = maxLines * maxLengthPerLine

            if (isGridMode) {
                val truncatedDescription =
                    "${item.description.substring(0, maxTotalLength).trim()}..."
                binding.text3des.text = truncatedDescription
            } else {
                // Truncate the description with three dots if it exceeds two lines
                binding.text3des.text = item.description
            }
        }

    }

}

