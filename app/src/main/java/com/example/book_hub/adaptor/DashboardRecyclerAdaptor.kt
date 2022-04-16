package com.example.book_hub.adaptor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.book_hub.R
import com.example.book_hub.activity.DiscriptionActivity
import com.example.book_hub.model.Book
import com.squareup.picasso.Picasso
import java.util.ArrayList

class DashboardRecyclerAdaptor(val context: Context, val itemList: ArrayList<Book>):
        RecyclerView.Adapter<DashboardRecyclerAdaptor.DashboardViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single,
                parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {

//        val text = itemList[position]
//        holder.textView.text = text.toString()

         val book = itemList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
//        holder.imgbookImage.setBackgroundResource(book.bookImage)

        Picasso.get().load(book.bookImage).error(R.drawable.book_app_icon_web).into(holder.imgBookImage)


        holder.llContent.setOnClickListener{
//            Toast.makeText(context,"Clicked on ${holder.txtbookName.text}", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, DiscriptionActivity::class.java)
            intent.putExtra("book_id", book.bookId)
            context.startActivity(intent)

        }

    }
    class DashboardViewHolder(view:View) : RecyclerView.ViewHolder(view)
    {

    val txtBookName : TextView = view.findViewById(R.id.txtBookName)
    val txtBookAuthor : TextView = view.findViewById(R.id.txtBookAuthor)
    val txtBookPrice : TextView = view.findViewById(R.id.txtBookPrice)
    val txtBookRating : TextView = view.findViewById(R.id.txtBookrating)
    val imgBookImage : ImageView = view.findViewById(R.id.imgBookImage)
    val llContent : LinearLayout = view.findViewById(R.id.llContent)
   }

}