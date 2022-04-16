package com.example.book_hub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.book_hub.*
import com.example.book_hub.fragment.AboutAppFragment
import com.example.book_hub.fragment.DashboardFragment
import com.example.book_hub.fragment.FavouriteFragment
import com.example.book_hub.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout : DrawerLayout
    lateinit var coordinateLayout : CoordinatorLayout
    lateinit var frameLayout: FrameLayout
    lateinit var toolbar : Toolbar
    lateinit var navigationView: NavigationView

    var previousMenuItem : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinateLayout = findViewById(R.id.coordinateLayout)
        frameLayout = findViewById(R.id.frame)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationBar)

        setUpToolbar()

        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout,
                R.string.open_drawer, R.string.close_drawer)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId){
                
                R.id.dashboard -> {

                    openDashboard()
//
                    drawerLayout.closeDrawers()
                }
                R.id.favourite -> {
//                    Toast.makeText(this@MainActivity, "clicked on favourite", Toast.LENGTH_SHORT).show()

                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frame, FavouriteFragment())
                        .commit()

                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }

                R.id.profile -> {
//                    Toast.makeText(this@MainActivity, "clicked on profile", Toast.LENGTH_SHORT).show()

                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frame, ProfileFragment())
//                        .addToBackStack("Profile")
                        .commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.about_app -> {
//                    Toast.makeText(this@MainActivity, "clicked on about app", Toast.LENGTH_SHORT).show()

                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frame, AboutAppFragment())
//                        .addToBackStack("About App")
                        .commit()

                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

     fun setUpToolbar(){

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard(){

//        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, DashboardFragment())
        transaction.commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)

    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when(frag){

            !is DashboardFragment -> openDashboard()

            else -> super.onBackPressed()

        }
    }
}