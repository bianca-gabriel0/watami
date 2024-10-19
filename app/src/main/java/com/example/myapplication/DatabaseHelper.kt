package com.example.myapplication

import ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DatabaseHelper {
    private const val BASE_URL = "http://localhost/RiddleGameAdmin/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // Change this function to be a suspend function
    suspend fun getAllQuestions(): List<Game>? {
        return try {
            // Call the API directly
            apiService.getRiddles() // Assuming this method exists in ApiService
        } catch (e: Exception) {
            // Handle the error (e.g., log it)
            null // Return null in case of an error
        }
    }
}