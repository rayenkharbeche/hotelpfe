package com.example.hotelapplication.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapplication.databinding.HistoryBinding
import com.example.hotelapplication.model.Command
import java.text.SimpleDateFormat


class HistoryAdapter() :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var history: List<Command> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val command = history[position]
        holder.bind(command)
    }

    override fun getItemCount(): Int {
        return history.size
    }

    fun setData(data: List<Command>) {
        history = data
        notifyDataSetChanged()
    }



    inner class ViewHolder(private val binding: HistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(command: Command) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val date = dateFormat.parse(command.date)

            val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
            val timeFormatter = SimpleDateFormat("HH:mm")
            val formattedDate = dateFormatter.format(date)
            val formattedTime = timeFormatter.format(date)

            binding.tv1.text = formattedDate + formattedTime
            binding.tv3.text = command.commandStatus.toString()
            binding.tv2.text = command.treatementDuration
            binding.tv4.text = "${command.totalPrice}DT"
        }
    }
}
