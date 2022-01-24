package com.example.github_booklist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.github_booklist.model.ImageLinks
import com.example.github_booklist.model.RandomBook
import com.example.github_booklist.model.RandomBookResponse
import com.example.github_booklist.model.VolumeInfo
import com.example.github_booklist.network.BookRepository
import com.example.github_booklist.utils.UIState
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BookViewModelTest {
    val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var bookRepository: BookRepository

    private  lateinit var viewModel: BookViewModel


    @Before
    fun doBefore() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = BookViewModel(bookRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun laodBook_success() =  runBlockingTest {

        val randomBookResopnce = RandomBookResponse(listOf(
            RandomBook(
                VolumeInfo("Clean Code","A Handbook of Agile Software Craftsmanship",
            ImageLinks("http://books.google.com/books/content?id=_i6bDeoCQzsC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api")
        )
            )
        ))

        whenever(bookRepository.getRandomBook("")).thenReturn(Response.success(randomBookResopnce))

        viewModel.getRandomBook("")

        viewModel.bookLiveData.observeForever() {
            assertEquals(it , randomBookResopnce)
        }

        viewModel.uiState.observeForever() {
            assertEquals(it , UIState.SUCCESS)
        }
    }

    @Test
    fun laodBook_failure() =  runBlockingTest {

        val randomBookResopnce = RandomBookResponse(listOf(RandomBook(VolumeInfo("","",
            ImageLinks("")
        ))))


        whenever(bookRepository.getRandomBook("")).thenReturn(
            Response.error(
            400,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        ))

        viewModel.getRandomBook("")

        viewModel.uiState.observeForever() {
            assertEquals(it , UIState.ERROR)
        }
    }
}