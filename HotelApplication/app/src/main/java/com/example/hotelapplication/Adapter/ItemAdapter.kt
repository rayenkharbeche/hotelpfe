package com.example.hotelapplication.Adapter

import android.annotation.SuppressLint
import android.text.TextPaint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapplication.data.ItemResponse
import com.example.hotelapplication.databinding.RecyclerItemBinding



class ItemAdapter (private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ItemAdapter.ListIViewHolder>(), Filterable {
    private var dataList = mutableListOf<ItemResponse>()
    private var filteredDataList: MutableList<ItemResponse>? = null
    private var isGridMode = false

    @SuppressLint("NotifyDataSetChanged")
    fun setDataListt(dataList: List<ItemResponse>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        filteredDataList = null
        Log.d("Adapter", "Data list item set: ${dataList.size} items")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListIViewHolder {
        return ListIViewHolder(
            RecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }




    override fun onBindViewHolder(holder: ListIViewHolder, position: Int) {
        val data = if (filteredDataList != null) {
            filteredDataList!![position]
        } else {
            dataList[position]
        }

        val currentItem = dataList[position]
        holder.bind(data)



    }

    fun sortListByName() {
        val comparator = compareBy<ItemResponse> { it.name?.lowercase() ?: "" }
        if (filteredDataList != null) {
            filteredDataList = filteredDataList!!.sortedWith(comparator).toMutableList()
        } else {
            dataList.sortWith(comparator)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (filteredDataList != null) {
            filteredDataList!!.size
        } else {
            dataList.size
        }
    }

    inner class ListIViewHolder(val binding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        init {
            binding.cardItem.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val itemc = if (filteredDataList != null) {
                        filteredDataList!![position]
                    } else {
                        dataList[position]
                    }
                    onItemClickListener.onItemClicked(itemc)
                }
            }
        }

        fun bind(data: ItemResponse) {
            //ImageLo.loadImage(binding.image, data.photo)
            Glide.with(binding.image)
                .load(data.photo)
                .into(binding.image)
            binding.executePendingBindings()
            binding.Name.text = data.name
            binding.price.text = String.format("%.2f", data.price)

            val maxLines = 2
            val maxLengthPerLine = 50 // Adjust this value as needed
            val maxTotalLength = maxLines * maxLengthPerLine

            if (isGridMode || data.description.length <= maxTotalLength) {
                // No need to truncate, display the full description
                binding.Desc.text = data.description
            } else {
                // Truncate the description with three dots if it exceeds two lines
                val truncatedDescription = "${data.description.substring(0, maxTotalLength).trim()}..."
                binding.Desc.text = truncatedDescription
            }


        }

        private fun truncateText(text: String, maxLength: Int, textPaint: TextPaint): String {
            val words = text.split(" ")
            var currentLineText = ""
            var lastLineText = ""
            var lines = 0

            for (word in words) {
                if (currentLineText.isNotEmpty()) {
                    currentLineText += " "
                }
                currentLineText += word

                if (textPaint.measureText(currentLineText) > maxLength) {
                    lastLineText = currentLineText
                    currentLineText = ""
                    lines++
                }
            }

            val truncatedText = StringBuilder()

            for (i in 0 until lines - 1) {
                truncatedText.append("\n")
            }

            truncatedText.append(lastLineText)

            return truncatedText.toString()
        }
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<ItemResponse>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(dataList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in dataList) {
                        if (item.name?.toLowerCase()?.contains(filterPattern) == true) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredDataList = results?.values as? MutableList<ItemResponse>
                notifyDataSetChanged()
            }
        }
    }


}
