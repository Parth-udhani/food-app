package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.PopularItemBinding
import com.example.foodapp.pojo.MealsByCategory

class MostPopularMealAdapter :
    RecyclerView.Adapter<MostPopularMealAdapter.PopularMealViewHolder>() {
    private var mealsList = ArrayList<MealsByCategory>()
    lateinit var onItemClick: ((MealsByCategory) -> Unit)
    var onLongItemClick: ((MealsByCategory) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setMeals(mealsList: ArrayList<MealsByCategory>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(
            PopularItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return mealsList.size
    }


    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)
        holder.itemView.setOnClickListener {
            this.onItemClick.invoke(mealsList[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealsList[position])
            true
        }
    }


    class PopularMealViewHolder(val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}