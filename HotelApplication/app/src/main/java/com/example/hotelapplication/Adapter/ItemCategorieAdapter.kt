package com.example.hotelapplication.Adapter

import ItemFragment
import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapplication.R
import com.example.hotelapplication.databinding.RecyclerItemCBinding
import com.example.hotelapplication.data.ItemCategoryResponse
import com.example.hotelapplication.viewmodel.ItemViewModel


class ItemCategorieAdapter (private val onItemCategorieClickListener: OnItemCategorieClickListener,private val viewModel: ItemViewModel) :
    RecyclerView.Adapter<ItemCategorieAdapter.ListitemViewHolder>(), Filterable {
     private var dataList = mutableListOf<ItemCategoryResponse>()
     private var filteredDataList: MutableList<ItemCategoryResponse>? = null



     @SuppressLint("NotifyDataSetChanged")
     fun setDataList(dataList: List<ItemCategoryResponse>) {
         this.dataList.clear()
         this.dataList.addAll(dataList)
         filteredDataList = null
         Log.d("Adapter", "Data list item categorie set: ${dataList.size} items")
         notifyDataSetChanged()
     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListitemViewHolder {
         return ListitemViewHolder(
             RecyclerItemCBinding.inflate(
                 LayoutInflater.from(parent.context),
                 parent,
                 false
             )
         )
     }


     override fun onBindViewHolder(holder: ListitemViewHolder, position: Int) {
         val data = if (filteredDataList != null) {
             filteredDataList!![position]
         } else {
             dataList[position]
         }

        // val currentItem = dataList[position]
         holder.bindc(data)
         //holder.rvNameC.text = currentItem.name

         holder.binding.buttonlisteItem.setOnClickListener {
             if (dataList.isEmpty()) {
                 // La liste est vide, affichez un message
                 Toast.makeText(holder.itemView.context, "La liste est vide.", Toast.LENGTH_SHORT).show()
                 Log.d(ContentValues.TAG, "List Item vide.")
             } else {
             val itemC = if (filteredDataList != null) {
                 filteredDataList!![position]
             } else {
                 dataList[position]
             }
             val itemCategoryId = itemC.id
             val fragment = ItemFragment()
             val bundle = Bundle()
             bundle.putString("itemCategoryId",itemCategoryId)
             fragment.arguments = bundle

             val transaction = (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
             transaction.replace(R.id.flFragment, fragment)
             transaction.addToBackStack(null)
             transaction.commit()

             Log.d(ContentValues.TAG, "Clicked Item.")

         }}
     }


    override fun getItemCount(): Int {
        return if (filteredDataList != null) {
            filteredDataList!!.size
        } else {
            dataList.size
        }
    }

    inner class ListitemViewHolder(val binding:RecyclerItemCBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val rvNameC: TextView = itemView.findViewById(R.id.NameItemC)
        init {
            binding.cardviewC.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val itemc = if (filteredDataList != null) {
                        filteredDataList!![position]
                    } else {
                        dataList[position]
                    }
                    onItemCategorieClickListener.onItemCategorieClicked(itemc)
                }
            }
        }
            fun bindc(data: ItemCategoryResponse) {

                binding.NameItemC.text = data.name

            }
        }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<ItemCategoryResponse>()
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
                filteredDataList = results?.values as? MutableList<ItemCategoryResponse>
                notifyDataSetChanged()
            }
        }
    }
}
