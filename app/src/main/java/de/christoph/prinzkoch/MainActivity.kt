package de.christoph.prinzkoch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import de.christoph.prinzkoch.constants.Constants
import de.christoph.prinzkoch.dialogs.SearchDialog
import de.christoph.prinzkoch.firebase.FirestoreClass
import de.christoph.prinzkoch.models.Recipe
import de.christoph.prinzkoch.recyclerview.MainRecyclerviewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, SearchDialog.SearchDialogListener {

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_search) {
            openSearchDialog()
        }
        return true
    }

    private fun openSearchDialog() {
        val searchDialog:SearchDialog = SearchDialog()
        searchDialog.show(supportFragmentManager, "search dialog")
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
        Log.d("dsafadsf", loadedRecipes.toString())
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

    override fun search(searchWord: String) {
        Log.d("s", searchWord)
        var newRecipeList:ArrayList<Recipe> = ArrayList()
        for(i in allRecipes) {
            if(i.nameOfRecipe.toLowerCase().contains(searchWord.toLowerCase()) || i.shortDescription.toLowerCase().contains(searchWord.toLowerCase()) || i.category.toLowerCase().contains(searchWord.toLowerCase()) || i.creatorName.toLowerCase().contains(searchWord.toLowerCase()))
                newRecipeList.add(i)
            else if(searchWord.toLowerCase().contains("mittag") && i.category == "lunch")
                newRecipeList.add(i)
            else if(searchWord.toLowerCase().contains("früh") || searchWord.toLowerCase().contains("frueh") && i.category == "breakfast")
                newRecipeList.add(i)
            else if(searchWord.toLowerCase().contains("snack") && i.category == "snacks")
                newRecipeList.add(i)
            else if(searchWord.toLowerCase().contains("abend") && i.category == "dinner")
                newRecipeList.add(i)
        }
        loadedAllRecipes(newRecipeList)
    }

    override fun reset() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().loadAllRecipesToMainActivity(this)
    }


}