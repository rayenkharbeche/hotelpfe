package com.example.hotelapplication.Fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.hotelapplication.Activity.MainActivity
import com.example.hotelapplication.R
import com.example.hotelapplication.databinding.RecyclerViewMenuBinding
import com.example.hotelapplication.Adapter.ListAdapter
import com.example.hotelapplication.Adapter.OnServiceClickListener
import com.example.hotelapplication.Api.RetrofitItemCategorie
import com.example.hotelapplication.Api.RetrofitService
import com.example.hotelapplication.Repository.ItemCategorieRepository
import com.example.hotelapplication.Repository.ServicesRepository
import com.example.hotelapplication.data.ServiceResponse
import com.example.hotelapplication.viewmodel.ItemCategorieViewModel
import com.example.hotelapplication.viewmodel.ServicesViewModel
import com.example.hotelapplication.viewmodel.ServicesviewModelFactory
import com.example.hotelapplication.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class ServiceListFragment : Fragment(), OnServiceClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val LIST_VIEW = "LIST_VIEW"
    private val GRID_VIEW = "GRID_VIEW"
    private var currentView = LIST_VIEW
    private lateinit var binding: RecyclerViewMenuBinding

    private lateinit var viewModel: ServicesViewModel
    private val retrofitService = RetrofitService.getInstance()
    private val retrofitItemCategorie = RetrofitItemCategorie.getInstance()
    private val itemCategorieRepository = ItemCategorieRepository(retrofitItemCategorie)
    private val itemCategorieViewModel = ItemCategorieViewModel(itemCategorieRepository)
    private val adapter = ListAdapter(this, itemCategorieViewModel)
   // private val dataList = mutableListOf<ExtarServices>()
    private var isGridMode = false
    private lateinit var progressBar: ProgressBar
    private var isSearching = false

    override fun onServiceClicked(service: ServiceResponse) {
        sharedViewModel.addService(service)
        val sna = Snackbar.make(
            binding.root,
            "The service '${service.name}' has been added.",
            Toast.LENGTH_SHORT
        )
        val view = sna.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        view.layoutParams = params
        view.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.red))
        sna.show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //val name = arguments?.getString("name")
        binding = RecyclerViewMenuBinding.inflate(inflater, container, false)
        progressBar = binding.progressBar
        val imageSlider = binding.imageslider
        val imageList = ArrayList<SlideModel>()
        imageList.add(
            SlideModel(
                "https://www.mashed.com/img/gallery/breakfast-chains-you-are-about-to-see-everywhere/intro-1684867681.webp",
                "Restaurant"
            )
        )
        imageList.add(
            SlideModel(
                "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/0e/7c/5d/50/hotel-spa-vilamont.jpg?w=700&h=-1&s=1",
                "Spa"
            )
        )
        imageList.add(
            SlideModel(
                "https://www.usatoday.com/gcdn/-mm-/05b227ad5b8ad4e9dcb53af4f31d7fbdb7fa901b/c=0-64-2119-1259/local/-/media/USATODAY/USATODAY/2014/08/13/1407953244000-177513283.jpg?width=660&height=373&fit=crop&format=pjpg&auto=webp",
                "Room"
            )
        )
        imageList.add(
            SlideModel(
                "https://www.mashed.com/img/gallery/coffee-mistakes-youre-probably-making-at-home/using-old-or-stale-beans-1594766282.jpg",
                "Coffee"
            )
        )
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        viewModel = ViewModelProvider(this, ServicesviewModelFactory(ServicesRepository(retrofitService))).get(
            ServicesViewModel::class.java
        )
        binding.recyclerview.adapter = adapter
        viewModel.getAllServices()
        viewModel.serviceResponseListLiveData.observe(viewLifecycleOwner, Observer { serviceResponseList ->
            adapter.setDataList(serviceResponseList)
            progressBar.visibility = View.GONE
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        })

        Log.d(TAG, "Requesting service list from backend...")
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate back to the previous fragment
                parentFragmentManager.popBackStack()
            }
        })

        return binding.root
    }
    private fun handleSearchViewVisibility() {
        if (isSearching) {
            // Cacher la barre de menu en bas lorsque vous êtes en mode de recherche
            binding.card.visibility = View.GONE
            binding.recyclerview.visibility = View.GONE
        } else {
            // Rétablir la visibilité de la barre de menu en bas lorsque vous avez terminé la recherche
            binding.card.visibility = View.VISIBLE
            binding.recyclerview.visibility = View.VISIBLE
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar

        val searchView = binding.search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                isSearching = false
                handleSearchViewVisibility()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                (activity as? MainActivity)?.invalidateOptionsMenu()

                return false
            }
        })

        listView()
        binding.sortButton.setOnClickListener {
            adapter.sortListByName()
        }
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
    }

    private fun listView() {
        currentView = LIST_VIEW
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        Log.d(TAG, "Switched to List View")
    }

    private fun gridView() {
        currentView = GRID_VIEW
        isGridMode = true
        binding.recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        Log.d(TAG, "Switched to Grid View")
    }

    companion object {
        fun newInstance(name: String?): ServiceListFragment {
            val args = Bundle()
            args.putString("name", name)
            val fragment = ServiceListFragment()
            fragment.arguments = args
            return fragment
        }
        fun newInstance(name: String?, email: String?, photo: String?): ProfileFragment {
            val args = Bundle()
            args.putString("name", name ?: "")
            args.putString("email", email ?: "")
            args.putString("photo", photo ?: "")
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }

    }
    override fun onResume() {
        super.onResume()
        val fragmentTitle = "Service list" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                isSearching = true
                (activity as? MainActivity)?.setMenuItemVisibility(false)
                return true
            }
            // Autres cas...
            else -> return super.onOptionsItemSelected(item)
        }
    }


}
