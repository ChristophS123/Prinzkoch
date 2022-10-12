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
import kotlinx.android.synthetic.main.item_my_recipe.view.*
import kotlinx.android.synthetic.main.item_recipe.view.*

class MyRecipeAdapter(private val context: Context, private var recipes:ArrayList<Recipe>)  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: MyRecipeAdapter.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyRecipeAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_my_recipe,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model:Recipe = recipes[position]
        if(holder is MyViewHolder) {
            holder.itemView.tv_my_recipe_name_of_recipe.text = model.nameOfRecipe
            holder.itemView.tv_my_recipe_short_description.text = model.shortDescription
            when(model.category) {
                Constants.BREAKFAST -> holder.itemView.tv_my_recipe_category.text = "Frühstück"
                Constants.LUNCH -> holder.itemView.tv_my_recipe_category.text = "Mittagessen"
                Constants.DINER -> holder.itemView.tv_my_recipe_category.text = "Abendessem"
                Constants.SNACKS -> holder.itemView.tv_my_recipe_category.text = "Snacks"
            }
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_recipe_place_holder)
                .into(holder.itemView.iv_my_recipe_recipe)
            holder.itemView.cmd_my_recipe_delete.setOnClickListener {
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