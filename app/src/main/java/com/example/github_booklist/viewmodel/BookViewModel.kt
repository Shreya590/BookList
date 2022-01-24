package com.example.github_booklist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github_booklist.model.RandomBookResponse
import com.example.github_booklist.network.BookRepository
import com.example.github_booklist.utils.UIState
import kotlinx.coroutines.launch

class BookViewModel (private val repository: BookRepository) : ViewModel(){
    //LiveData
    private val _bookLiveData = MutableLiveData<RandomBookResponse>()
    val bookLiveData : LiveData<RandomBookResponse>
        get() = _bookLiveData

    private val _uiState = MutableLiveData<UIState>()
    val uiState : LiveData<UIState>
        get() = _uiState

    fun getRandomBook(q : String){
        _uiState.postValue(UIState.LOADING)
        viewModelScope.launch {
            val response = repository.getRandomBook(q)
            if(response.isSuccessful){
                _bookLiveData.postValue(response.body())
                _uiState.postValue(UIState.SUCCESS)
            }else{
                _uiState.postValue(UIState.ERROR)
            }
        }
    }
    fun selectedBook(){
        _uiState.postValue(UIState.SELECTED)
    }
}