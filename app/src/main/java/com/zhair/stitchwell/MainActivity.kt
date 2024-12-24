package com.zhair.stitchwell

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        var user: Users? = null
    }
    lateinit var  authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        val headerView = navigationView.getHeaderView(0)

        val userrole = headerView.findViewById<TextView>(R.id.rolee)
        val userName = headerView.findViewById<TextView>(R.id.textView10)

        navigationView.setNavigationItemSelectedListener(this)

        val imageView = findViewById<ImageView>(R.id.drawer_icon)
        imageView.setOnClickListener { view: View? ->
            if (drawer.isDrawerVisible(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }

      authViewModel = AuthViewModel()
        authViewModel.loadUser()

        lifecycleScope.launch {
            authViewModel.currentUser.collect{
                it?.let {
                    user = it
//                    if(user !==null){
                        userrole.text = user!!.role
                        userName.text = user!!.fullName
//                    }
                }
            }
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val bottomNavigationView = findViewById<BottomNavigationView>(
            R.id.bottomNavigation
        )
        bottomNavigationView.setupWithNavController(navHostFragment.navController)


    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val viewModel = AuthViewModel()
        when (item.itemId) {
            R.id.item_logout -> {
                lifecycleScope.launch { // Launch a coroutine
                    viewModel.Logout()
                    startActivity(Intent(this@MainActivity, Login::class.java))
                    finish()
                }
            }
            R.id.item_about -> {
                // Navigate to home screen (replace with your actual navigation logic)
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
            }
            // Add cases for other navigation items if needed
        }
        return true
    }


}