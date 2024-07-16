package com.example.foodapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.ViewModel.MealViewModel
import com.example.foodapp.ViewModel.MealViewModelFactory
import com.example.foodapp.databinding.ActivityMealBinding
import com.example.foodapp.db.MealDataBase
import com.example.foodapp.fragments.HomeFragment
import com.example.foodapp.pojo.Meal

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var ytLink: String
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealMVVM: MealViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)

        val mealDatabase = MealDataBase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMVVM = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]
        setContentView(binding.root)

        getMealInformationFromIntent()
        setInformationInViews()
        loadingCase()
        mealMVVM.getMealDetails(mealId)

        observeMealDetailLiveData()
        onYouTubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.fab.setOnClickListener {
            mealTOSave?.let {
                mealMVVM.insertMeal(it)
                Toast.makeText(this, "${mealTOSave!!.strMeal} saved", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private var mealTOSave: Meal? = null

    private fun onYouTubeImageClick() {
        binding.imageYt.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ytLink))
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeMealDetailLiveData() {

        mealMVVM.observeMealDetailsLiveData().observe(
            this
        ) { meal ->
            mealTOSave = meal
            binding.tvCategory.text = "Category: ${meal!!.strCategory}"
            binding.tvLocation.text = "Location: ${meal.strArea}"
            binding.tvInstructionSteps.text = meal.strInstructions
            ytLink = meal.strYoutube!!
            onResponseCase()
        }
    }

    private fun setInformationInViews() {
        binding.collaspingToolbar.title = mealName
        Glide.with(applicationContext).load(mealThumb).into(binding.imgMealDetail)
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }

    private fun loadingCase() {
        binding.fab.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvLocation.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.imageYt.visibility = View.INVISIBLE

        binding.progressbar.visibility = View.VISIBLE


    }

    private fun onResponseCase() {
        binding.fab.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvLocation.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.imageYt.visibility = View.VISIBLE

        binding.progressbar.visibility = View.INVISIBLE


    }

}