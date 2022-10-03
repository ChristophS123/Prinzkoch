package de.christoph.prinzkoch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_logged_in_main.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_logged_in.*
import kotlinx.android.synthetic.main.app_bar_main.*

class LoggedInMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in_main)
        setUpActionBar()
        nav_view_logged_in.setNavigationItemSelectedListener(this)
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_logged_in_activity)
        toolbar_logged_in_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar_logged_in_activity.title = resources.getString(R.string.register)
        toolbar_logged_in_activity.setNavigationOnClickListener {
            onNavigationClick()
        }
    }

    private fun onNavigationClick() {
        toggleDrawer()
    }

    override fun onBackPressed() {
        if(logged_in_drawer_layout.isDrawerOpen(GravityCompat.START))
            logged_in_drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun toggleDrawer() {
        if(logged_in_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            logged_in_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            logged_in_drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.log_out -> {
                Firebase.auth.signOut()
                startActivity(Intent(this@LoggedInMainActivity, MainActivity::class.java))
            }
        }
        return true
    }

}