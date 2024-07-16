package com.example.foodapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.MealDataBase
import com.example.foodapp.pojo.Meal
import com.example.foodapp.pojo.MealList
import com.example.foodapp.retrofit.RetroFitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(private val mealDatabase:MealDataBase) : ViewModel() {

    private var mealLiveData = MutableLiveData<Meal>()

    fun getMealDetails(id: String) {
        RetroFitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    mealLiveData.value = response.body()!!.meals[0]
                } else return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())
            }

        })
    }
    fun observeMealDetailsLiveData(): LiveData<Meal> {
        return mealLiveData
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
    fun deleteMeal(meal:Meal){
        viewModelScope.launch { mealDatabase.mealDao().delete(meal) }
    }
}