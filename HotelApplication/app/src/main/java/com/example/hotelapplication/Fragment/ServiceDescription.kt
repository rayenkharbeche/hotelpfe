package com.example.hotelapplication.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.hotelapplication.Adapter.ImageLo
import com.example.hotelapplication.data.ItemResponse
import com.example.hotelapplication.databinding.ServicesdetailsBinding


class ServiceDescription : Fragment() {
    private lateinit var binding: ServicesdetailsBinding
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ServicesdetailsBinding.inflate(inflater, container, false)
        progressBar = binding.progressBar
        showProgressBar()
        progressBar.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the selected ItemResponse from arguments
        val item = arguments?.getParcelable<ItemResponse>("selectedItem")

        // Display the item details in the UI
        item?.let {
            ImageLo.loadImage(binding.image, it.photo)
            binding.name.text = it.name
            binding.price.text = String.format("%.2f", it.price)
            binding.description.text = it.description
            // Add more views to display other item details as needed
        }
    }
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(item: ItemResponse):  ServiceDescription {
            val args = Bundle()
            args.putParcelable("selectedItem", item)
            val fragment = ServiceDescription()
            fragment.arguments = args
            return fragment
        }
    }


}
