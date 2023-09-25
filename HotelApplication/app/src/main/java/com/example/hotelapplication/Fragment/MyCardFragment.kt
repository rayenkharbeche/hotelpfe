package com.example.hotelapplication.Fragment

import CommandeViewModel
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hotelapplication.R
import com.example.hotelapplication.databinding.MylistrecyclerviewBinding
import com.example.hotelapplication.Adapter.MyCardAdapter
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.Repository.CommandeRepository
import com.example.hotelapplication.viewmodel.CommandeViewModelFactory
import com.example.hotelapplication.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class MyCardFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var commandeViewModel: CommandeViewModel
    private lateinit var binding: MylistrecyclerviewBinding
    private lateinit var myCardAdapter: MyCardAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val totalPrice = sharedViewModel.getTotalPrice()
        binding = MylistrecyclerviewBinding.inflate(layoutInflater)
        // Configure RecyclerView
        binding.recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        myCardAdapter = MyCardAdapter(sharedViewModel.myitemList.value ?: emptyList(), sharedViewModel)
        binding.recyclerview.adapter = myCardAdapter

        // Observe changes in itemList
        sharedViewModel.myitemList.observe(viewLifecycleOwner) { itemList ->
            myCardAdapter.updateData(itemList)
            binding.totalprx.text = "Total Price: %.2f".format(sharedViewModel.getTotalPrice())
        }

        commandeViewModel = ViewModelProvider(
            this,
            CommandeViewModelFactory(
                CommandeRepository(APIService.getService()),
                requireActivity().application
            )
        ).get(CommandeViewModel::class.java)




        binding.buttonconfirm.setOnClickListener {
            if (totalPrice == 0.0) {
                val snackbar = Snackbar.make(binding.root, "You must add items service first", Snackbar.LENGTH_SHORT)
                val view = snackbar.view
                val params = view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.BOTTOM
                view.layoutParams = params
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                snackbar.show()
            } else {
                sharedViewModel.hasClickedButton = true
                // serviceId = arguments?.getString("serviceId") ?: ""
                val name = arguments?.getString("name") ?: ""
                val id =arguments?.getString("id") ?: ""
                val totalPrice = arguments?.getFloat("totalPrice") ?: 0f
                openPayementFragment(id,name,totalPrice)


            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
                parentFragmentManager.beginTransaction()
                    .remove(this@MyCardFragment)
                    .commit()
            }
        })

        return binding.root


    }




    companion object {
        fun newInstance(name: String?): MyCardFragment {
            val args = Bundle()
          //  args.putString("id", id)
         //   args.putString("serviceId", serviceId)
            args.putString("name", name)
            val fragment = MyCardFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun openPayementFragment(id: String?, name: String?,totalPrice: Float?) {
        sharedViewModel.hasClickedButton = true
        val secondFragment = PayementFragment.newInstance(id, name,totalPrice)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, secondFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        val fragmentTitle = "List of commands" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }
}






