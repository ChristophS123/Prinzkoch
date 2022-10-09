package de.christoph.prinzkoch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.christoph.prinzkoch.constants.Constants
import de.christoph.prinzkoch.firebase.FirestoreClass
import de.christoph.prinzkoch.models.Recipe
import de.christoph.prinzkoch.recyclerview.MainRecyclerviewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_logged_in.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var allRecipes:ArrayList<Recipe> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpActionBar()
        nav_view.setNavigationItemSelectedListener(this)
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().loadAllRecipesToMainActivity(this)
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_main_activity)
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar_main_activity.title = resources.getString(R.string.register)
        toolbar_main_activity.setNavigationOnClickListener {
            onNavigationClick()
        }
    }

    private fun onNavigationClick() {
        toggleDrawer()
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun toggleDrawer() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.log_in -> {
                startActivity(Intent(this, LogInActivity::class.java))
            }
            R.id.register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun loadedAllRecipes(loadedRecipes: ArrayList<Recipe>) {
        hideProgressDialog()
        allRecipes = loadedRecipes
        setUpRecyclerview()
    }

    private fun setUpRecyclerview() {
        if(allRecipes.size > 0) {
            rv_main_activity.visibility = View.VISIBLE
            rv_main_activity.layoutManager = LinearLayoutManager(this)
            rv_main_activity.setHasFixedSize(true)
            val adapter: MainRecyclerviewAdapter = MainRecyclerviewAdapter(this, allRecipes)
            rv_main_activity.adapter = adapter
            adapter.setOnClickListener(object: MainRecyclerviewAdapter.OnClickListener {
                override fun onClick(position: Int, model: Recipe) {
                    var intent:Intent = Intent(this@MainActivity, RecipeDetails::class.java)
                    intent.putExtra(Constants.RECIPE_MODEL, model)
                    startActivity(intent)
                }
            })
        }
    }


}