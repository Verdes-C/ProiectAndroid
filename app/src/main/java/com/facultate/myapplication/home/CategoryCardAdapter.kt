package com.facultate.myapplication.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.R

class CategoryCardAdapter(private val categoriesList:ArrayList<HomeFragment.Category>):RecyclerView.Adapter<CategoryCardAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoryCardAdapter.MyViewHolder, position: Int) {
        val currentItem = categoriesList[position]

        holder.categoryName.text = currentItem.categoryName
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryCardAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_categories,parent,false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val categoryName = itemView.findViewById<TextView>(R.id.text_view_category_name)

    }


}