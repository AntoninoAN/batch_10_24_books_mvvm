package com.example.bookapisearch.model

import android.util.Log
import com.example.bookapisearch.model.remote.BookService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface IRepository {
    fun searchBookByBookTitle(
        bookTitle: String,
        bookType: String,
        bookResultSize: Int,
        successCallback: (BookResponse) -> Unit
    )
}

private const val TAG: String = "IRepository"

class RepositoryImpl(): IRepository {

    private val bookService: BookService by lazy {
        val retrofit = BookService.initRetrofit()
        val bookService = BookService.createService(retrofit)
        bookService
    }

    override fun searchBookByBookTitle(bookTitle: String,
                                       bookType: String,
                                       bookResultSize: Int,
                                       callBackSuccess: (BookResponse) -> Unit) {

        val response = bookService.searchBook(
            bookTitle,
            bookType,
            bookResultSize
        )
        response.enqueue(
            object : Callback<BookResponse> {
                override fun onResponse(
                    call: Call<BookResponse>,
                    response: Response<BookResponse>
                ) {
                    call.cancel()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Log.d(TAG, "onResponse: $it")
//                            bookResponse = it
                            callBackSuccess(it)
                        } ?: kotlin.run {
                            Log.e(TAG, "onResponse: Empty body")
                        }
                    } else {
                        response.errorBody()?.let {
                            Log.e(TAG, "onResponse: ${it.string()}")
                            //callBackEmptyBody(it.string())
                        }
                    }
                }

                override fun onFailure(call: Call<BookResponse>,
                                       t: Throwable) {
                    t.printStackTrace()
                    //callbackFailure(t)
                }
            }
        )
    }
}