package com.example.foodapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.ViewModel.CategoryMealsViewModel
import com.example.foodapp.adapter.CategoryMealsAdapter
import com.example.foodapp.databinding.ActivityCategoryMealsBinding
import com.example.foodapp.fragments.HomeFragment

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryViewModel: CategoryMealsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareRecyclerView()
        categoryViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]
        categoryViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryViewModel.observeMealsLiveData().observe(this) { mealsList ->
            val categoryName=intent.getStringExtra(HomeFragment.CATEGORY_NAME)
            binding.tvCategoryCount.text = "$categoryName: ${mealsList.size}"
            categoryMealsAdapter.setMeals(mealsList)
        }

    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }

    }
}