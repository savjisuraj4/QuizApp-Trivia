package com.example.quizapptrivia

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class RecycleviewAdapter(mainActivity: MainActivity,categoryList:ArrayList<CategoryInfo>): RecyclerView.Adapter<ViewHolder>() {
    private var categoryList:ArrayList<CategoryInfo>
    var mainActivity: MainActivity
    private lateinit var viewHolder: ViewHolder
    init {
        this.categoryList=categoryList
        this.mainActivity=mainActivity
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemVew: View =LayoutInflater.from(parent.context).inflate(R.layout.categorylistview,parent,false)
        viewHolder= ViewHolder(itemVew)
        viewHolder.categoryName?.minHeight=(parent.height*0.2).toInt()
        viewHolder.setOnClickListener(object :ViewHolder.ClickListener{
            override fun setOnItemClick(view: View, position: Int) {
                mainActivity.levelDisplay(position)
            }

        })
        return viewHolder
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        
        holder.categoryName?.text=categoryList[position].category
        if (position % 6 == 0) {
            holder.categoryName?.setBackgroundColor(Color.RED)
        } else if (position % 6 == 1) {
            holder.categoryName?.setBackgroundColor(Color.BLUE)
        } else if (position % 6 == 2) {
            holder.categoryName?.setBackgroundColor(Color.rgb(0,255,0))
        } else if (position % 6 == 3) {
            holder.categoryName?.setBackgroundColor(Color.GRAY)
        } else if (position % 6 == 4) {
            holder.categoryName?.setBackgroundColor(Color.MAGENTA)
        } else{
            holder.categoryName?.setBackgroundColor(Color.parseColor("#FF03DAC5"))
        }


    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}