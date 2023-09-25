package com.example.hotelapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.hotelapplication.databinding.StatViewpagerBinding
import com.example.hotelapplication.Adapter.SliderPagerAdapter


class SliderFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var binding: StatViewpagerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StatViewpagerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = binding.viewPager
        val adapter = SliderPagerAdapter(childFragmentManager)
        viewPager.adapter = adapter
    }
}
