package com.example.book_hub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.book_hub.R
//import com.example.book_hub.activity.bookId
import com.example.book_hub.adaptor.DashboardRecyclerAdaptor
import com.example.book_hub.model.Book
import com.example.book_hub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
//    lateinit var btnCheckInternet  : Button
    lateinit var recyclerAdaptor : DashboardRecyclerAdaptor
    lateinit var progressLayout : RelativeLayout
//    lateinit var progressBar : ProgressBar
    val bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book> { book1, book2 ->
       if ( book1.bookRating.compareTo(book2.bookRating, true) == 0){
           book1.bookName.compareTo(book2.bookName, true)
       }else{
           book1.bookRating.compareTo(book2.bookRating, true)
       }
    }

    override fun onCreateView
            (inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?):
            View? {

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
//        btnCheckInternet = view.findViewById(R.id.btnCheckInternet)

        progressLayout = view.findViewById(R.id.progressLayout)

//        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjRequest = object : JsonObjectRequest(Request.Method.GET, url,
                    null, Response.Listener {

                try {

                    progressLayout.visibility = View.GONE
                val success = it.getBoolean("success")
                if (success) {

                    val data = it.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val bookJsonObject = data.getJSONObject(i)
                        val bookObj = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                        )
                        bookInfoList.add(bookObj)

                        recyclerAdaptor = DashboardRecyclerAdaptor(activity as Context, bookInfoList)

                        recyclerDashboard.adapter = recyclerAdaptor

                        recyclerDashboard.layoutManager = layoutManager

//                        recyclerDashboard.addItemDecoration(
//                                DividerItemDecoration(
//                                        recyclerDashboard.context,
//                                        (layoutManager as LinearLayoutManager).orientation
//                                )
//                        )
                    }

                } else {
                    Toast.makeText(activity as Context, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
//            println("Response is $it")
            }
                catch (e: JSONException){

                    Toast.makeText(activity as Context, "Some error occurred", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {

                if (activity != null) {
                    Toast.makeText(activity as Context, "Volley error occurred", Toast.LENGTH_SHORT).show()
//                    println("Error is $it")
                }
            }) {

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "de92a6528d5b09"
                    return headers
                }
            }

            queue.add(jsonObjRequest)
        }
        else{

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Open setting"){ text, listner ->

                val intentSetting = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intentSetting)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text, listner ->

                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)

    }


     override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_sort){
            Collections.sort(bookInfoList, ratingComparator)
            bookInfoList.reverse()
        }
        recyclerAdaptor.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }
}