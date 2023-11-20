package com.example.bookapisearch.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bookapisearch.databinding.FragmentSearchBookBinding

class BookSearchFragment: Fragment() {

    interface BookUserInputBridge{
        fun searchBookArgs(bookTitle: String, bookType: String, maxResults: Int)
    }

    private lateinit var searchBindingFragment: FragmentSearchBookBinding
    private lateinit var bridge: BookUserInputBridge

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is BookUserInputBridge -> bridge = context
            else -> throw Exception("Incorrect Activity")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchBindingFragment = FragmentSearchBookBinding.inflate(inflater, container, false)

        return searchBindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBindingFragment.spBookSearchPrintType.adapter = createBookPrintTypeAdapter()
        searchBindingFragment.sbBookSearchMaxResults.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                min = 1
            }
            max = 40
        }
        searchBindingFragment.fabSearch.setOnClickListener { sendSearchValues() }
    }

    private fun sendSearchValues() {
        searchBindingFragment.apply {
            val bookTitle = tilBookSearchTitle.editText?.text.toString()
            val bookPrintType = spBookSearchPrintType.selectedItem.toString()
            val maxResult = sbBookSearchMaxResults.progress

            if (bookTitle.isNotEmpty() &&
                bookPrintType.isNotEmpty() &&
                maxResult > 0) {
                bridge.searchBookArgs(bookTitle, bookPrintType, maxResult)
            } else {
                Toast.makeText(context, "Incorrect search values", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createBookPrintTypeAdapter(): SpinnerAdapter? {
        return ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            arrayOf("all", "magazines", "books")
        )
    }
}