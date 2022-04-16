package com.example.book_hub.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.book_hub.R
import com.squareup.picasso.Picasso
import com.example.book_hub.database.BookEntity



class FavRecyclerAdaptor(val context : Context, private val bookList : List<BookEntity>) :
        RecyclerView.Adapter<FavRecyclerAdaptor.FavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_fav_single_row, parent ,false)

        return FavViewHolder(view)

    }

    override fun getItemCount(): Int {

        return bookList.size
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {

        val book = bookList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.book_app_icon_web).into(holder.imgBookImage)
    }


    class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtBookName: TextView = view.findViewById(R.id.txtFavBookTitle)
        val txtBookAuthor: TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtBookRating: TextView = view.findViewById(R.id.txtFavBookRating)
        val txtBookPrice: TextView = view.findViewById(R.id.txtFavBookPrice)
        val imgBookImage: ImageView = view.findViewById(R.id.imgFavBookImage)
        val llContent: LinearLayout = view.findViewById(R.id.llFavContent)
    }
}