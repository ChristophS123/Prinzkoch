package de.christoph.prinzkoch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_recipe_details.*

class CommentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_comment_activity)
        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_pressed)
            actionBar.title = resources.getString(R.string.comment)
        }
        toolbar_comment_activity.setNavigationOnClickListener {
            finish()
        }
    }

}