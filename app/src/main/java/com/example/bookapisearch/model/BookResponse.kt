package com.example.bookapisearch.model

data class BookResponse(
    val items: List<BookItems>?
)

data class BookItems(
    val volumeInfo: BookVolumeInfo?
)

data class BookVolumeInfo(
    val title: String?,
    val imageLinks: BookImageLinks?
)

data class BookImageLinks(
    val smallThumbnail: String?,
    val thumbnail: String?
)