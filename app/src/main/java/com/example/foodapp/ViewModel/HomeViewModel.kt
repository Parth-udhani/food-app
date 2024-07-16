package com.example.foodapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.MealDataBase
import com.example.foodapp.pojo.Category
import com.example.foodapp.pojo.CategoryList
import com.example.foodapp.pojo.Meal
import com.example.foodapp.pojo.MealList
import com.example.foodapp.pojo.MealsByCategory
import com.example.foodapp.pojo.MealsByCategoryList
import com.example.foodapp.retrofit.RetroFitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDataBase) : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favouriteMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private val searchMealsLiveData = MutableLiveData<List<Meal>>()

    //    init {
//        getRandomMeal()
//    }
    private var saveStateRandomMeal: Meal? = null
    fun getRandomMeal() {
        saveStateRandomMeal?.let { randomMeal ->
            randomMealLiveData.postValue(randomMeal)
            return

        }
        RetroFitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    saveStateRandomMeal = randomMeal
                } else return
            }


            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }

    fun getMealByID(id: String) {
        RetroFitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                mealDatabase.mealDao().upsert(meal)
            } catch (e: Exception) {
                Log.d("Exception1", e.message.toString())
            }
        }
    }


    /*fun getPopularItems() {
        RetroFitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    popularItemLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Home Fragment", t.message.toString())
            }


        })
    }*/
    fun getPopularItems() {
        RetroFitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        popularItemLiveData.value = response.body()!!.meals
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("Home Fragment", t.message.toString())
                }


            })
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch { mealDatabase.mealDao().delete(meal) }
    }

    fun getCategories() {
        RetroFitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body().let { categoryList ->
                    categoriesLiveData.postValue(categoryList!!.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun searchMeals(searchQuery: String) =
        RetroFitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealsList = response.body()?.meals
                mealsList?.let { meal ->
                    searchMealsLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })

    fun observeSearchMealsLiveData(): LiveData<List<Meal>> = searchMealsLiveData

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }


    fun observePopularMealLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemLiveData
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun observeFavouriteMealsLiveData(): LiveData<List<Meal>> {
        return favouriteMealsLiveData
    }

    fun observeBottomSheetMEal(): LiveData<Meal> {
        return bottomSheetMealLiveData
    }
}

