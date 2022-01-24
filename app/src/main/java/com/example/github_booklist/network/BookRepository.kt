package com.example.github_booklist.network

import retrofit2.Response
import com.example.github_booklist.model.RandomBookResponse

interface BookRepository {
    suspend fun getRandomBook(q: String): Response<RandomBookResponse>
}
class BookRepositoryImpl(private val service: ApiService):BookRepository {
    override suspend fun getRandomBook(q: String) = service.getRandomBook(q)
}