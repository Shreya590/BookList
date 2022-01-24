package com.example.github_booklist.network

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookRepositoryTest {
    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var bookRepository: BookRepository

    @Before
    fun doBefore() {
        val baseUrl = mockWebServer.url("/")
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
        bookRepository = BookRepositoryImpl(service = retrofit)
    }

    @Test
    fun getBookResponse_success() {
        runBlocking {
            mockWebServer.enqueue(successString(BOOK_SUCCESS_RESPONSE))
            val actual = bookRepository.getRandomBook("").body()
            Assert.assertEquals(10, actual?.items?.count())
        }
    }

    @Test
    fun getBookResponse_Empty() {
        runBlocking {
            mockWebServer.enqueue(successString(BOOK_EMPTY_RESPONSE))
            val actual = bookRepository.getRandomBook("").body()

            Assert.assertEquals(null, actual?.items?.count())
        }
    }

    private fun successString(file:String) = MockResponse().setBody(getResourceAsString(file))

    private fun getResourceAsString(filename: String): String  {
        return try {
            BookRepositoryTest::class.java.getResourceAsStream(filename).bufferedReader().readText()
        } catch (e: Exception) {
            return  ""
        }
    }
    companion object {
        private const val BOOK_SUCCESS_RESPONSE = "/json/book/bookSuccessResponse.json"
        private const val BOOK_EMPTY_RESPONSE = "/json/book/bookEmptyResponse.json"

    }
}