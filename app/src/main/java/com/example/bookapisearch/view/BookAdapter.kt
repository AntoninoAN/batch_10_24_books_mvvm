package com.example.bookapisearch.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapisearch.R
import com.example.bookapisearch.databinding.ItemLayoutBooksBinding
import com.example.bookapisearch.databinding.ItemLayoutErrorBinding
import com.example.bookapisearch.model.BookItems
import com.example.bookapisearch.model.BookVolumeInfo
import com.squareup.picasso.Picasso

class BookAdapter(private var dataSet: List<BookVolumeInfo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateDataSet(newDataSet: List<BookVolumeInfo>) {
        dataSet = newDataSet
        notifyItemRangeChanged(0, newDataSet.size)
    }

    class BookListVH(private val binding: ItemLayoutBooksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(bookItem: BookVolumeInfo,
                   openDetails: (BookVolumeInfo) -> Unit) {
            binding.apply {
                tvBookTitle.text =
                    binding.root.context.getString(R.string.book_title_header, bookItem.title)
                Picasso
                    .get()
                    .load(bookItem.imageLinks?.smallThumbnail)
                    .error(R.drawable.baseline_broken_image_24)
                    .into(ivBookCover)
            }
        }
    }

    class BookErrorVH(private val binding: ItemLayoutErrorBinding) :
        RecyclerView.ViewHolder(binding.root)

    private enum class BookVHTypes {
        Success, Error
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet.isEmpty())
            BookVHTypes.Error.ordinal
        else
            BookVHTypes.Success.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BookVHTypes.Success.ordinal -> BookListVH(
                ItemLayoutBooksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            BookVHTypes.Error.ordinal -> BookErrorVH(
                ItemLayoutErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> throw Exception("Incorrect ViewHolder Type.")
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BookListVH -> {
                holder.onBind(dataSet[position]) {

                }
            }

            is BookErrorVH -> {
                // todo if we need to send a message from the client/server to be displayed.
            }

            else -> throw Exception("Incorrect ViewHolder Type.")
        }
    }
}



