package com.example.github_booklist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.github_booklist.R
import com.example.github_booklist.databinding.ActivityMainBinding
import com.example.github_booklist.model.RandomBook
import com.example.github_booklist.network.ApiService
import com.example.github_booklist.network.BookRepositoryImpl
import com.example.github_booklist.utils.OnBookClickListener
import com.example.github_booklist.viewmodel.BookViewModel
import com.example.github_booklist.viewmodel.BookViewModelFactory

class MainActivity : AppCompatActivity() , OnBookClickListener {

    lateinit var binding: ActivityMainBinding
    private val bookAdapter = BookAdapter(this)
    private val service = ApiService.getRetrofit()
    private val repository = BookRepositoryImpl(service)
    private val viewModel : BookViewModel by lazy {
        ViewModelProvider(
            this,
            BookViewModelFactory(repository)
        )[BookViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bookList.adapter = bookAdapter
        binding.bookList.layoutManager = GridLayoutManager(this,3)
        configureObservers()
        viewModel.getRandomBook(q = "Robert C. Martin")
    }

    private fun configureObservers(){
        viewModel.bookLiveData.observe(this,{
            bookAdapter.setBookList(it.items)
            binding.progressbar.visibility = View.GONE
            binding.errorText.visibility = View.GONE
        })
    }

    override fun onBookClick(q: RandomBook) {
        viewModel.selectedBook()
    }
}