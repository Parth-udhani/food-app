package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.MealItemBinding
import com.example.foodapp.pojo.MealsByCategory

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewModel>() {
    private var mealList = ArrayList<MealsByCategory>()


    @SuppressLint("NotifyDataSetChanged")
    fun setMeals(mealsList: List<MealsByCategory>) {
        this.mealList = mealsList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    inner class CategoryMealsViewModel(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imageMeal)

        holder.binding.tvMealName.text = mealList[position].strMeal
    }
}