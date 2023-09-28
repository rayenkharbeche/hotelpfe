package com.example.hotelapplication.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.databinding.StatBinding
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.model.Command
import com.example.hotelapplication.viewmodel.ClientViewModel
import com.example.hotelapplication.viewmodel.ClientViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter




class StatFragment : Fragment() {
    private lateinit var binding: StatBinding
    private lateinit var viewModel: ClientViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ClientViewModelFactory(
                ClientRepository(APIService.getUpdatedService()),
                requireActivity().application
            )
        ).get(ClientViewModel::class.java)

        val barChart: BarChart = binding.barChart
        viewModel.client.observe(viewLifecycleOwner) { client ->
            client?.let {
                val historyData = client.history
                generateBarChart(historyData, barChart)
            }
        }
    }

    private fun generateBarChart(historyData: List<Command>, barChart: BarChart) {
        val groupedByDate = historyData.groupBy { it.date } // Replace 'date' with the actual property name for the date
        val aggregatedDataByDate = groupedByDate.map { (date, commands) ->
            val totalRevenue = commands.sumByDouble { it.totalPrice?.toDouble() ?: 0.0 } // Handle nullable totalPrice
            date to totalRevenue
        }
        val entries = aggregatedDataByDate.mapIndexed { index, (date, revenue) ->
            BarEntry(index.toFloat(), revenue.toFloat())
        }

        val dataSet = BarDataSet(entries, "Expenses by Date")
        dataSet.color = Color.BLUE
        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(aggregatedDataByDate.map { it.first }) // Assuming 'first' is the date
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.xAxis.labelCount = aggregatedDataByDate.size
        barChart.description.isEnabled = false
    }


}
