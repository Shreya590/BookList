package com.example.github_booklist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github_booklist.databinding.BookListItemBinding
import com.example.github_booklist.model.RandomBook
import com.example.github_booklist.utils.OnBookClickListener

class BookAdapter(
    private val listener : OnBookClickListener
) : RecyclerView.Adapter<BookViewHolder>() {

    private var bookList = mutableListOf<RandomBook>()
    fun setBookList(list : List<RandomBook>){
        bookList.addAll(list)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BookListItemBinding.inflate(inflater,parent,false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = bookList[position]
        holder.binding.bookTitle.text = currentBook.volumeInfo.title


        currentBook.volumeInfo.imageLinks?.smallThumbnail?.let { imageUrl ->
            Glide.with(holder.binding.bookImage)
                .load(imageUrl.replace("http","https"))
                .into(holder.binding.bookImage)
        }

        holder.itemView.setOnClickListener{
            val message : String = currentBook.volumeInfo.title
            Toast.makeText(holder.itemView.context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount() = bookList.size
}

class BookViewHolder (val binding: BookListItemBinding) : RecyclerView.ViewHolder(binding.root)
