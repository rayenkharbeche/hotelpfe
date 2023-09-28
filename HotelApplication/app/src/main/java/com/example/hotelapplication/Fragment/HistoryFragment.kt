package com.example.hotelapplication.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelapplication.databinding.HistoryRecyclerviewBinding
import com.example.hotelapplication.Adapter.HistoryAdapter
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.model.Command
import com.example.hotelapplication.viewmodel.ClientViewModel
import com.example.hotelapplication.viewmodel.ClientViewModelFactory
import com.example.hotelapplication.viewmodel.RegisterActivityViewModel
import com.example.hotelapplication.viewmodel.RegisterActivityViewModelFactory
import org.json.JSONArray
import org.json.JSONObject


class HistoryFragment : Fragment () {

    private lateinit var binding: HistoryRecyclerviewBinding
    private lateinit var ViewModel: ClientViewModel
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var viewModelRegister: RegisterActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = HistoryRecyclerviewBinding.inflate(layoutInflater)

        ViewModel = ViewModelProvider(
            this,
            ClientViewModelFactory(
                ClientRepository(APIService.getUpdatedService()),
                requireActivity().application
            )
        ).get(ClientViewModel::class.java)
        viewModelRegister = ViewModelProvider(
            this, RegisterActivityViewModelFactory(
                requireActivity().application,
                ClientRepository(APIService.getService())
            )
        ).get(RegisterActivityViewModel::class.java)
        viewModelRegister._clientIdLiveData.observe(viewLifecycleOwner) { clientId ->
            Log.d("historique ", "Client ID: $clientId")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyAdapter = HistoryAdapter()

        // Initialize the ViewModel
        ViewModel = ViewModelProvider(
            this,
            ClientViewModelFactory(
                ClientRepository(APIService.getUpdatedService()),
                requireActivity().application
            )
        ).get(ClientViewModel::class.java)

        // Fetch the command history for the given client ID
        val clientId = viewModelRegister.getClientId().value
        if (clientId != null) {
            ViewModel.fetchCommandHistoryByQuery(clientId)
        }

        // Observe the LiveData for command history updates
        ViewModel.commandHistoryLiveData.observe(viewLifecycleOwner) { commandList ->
            if (commandList != null) {
                // Update the RecyclerView adapter with the new data
                historyAdapter.setData(commandList)
            } else {
                Log.d("HistoryFragment", "Command history is null")
            }
        }

        binding.historyRecyclerView.apply {
           layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
                parentFragmentManager.beginTransaction()
                    .remove(this@HistoryFragment)
                    .commit()
            }
        })
}

    companion object {
        fun newInstance(history: MutableList<Command>): StatByTypeFragment {
            val args = Bundle()
            args.putString("history",serializeHistory(history))
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
            // Add other properties here
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

}
    override fun onResume() {
        super.onResume()
        val fragmentTitle = "History" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }
}