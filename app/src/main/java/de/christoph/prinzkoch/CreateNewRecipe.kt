package de.christoph.prinzkoch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_new_recipe.*
import kotlinx.android.synthetic.main.activity_register.*

class CreateNewRecipe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_recipe)
        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_new_recipe_activity)
        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_pressed)
            actionBar.title = resources.getString(R.string.log_in)
        }
        toolbar_new_recipe_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}