package com.example.github_booklist.utils

import com.example.github_booklist.model.RandomBook

interface OnBookClickListener {
    fun onBookClick(q: RandomBook)
}