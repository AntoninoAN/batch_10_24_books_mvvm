package com.example.bookapisearch.model.remote

import android.net.Uri
import com.example.bookapisearch.model.BookImageLinks
import com.example.bookapisearch.model.BookItems
import com.example.bookapisearch.model.BookResponse
import com.example.bookapisearch.model.BookVolumeInfo
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

class Network {

    companion object {
        const val BASE_URL = "https://www.googleapis.com/"
        const val ENDPOINT = "books/v1/volumes"
        const val ARG_Q = "q"
        const val ARG_maxResults = "maxResults"
        const val ARG_printType = "printType"


    }

    fun queryBookSearch(bookTitle: String,
                        bookResultSize: Int,
                        bookPrintType: String): BookResponse {

        val booksURI = Uri.parse(BASE_URL+"/"+ ENDPOINT+"?").buildUpon()
            .appendQueryParameter(ARG_Q, bookTitle)
            .appendQueryParameter(ARG_maxResults, bookResultSize.toString())
            .appendQueryParameter(ARG_printType, bookPrintType)
            .build()
        val bookURL = URL(booksURI.toString())

        val bookHttpUrlConnection = bookURL.openConnection() as HttpURLConnection
        bookHttpUrlConnection.readTimeout = 10000
        bookHttpUrlConnection.connectTimeout = 15000
        bookHttpUrlConnection.requestMethod = "GET"
        bookHttpUrlConnection.doInput = true

        bookHttpUrlConnection.connect()

        val responseCode = bookHttpUrlConnection.responseCode

        val bookInputStream: InputStream = bookHttpUrlConnection.inputStream

        return parseBookInputStream(bookInputStream)
    }

    private fun parseBookInputStream(bookInputStream: InputStream): BookResponse {
        val bookParseResponse = StringBuilder()

        val bookReader = BufferedReader(
            InputStreamReader(
                bookInputStream
            )
        )

        var line = bookReader.readLine()

        while (line != null) {
            bookParseResponse.append(line)
            line = bookReader.readLine()
        }

        return if (bookParseResponse.isNotEmpty()) {
            val bookList = deserializeBookResponse(bookParseResponse.toString())
            BookResponse(bookList)
        } else {
            BookResponse(
                emptyList()
            )
        }
    }

    private fun deserializeBookResponse(bookSerialize: String): List<BookItems> {
        val bookJsonResponse = JSONObject(bookSerialize)
        val bookItems = mutableListOf<BookItems>()

        val bookItemJsonArray = bookJsonResponse.getJSONArray("items")
        var thumbnail: String
        var smallThumb: String
        for (index in 0 until bookItemJsonArray.length()) {
            val currentJsonObject = bookItemJsonArray.getJSONObject(index)
            val currentVolumeInfo = currentJsonObject.getJSONObject("volumeInfo")
            try {
                val bookImageJsonObject = currentVolumeInfo.getJSONObject("imageLinks")
               thumbnail = bookImageJsonObject.getString("thumbnail")
                 smallThumb= bookImageJsonObject.getString("smallThumbnail")
            } catch (jsonEx: JSONException) {
                thumbnail = ""
                smallThumb = ""
            }

            val title = currentVolumeInfo.getString("title")

            BookVolumeInfo(
                title,
                BookImageLinks(
                    smallThumb,
                    thumbnail
                )
            ).also {
                BookItems(it).also {
                    bookItems.add(it)
                }
            }
        }

        return bookItems
    }
}





