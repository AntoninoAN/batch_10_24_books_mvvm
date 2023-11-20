package com.example.bookapisearch.model.remote

import com.example.bookapisearch.model.BookResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService {
    @GET(Network.ENDPOINT) // https://www.something.something/users/{userid}/v1/somehting?q=dfdfd&fdfdfd=
    fun searchBook(
        @Query(Network.ARG_Q) bookTitle: String,
        @Query(Network.ARG_printType) bookPrintType: String,
        @Query(Network.ARG_maxResults) maxResults: Int
    ): Call<BookResponse>

    companion object {
        fun initRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Network.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }

        fun createService(retrofit: Retrofit): BookService =
            retrofit.create(BookService::class.java)
    }
}