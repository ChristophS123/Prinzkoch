package de.christoph.prinzkoch.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.christoph.prinzkoch.R
import de.christoph.prinzkoch.constants.Constants
import de.christoph.prinzkoch.models.Recipe
import kotlinx.android.synthetic.main.item_recipe.view.*
import kotlinx.android.synthetic.main.nav_header_logged_in.*

class MainRecyclerviewAdapter(private val context: Context, private var recipes:ArrayList<Recipe>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener:OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate (
                R.layout.item_recipe,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model:Recipe = recipes[position]
        if(holder is MyViewHolder) {
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_recipe_place_holder)
                .into(holder.itemView.iv_item_recipe)
            holder.itemView.tv_item_recipe_name_of_recipe.text = model.nameOfRecipe
            holder.itemView.tv_item_recipe_short_description.text = model.shortDescription
            Glide
                .with(context)
                .load(model.creatorImage)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.itemView.iv_item_recipe_user_image)
            holder.itemView.tv_item_recipe_user_name.text = model.creatorName
            when(model.category) {
                Constants.BREAKFAST -> holder.itemView.tv_item_recipe_category.text = "Frühstück"
                Constants.LUNCH -> holder.itemView.tv_item_recipe_category.text = "Mittagessen"
                Constants.DINER -> holder.itemView.tv_item_recipe_category.text = "Abendessem"
                Constants.SNACKS -> holder.itemView.tv_item_recipe_category.text = "Snacks"
            }
            holder.itemView.setOnClickListener {
                if(onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position:Int, model: Recipe) {

        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

}