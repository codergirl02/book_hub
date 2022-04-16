package com.example.book_hub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.Response
import com.example.book_hub.R
import com.example.book_hub.util.ConnectionManager
import com.squareup.picasso.Picasso
import com.example.book_hub.database.BookDatabase
import com.example.book_hub.database.BookEntity
import org.json.JSONObject
import java.lang.Exception

class DiscriptionActivity : AppCompatActivity() {

lateinit var txtBookName : TextView
lateinit var txtBookAuthor : TextView
lateinit var txtBookPrice : TextView
lateinit var txtBookRating : TextView
lateinit var imgBookImage : ImageView
lateinit var txtBookDesc : TextView
lateinit var btnAddToFav : Button
lateinit var progressBar : ProgressBar
lateinit var progressLayout :RelativeLayout

lateinit var toolbar : Toolbar

var bookId : String? = "100"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discription)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookRating = findViewById(R.id.txtBookRating)
        txtBookPrice = findViewById(R.id.txtBookprice)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        imgBookImage = findViewById(R.id.imgBookImage)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"


        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(this@DiscriptionActivity, "some error occurred", Toast.LENGTH_SHORT).show()
        }

        if (bookId == "100") {
            finish()
            Toast.makeText(this@DiscriptionActivity, "some error occurred", Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this@DiscriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this@DiscriptionActivity)) {


        val jsonReq = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

            try {
                val success = it.getBoolean("success")
                if (success) {
                    val bookJsonObj = it.getJSONObject("book_data")
                    progressLayout.visibility = View.GONE

                    val bookImageUrl = bookJsonObj.getString("image")

                    Picasso.get().load(bookJsonObj.getString("image"))
                            .error(R.drawable.book_app_icon_web).into(imgBookImage)
                    txtBookName.text = bookJsonObj.getString("name")
                    txtBookAuthor.text = bookJsonObj.getString("author")
                    txtBookPrice.text = bookJsonObj.getString("price")
                    txtBookRating.text = bookJsonObj.getString("rating")
                    txtBookDesc.text = bookJsonObj.getString("description")

                    val bookEntity = BookEntity(

                            bookId?.toInt() as Int,
                            txtBookName.text.toString(),
                            txtBookAuthor.text.toString(),
                            txtBookPrice.text.toString(),
                            txtBookRating.text.toString(),
                            txtBookDesc.text.toString(),
                            bookImageUrl)

                    val checkFav =
                            DBAsync(applicationContext, bookEntity, 1).execute()

                    val isFav = checkFav.get()

                    if (isFav){
                        btnAddToFav.text = "Remove From Favourites"
                        val favColor = ContextCompat.getColor(applicationContext, R.color.fav_color)
                        btnAddToFav.setBackgroundColor(favColor)

                    }else{

                        btnAddToFav.text = "Add to Favourites"
                        val noFavColor = ContextCompat.getColor(applicationContext, R.color.teal_200)
                        btnAddToFav.setBackgroundColor(noFavColor)

                    }

                    btnAddToFav.setOnClickListener {

                        if (!DBAsync(applicationContext, bookEntity, 1).execute().get()){


                        val async=
                                DBAsync(applicationContext, bookEntity, 2).execute()
                        val result = async.get()

                        if (result){
                            Toast.makeText(this@DiscriptionActivity, "Book added to favourite", Toast.LENGTH_SHORT).show()


                            btnAddToFav.text = "Remove from favourite"
                            val favColor = ContextCompat.getColor(applicationContext, R.color.fav_color)
                            btnAddToFav.setBackgroundColor(favColor)

                        }
                        else {
                            Toast.makeText(this@DiscriptionActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                        }
                        }else{
                            val async = DBAsync(applicationContext, bookEntity, 3).execute()
                            val result = async.get()

                            if (result){
                                Toast.makeText(this@DiscriptionActivity, "Book removed from favourite", Toast.LENGTH_SHORT).show()


                                btnAddToFav.text = "Add to favourite"
                                val noFavColor = ContextCompat.getColor(applicationContext, R.color.teal_200)
                                btnAddToFav.setBackgroundColor(noFavColor)

                            }
                            else{
                                Toast.makeText(this@DiscriptionActivity, "Some error occurred", Toast.LENGTH_SHORT).show()

                            }
                        }
                    }

                } else {

                    Toast.makeText(this@DiscriptionActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {

                Toast.makeText(this@DiscriptionActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
            }
        },
              Response.ErrorListener {

            Toast.makeText(this@DiscriptionActivity, "Volley error $it", Toast.LENGTH_SHORT).show()

        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "de92a6528d5b09"
                return headers
            }
        }

        queue.add(jsonReq)
    }else{
            val dialog = AlertDialog.Builder(this@DiscriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Open setting"){ text, listner ->

                val intentSetting = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intentSetting)
                finish()
            }
            dialog.setNegativeButton("Exit"){text, listner ->

                ActivityCompat.finishAffinity(this@DiscriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    class DBAsync(val context: Context, val bookEntity: BookEntity, val mode : Int) : AsyncTask<Void, Void, Boolean>(){

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){
                1 ->{
                    //check add to fav or not
                    val book : BookEntity? = db.bookDao().getBookById(bookEntity.bookId.toString())
                    db.close()

                    return book != null
                }
                2 -> {
                    //save book in db as fav
                    db.bookDao().insertBook(bookEntity)
                    db.close()

                    return true
                }
                3 -> {
                    // remove fav book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }

            return false
        }

    }
}