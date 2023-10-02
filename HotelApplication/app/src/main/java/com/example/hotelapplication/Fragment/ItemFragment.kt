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
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.hotelapplication.Adapter.ItemAdapter
import com.example.hotelapplication.Adapter.OnItemClickListener
import com.example.hotelapplication.Api.RetrofitItem
import com.example.hotelapplication.Converter.GridSpacingItemDecoration
import com.example.hotelapplication.R
import com.example.hotelapplication.Repository.ItemRepository
import com.example.hotelapplication.data.ItemResponse
import com.example.hotelapplication.databinding.FragmentItemBinding
import com.example.hotelapplication.viewmodel.ItemViewModel
import com.example.hotelapplication.viewmodel.ItemViewModelFactory
import com.example.hotelapplication.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class ItemFragment : Fragment(), OnItemClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val LIST_VIEW = "LIST_VIEW"
    private val GRID_VIEW = "GRID_VIEW"
    private var isGridMode = false
    private var currentView = LIST_VIEW
    private lateinit var binding: FragmentItemBinding
    private lateinit var viewModel: ItemViewModel
    private val retrofitItem = RetrofitItem.getInstance()
    private val adapter = ItemAdapter(this)
    private lateinit var serviceId: String
    private lateinit var progressBar: ProgressBar
    private val favoriteItems = mutableListOf<ItemResponse>()

    override fun onItemClicked(item: ItemResponse) {
        sharedViewModel.addItem(item)

        val sna = Snackbar.make(
            binding.root,
            "The product '${item.name}' has been successfully added to your list.",
            Toast.LENGTH_SHORT
        )
        val view = sna.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        view.layoutParams = params
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        sna.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val name = arguments?.getString("name")
        binding = FragmentItemBinding.inflate(inflater, container, false)
        val imageSlider = binding.imageslider
        val imageList = ArrayList<SlideModel>()
        imageList.add(
            SlideModel(
                "https://www.mashed.com/img/gallery/taco-bells-crunchwrap-is-finally-going-vegan/intro-1686224761.webp",
                "Restaurant"
            )
        )
        imageList.add(
            SlideModel(
                "https://www.mashed.com/img/gallery/8-fast-food-chains-who-make-their-milkshakes-with-real-ice-cream-and-7-who-dont/intro-1685549286.webp",
                "Jus"
            )
        )
        imageList.add(
            SlideModel(
                "https://www.mashed.com/img/gallery/fast-food-orders-that-are-surprisingly-good-for-you/chick-fil-a-grilled-market-salad-1642708415.webp",
                "Salade"
            )
        )
        imageList.add(
            SlideModel(
                "https://www.mashed.com/img/gallery/coffee-mistakes-youre-probably-making-at-home/intro-1594766282.jpg",
                "Café"
            )
        )
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        serviceId = arguments?.getString("serviceId") ?: ""
        viewModel = ViewModelProvider(this, ItemViewModelFactory(ItemRepository(retrofitItem))).get(ItemViewModel::class.java)
        binding.recyclerItem.adapter = adapter
        progressBar = binding.progressBar
        showProgressBar()
        viewModel.getItem(serviceId)
        viewModel.ItemResponseListLiveData.observe(viewLifecycleOwner, Observer { dataListt ->
            adapter.setDataListt(dataListt)
            progressBar.visibility = View.GONE
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        })
        Log.d(ContentValues.TAG, "Requesting item list from backend...")
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
        val itemList = mutableListOf<ItemResponse>()
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
                isGridMode = true
                listView()
            }
        }
        adapter.setDataListt(itemList)
    }

    private fun listView() {
        currentView = LIST_VIEW
        binding.recyclerItem.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerItem.adapter = adapter
        Log.d(ContentValues.TAG, "Switched to List View")

    }

    private fun gridView() {
        currentView = GRID_VIEW
        isGridMode = true
        binding.recyclerItem.layoutManager = GridLayoutManager(requireContext(), 2)
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        val itemDecoration = GridSpacingItemDecoration(spacing, 2)
        binding.recyclerItem.addItemDecoration(itemDecoration)
        binding.recyclerItem.adapter = adapter
        Log.d(ContentValues.TAG, "Switched to Grid View")

        binding.sortButton.setOnClickListener {
            adapter.sortListByName()
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    companion object {
        fun newInstance(name: String?): ItemFragment {
            val args = Bundle()
            args.putString("name", name)
            val fragment = ItemFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onResume() {
        super.onResume()
        val fragmentTitle = "List of product" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }
}
