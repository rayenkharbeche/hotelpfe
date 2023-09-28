package com.example.hotelapplication.Adapter

import ItemFragment
import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapplication.R
import com.example.hotelapplication.data.ServiceResponse
import com.example.hotelapplication.databinding.RecylcerviewBinding
import com.example.hotelapplication.viewmodel.ItemCategorieViewModel


class ListAdapter(
    private val onServiceClickListener: OnServiceClickListener,
    private val viewModel: ItemCategorieViewModel
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>(), Filterable {
    private var dataList = mutableListOf<ServiceResponse>()
    private var filteredDataList: MutableList<ServiceResponse>? = null
    //private var otherItemCategory: ItemCategory = ItemCategory("", null, null, "", "", null)
    private var isGridMode = false


    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: List<ServiceResponse>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        filteredDataList = null
        Log.d("Adapter", "Data service list  set: ${dataList.size} items")
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            RecylcerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    fun sortListByName() {
        val comparator = compareBy<ServiceResponse> { it.name?.lowercase() ?: "" }
        if (filteredDataList != null) {
            filteredDataList = filteredDataList!!.sortedWith(comparator).toMutableList()
        } else {
            dataList.sortWith(comparator)
        }
        notifyDataSetChanged()
    }




    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = if (filteredDataList != null) {
            filteredDataList!![position]
        } else {
            dataList[position]
        }
        holder.bind(data)

        holder.binding.displayImage.setOnClickListener {
            val service = if (filteredDataList != null) {
                filteredDataList!![position]
            } else {
                dataList[position]
            }
            val serviceId = service.id
            val fragment = ItemFragment()
            val bundle = Bundle()
            bundle.putString("serviceId", serviceId)
            fragment.arguments = bundle

            val transaction = (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

            Log.d(ContentValues.TAG, "Clicked.")
        }
    }

    override fun getItemCount(): Int {
        return if (filteredDataList != null) {
            filteredDataList!!.size
        } else {
            dataList.size
        }
    }

    inner class ListViewHolder(val binding: RecylcerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardview1.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val service = if (filteredDataList != null) {
                        filteredDataList!![position]
                    } else {
                        dataList[position]
                    }
                    onServiceClickListener.onServiceClicked(service)
                }
            }
        }

        fun bind(data: ServiceResponse) {
            binding.textName.text = data.name
           /* if (isGridMode) {

                binding.textName.text = data.name
            } else {
                binding.textName.text = data.name
            }*/
            //binding.textphone.text = data.phone
           //binding.textDesc.text = data.description
           // loadImage(binding.displayImage, data.photo)
            Glide.with(binding.displayImage)
                .load(data.photo)
                .into(binding.displayImage)
            binding.executePendingBindings()
         /*   val maxLines = 1
            if (isGridMode) {
                binding.textDesc.text = "${data.description.substring(0, maxLines *5).trim()}..."
            } else {
                binding.textDesc.text = "${data.description.substring(0, maxLines * 50).trim()}..."
            }*/
            binding.textDesc.text = data.description
           /* if (isGridMode) {

                binding.textDesc.text = data.description
            } else {
                binding.textDesc.text = data.description
            }*/
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
                val filteredList = mutableListOf<ServiceResponse>()
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
                filteredDataList = results?.values as? MutableList<ServiceResponse>
                notifyDataSetChanged()
            }
        }
    }
}

