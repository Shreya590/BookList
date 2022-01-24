package com.example.github_booklist.network

import com.example.github_booklist.model.RandomBookResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
//https://www.googleapis.com/books/v1/volumes?q=Robert%20C.%20Martin

    @GET("books/v1/volumes")
    suspend fun getRandomBook(
        @Query("q") q : String
    ): Response<RandomBookResponse>

    companion object{
        var retrofit : Retrofit? = null
        fun getRetrofit() : ApiService{
            if(retrofit == null)
            {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://www.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiService::class.java)
        }
    }
}