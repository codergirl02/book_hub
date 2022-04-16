package com.example.book_hub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.book_hub.R
import com.example.book_hub.adaptor.FavRecyclerAdaptor
import com.example.book_hub.database.BookDatabase
import com.example.book_hub.database.BookEntity


class FavouriteFragment : Fragment() {

    lateinit var recyclerFavourite: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdaptor : FavRecyclerAdaptor
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    var dbBookList = listOf<BookEntity>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourites)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        layoutManager = GridLayoutManager(activity as Context, 2)

        dbBookList = RetriveFavrites(activity as Context).execute().get()

        if (activity != null){

            progressLayout.visibility = View.GONE
            recyclerAdaptor = FavRecyclerAdaptor(activity as Context, dbBookList)
            recyclerFavourite.adapter = recyclerAdaptor
            recyclerFavourite.layoutManager = layoutManager
        }

        return view
    }

    class RetriveFavrites(val context : Context) : AsyncTask<Void, Void, List<BookEntity>>(){

        override fun doInBackground(vararg p0: Void?): List<BookEntity> {

            val db = Room.databaseBuilder(context, BookDatabase::class.java, "book-db").build()

            return db.bookDao().getAllBooks()
        }
    }
}