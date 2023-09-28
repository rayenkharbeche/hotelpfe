package com.example.hotelapplication.Fragment

import android.content.ContentValues
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.hotelapplication.R
import com.example.hotelapplication.databinding.FragmentItemCategorieBinding
import com.example.hotelapplication.Adapter.ItemCategorieAdapter
import com.example.hotelapplication.Adapter.OnItemCategorieClickListener
import com.example.hotelapplication.Api.RetrofitItem
import com.example.hotelapplication.Api.RetrofitItemCategorie
import com.example.hotelapplication.Repository.ItemCategorieRepository
import com.example.hotelapplication.Repository.ItemRepository
import com.example.hotelapplication.data.ItemCategoryResponse
import com.example.hotelapplication.viewmodel.ItemCategorieViewModel
import com.example.hotelapplication.viewmodel.ItemCategorieViewModelFactory
import com.example.hotelapplication.viewmodel.ItemViewModel
import com.example.hotelapplication.viewmodel.SharedViewModel

import com.google.android.material.snackbar.Snackbar

class ItemCategorieFragment : Fragment() , OnItemCategorieClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val LIST_VIEW = "LIST_VIEW"
    private val GRID_VIEW = "GRID_VIEW"
    private var currentView = LIST_VIEW
    private lateinit var binding: FragmentItemCategorieBinding
    private lateinit var viewModel: ItemCategorieViewModel
    private val retrofitItemCategorie = RetrofitItemCategorie.getInstance()
    val retrofitItem = RetrofitItem.getInstance()
    val itemRepository = ItemRepository(retrofitItem)
    val itemViewModel = ItemViewModel(itemRepository)
    private val adapter = ItemCategorieAdapter(this,itemViewModel)
    private lateinit var serviceId: String

    override fun onItemCategorieClicked(itemc: ItemCategoryResponse) {
        sharedViewModel.additemC(itemc)
        val snackbar = Snackbar.make(
            binding.root,
            "The service '${itemc.name}' .",
            Toast.LENGTH_SHORT
        )
        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER
        view.layoutParams = params
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), androidx.appcompat.R.color.material_deep_teal_200))
        snackbar.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      //  val name = arguments?.getString("name")
        binding = FragmentItemCategorieBinding.inflate(inflater, container, false)
        val imageSlider = binding.imageslider
        val imageList = ArrayList<SlideModel>()
        imageList.add(
            SlideModel(
                "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/09/a4/7f/e1/hotel-spa-vilamont.jpg?w=1000&h=-1&s=1",
                "Restaurant"
            )
        )
        imageList.add(
            SlideModel(
                "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/09/a4/81/3a/hotel-spa-vilamont.jpg?w=600&h=-1&s=1",
                "Spa"
            )
        )
        imageList.add(
            SlideModel(
                "https://img.freepik.com/free-photo/luxury-classic-modern-bedroom-suite-hotel_105762-1787.jpg?size=626&ext=jpg",
                "Room"
            )
        )
        imageList.add(
            SlideModel(
                "https://www.mashed.com/img/gallery/the-right-way-to-add-milk-to-your-tea-according-to-science/intro-1686244231.jpg",
                "Coffee"
            )
        )
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        viewModel = ViewModelProvider(this, ItemCategorieViewModelFactory(ItemCategorieRepository(retrofitItemCategorie))).get(
            ItemCategorieViewModel::class.java)

        serviceId = arguments?.getString("serviceId") ?: ""

        binding.recyclerItemC.adapter = adapter
        viewModel.getItemCategorie(serviceId)
        viewModel.serviceResponseListLiveData.observe(viewLifecycleOwner, Observer { itemcResponseList ->
            adapter.setDataList(itemcResponseList)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })


        Log.d(ContentValues.TAG, "Requesting service list from backend...")
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate back to the previous fragment
                parentFragmentManager.popBackStack()
            }
        })
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = binding.search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        listView()

        binding.gridButton.setOnClickListener {
            if (currentView == LIST_VIEW) {
                binding.gridButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_grid_view
                    )
                )
                gridView()
            } else {
                binding.gridButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_format_list_bulleted
                    )
                )
                listView()
            }
        }

        // Appeler la méthode setDataList de l'adapter avec la liste de services
        val itemList = mutableListOf<ItemCategoryResponse>()
        adapter.setDataList(itemList)
    }

    private fun listView() {
        currentView = LIST_VIEW
        binding.recyclerItemC.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerItemC.adapter = adapter
        Log.d(ContentValues.TAG, "Switched to List View")
    }

    private fun gridView() {
        currentView = GRID_VIEW
        binding.recyclerItemC.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerItemC.adapter = adapter
        Log.d(ContentValues.TAG, "Switched to Grid View")
    }

    companion object {
        fun newInstance(name: String?): ItemCategorieFragment {
            val args = Bundle()
            args.putString("name", name)
            val fragment = ItemCategorieFragment()
            fragment.arguments = args
            return fragment
        }
    }




}
