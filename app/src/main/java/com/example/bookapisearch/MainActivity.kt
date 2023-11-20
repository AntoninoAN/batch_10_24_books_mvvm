package com.example.bookapisearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.example.bookapisearch.model.RepositoryImpl
import com.example.bookapisearch.model.remote.Network
import com.example.bookapisearch.view.BookDisplayFragment
import com.example.bookapisearch.view.BookSearchFragment

class MainActivity : AppCompatActivity(), BookSearchFragment.BookUserInputBridge {

    private val network by lazy {
        Network()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)

//        Thread {
//            network.queryBookSearch(
//                "bible",
//                1,
//                "all"
//            ).also {
//                //Toast.makeText(this, "${it.items.forEach(::println)}}", Toast.LENGTH_SHORT).show()
//                findViewById<TextView>(R.id.text).text ="${it.items.forEach(::println)}}"
//            }
//        }
//        RepositoryImpl().searchBookByBookTitle(
//            "Animal Farm",
//            "all",
//            10
//        )

        openSearchFragment()
    }

    override fun searchBookArgs(bookTitle: String, bookType: String, maxResults: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.display_fragment_container, BookDisplayFragment.newInstance(
                BookDisplayFragment.BookSearchArguments(bookTitle, bookType, maxResults)
            ))
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun openSearchFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.search_fragment_container, BookSearchFragment())
            .commit()
    }
}

// Research, test, evaluate the sample backend endpoint.
// Create the data classes that maps your Json response.
// Add the library dependency Retrofit.
// Create the Service interface.
// Create the Retrofit object.
// Consume the network request.