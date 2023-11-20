package com.example.bookapisearch.view

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookapisearch.databinding.FragmentDisplayResultsBinding
import com.example.bookapisearch.model.BookResponse
import com.example.bookapisearch.model.BookVolumeInfo
import com.example.bookapisearch.model.IRepository
import com.example.bookapisearch.model.RepositoryImpl
import kotlinx.parcelize.Parcelize
import java.io.Serializable

class BookDisplayFragment : Fragment() {

    private lateinit var displayBinding: FragmentDisplayResultsBinding
    private val repository: IRepository by lazy { RepositoryImpl() }
    private val adapter: BookAdapter by lazy {
        BookAdapter(emptyList())
    }

    @Parcelize
    data class BookSearchArguments(
        val bookTitle: String,
        val bookType: String,
        val maxResults: Int
    ) : Parcelable

    companion object {
        const val BOOK_SEARCH_ARGS = "BOOK_SEARCH_ARGS"
        fun newInstance(bookSearchArguments: BookSearchArguments) =
            BookDisplayFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BOOK_SEARCH_ARGS, bookSearchArguments)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        displayBinding = FragmentDisplayResultsBinding.inflate(inflater, container, false)

        return displayBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        displayBinding.rvBookResult.layoutManager = GridLayoutManager(context, 2)
        displayBinding.rvBookResult.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(BOOK_SEARCH_ARGS, BookSearchArguments::class.java)?.let {
                repository.searchBookByBookTitle(it.bookTitle, it.bookType, it.maxResults) {
                    // todo update adapter
                    adapter.updateDataSet(
                        it.items?.map { bookItems ->
                            BookVolumeInfo(
                                bookItems.volumeInfo?.title,
                                bookItems.volumeInfo?.imageLinks
                            )
                        } ?: emptyList()
                    )
                }

            }
        } else {
            arguments?.getParcelable<BookSearchArguments>(BOOK_SEARCH_ARGS)?.let {
                repository.searchBookByBookTitle(it.bookTitle, it.bookType, it.maxResults) {

                }
            }
        }
    }
}









