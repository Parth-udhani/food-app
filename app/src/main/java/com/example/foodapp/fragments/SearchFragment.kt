package com.example.foodapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.ViewModel.HomeViewModel
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.adapter.MealsAdapter
import com.example.foodapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerViewAdapter: MealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        binding.imageSearchArrow.setOnClickListener {
            searchMeals()
        }
    }

    private fun searchMeals() {
        val searchQuery = binding.editSearchBox.text.toString()
        if (searchQuery.isNotEmpty()) {
            viewModel.searchMeals(searchQuery)
        }
        observeSearchMealsLiveData()

        var searchJob: Job? = null
        binding.editSearchBox.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeals(searchQuery.toString())
            }
        }
    }

    private fun observeSearchMealsLiveData() {
        viewModel.observeSearchMealsLiveData().observe(viewLifecycleOwner) { mealsList ->
            searchRecyclerViewAdapter.differ.submitList(mealsList)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = MealsAdapter()
        binding.rvRecyclerSearch.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter

        }
    }

}
