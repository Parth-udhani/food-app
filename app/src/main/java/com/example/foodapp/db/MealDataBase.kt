package com.example.foodapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodapp.pojo.Meal

@Database(entities = [Meal::class], version = 1)
abstract class MealDataBase:RoomDatabase() {

    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        var INSTANCE: MealDataBase? = null

        @Synchronized
        fun getInstance(context: Context): MealDataBase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDataBase::class.java,
                    "meals"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as MealDataBase
        }

    }
}