package com.example.bookapisearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapisearch.model.BookResponse
import com.example.bookapisearch.model.BookVolumeInfo
import com.example.bookapisearch.model.IRepository
import com.example.bookapisearch.model.RepositoryImpl
import com.example.bookapisearch.view.BookDisplayFragment

class BookSearchViewModel: ViewModel() {
    private val repository: IRepository by lazy { RepositoryImpl() }
    // MVP Fashion
    private lateinit var view: BookDisplayFragment

    // Backing field
    private val _bookDataResult = MutableLiveData<BookResponse>()
    val bookDataResult: LiveData<BookResponse>
        get() = _bookDataResult

    // Backing field
    private var _SomeInternalValue = "DEFAULT VALUE"
    val publicValue: String
        get() = _SomeInternalValue

    fun searchBooksByArgs(
        bookTitle: String,
        bookPrintType: String,
        maxResult: Int
    ) {
        repository.searchBookByBookTitle(bookTitle, bookPrintType, maxResult) {
//            view.updateBookAdapter(it)
            _bookDataResult.value = it
            _SomeInternalValue = "Other thing"
        }
    }
}




