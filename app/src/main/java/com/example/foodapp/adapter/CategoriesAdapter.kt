package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.CategoryItemBinding
import com.example.foodapp.pojo.Category
import com.example.foodapp.pojo.MealsByCategory

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    private var categories = ArrayList<Category>()
    var onItemClick: ((Category) -> Unit)?=null

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryList(categoryList: List<Category>) {
        this.categories = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categories[position].strCategoryThumb)
            .into(holder.binding.imageCategory)

        holder.itemView.setOnClickListener {
            this.onItemClick!!.invoke(categories[position])

        }

        holder.binding.tvCategoryName.text = categories[position].strCategory

    }
}