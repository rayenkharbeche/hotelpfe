package com.example.hotelapplication.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.databinding.PiestatBinding
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.model.Command
import com.example.hotelapplication.viewmodel.ClientViewModel
import com.example.hotelapplication.viewmodel.ClientViewModelFactory
import com.example.hotelapplication.viewmodel.RegisterActivityViewModel
import com.example.hotelapplication.viewmodel.RegisterActivityViewModelFactory
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import org.json.JSONArray
import org.json.JSONObject


class StatByTypeFragment : Fragment() {
    private lateinit var binding: PiestatBinding
    private lateinit var viewModel: ClientViewModel
    private lateinit var viewModelRegister: RegisterActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PiestatBinding.inflate(inflater, container, false)

        viewModelRegister = ViewModelProvider(
            this, RegisterActivityViewModelFactory(
                requireActivity().application,
                ClientRepository(APIService.getService())
            )
        ).get(RegisterActivityViewModel::class.java)
        viewModelRegister._clientIdLiveData.observe(viewLifecycleOwner) { clientId ->
            Log.d("statistique ", "Client ID: $clientId")
        }
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

        Log.d("StatByTypeFragment", "ViewModel initialized")

        val pieChart: PieChart = binding.barChart

        // Remplacez "6485962aa005aa6ab8560221" par le véritable ID du client

        val clientId = viewModelRegister.getClientId().value
        Log.d("stat ", "clientIdLiveData: ${viewModelRegister.clientIdLiveData.value}")
        // Récupérer l'historique des commandes pour le client donné
        if (clientId != null) {
            viewModel.fetchCommandHistoryByQuery(clientId)
        }

        // Observez les mises à jour de la LiveData pour l'historique des commandes
        viewModel.commandHistoryLiveData.observe(viewLifecycleOwner) { commandList ->
            // Vérifiez si la liste de commandes n'est pas nulle ou vide
            if (commandList != null && commandList.isNotEmpty()) {
                // Générer le graphique en utilisant les données de l'historique des commandes
                generatePieChart(commandList, pieChart)
            } else {
                // La liste de commandes est nulle ou vide
                Log.d("StatByTypeFragment", "Command history is empty.")
            }
        }
    }

    private fun generatePieChart(historyData: List<Command>, pieChart: PieChart) {
        // Générer les entrées du graphique à partir des données de l'historique des commandes
        val groupedByType = historyData.groupBy { it.commandStatus }
        val entries = groupedByType.map { (type, commands) ->
            PieEntry(commands.size.toFloat(), type?.name ?: "") // Utilisez type.name pour obtenir le nom de l'état de la commande
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.VORDIPLOM_COLORS.toList()
        val pieData = PieData(dataSet)

        // Définir un texte pour la légende du graphique (le titre du DataSet)
        pieChart.data = pieData
        pieChart.setCenterText("Statistiques des commandes par status")
        pieChart.setCenterTextColor(Color.BLACK)
        pieChart.setCenterTextSize(12f)

        pieChart.description.isEnabled = false // Désactiver la description du graphique
        pieChart.legend.isEnabled = true // Activer la légende
        pieChart.legend.textSize = 12f // Taille du texte de la légende
        pieChart.legend.textColor = Color.BLACK // Couleur du texte de la légende
        pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT // Set the direction to LEFT_TO_RIGHT
        pieChart.legend.setDrawInside(false) // Afficher la légende à l'extérieur du graphique

        // Personnaliser le format du texte de la légende (utiliser "\n" pour créer des sauts de ligne)
        pieChart.legend.setWordWrapEnabled(true)
        pieChart.legend.isWordWrapEnabled = true

        // Set the value formatter for the DataSet
        dataSet.valueFormatter = IndexAxisValueFormatter(entries.map { it.label })

        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.animateXY(1000, 1000)
        pieChart.invalidate()
    }

    companion object {
        fun newInstance(history: MutableList<Command>): StatByTypeFragment {
            val args = Bundle()
            args.putString("history", serializeHistory(history))
            val fragment = StatByTypeFragment()
            fragment.arguments = args
            return fragment
        }

        private fun serializeHistory(history: MutableList<Command>): String {
            val jsonArray = JSONArray()
            for (command in history) {
                val jsonObject = JSONObject()
                jsonObject.put("id", command.id)
                jsonObject.put("date", command.date)
                // Ajoutez d'autres propriétés ici
                jsonArray.put(jsonObject)
            }
            return jsonArray.toString()
        }
    }
    override fun onResume() {
        super.onResume()
        val fragmentTitle = "Statistical" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }
}