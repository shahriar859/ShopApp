package com.shahriar.shop.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shahriar.shop.R

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnApplyWindowInsetsListener(null)
        bottomNav.setPadding(0, 0, 0, 0)

        val homeFragment: Fragment = HomeFragment()
        val exploreFragment: Fragment = ExploreFragment()
        val favouriteFragment: Fragment = FavouriteFragment()
        val ordersFragment: Fragment = OrdersFragment()

        var activeFragment: Fragment = homeFragment

        supportFragmentManager.beginTransaction().apply {
            add(R.id.content, homeFragment)
            add(R.id.content, exploreFragment)
            add(R.id.content, favouriteFragment)
            add(R.id.content, ordersFragment)
            hide(exploreFragment)
            hide(favouriteFragment)
            hide(ordersFragment)
            commit()
        }

        bottomNav.setOnItemSelectedListener { item ->
            val newFragment =
                when (item.itemId) {
                    R.id.home -> homeFragment
                    R.id.explore -> exploreFragment
                    R.id.favourite -> favouriteFragment
                    R.id.orders -> ordersFragment
                    else -> return@setOnItemSelectedListener false
                }
            if (activeFragment != newFragment) {
                supportFragmentManager.beginTransaction().apply {
                    hide(activeFragment)
                    show(newFragment)
                    commit()
                }
                activeFragment = newFragment
            }
            else{
                Toast.makeText(this,"clicked the same button!", Toast.LENGTH_SHORT).show()
            }
            true
        }

    }
}