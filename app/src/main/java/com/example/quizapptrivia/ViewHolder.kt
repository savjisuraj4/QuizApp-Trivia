package com.example.quizapptrivia

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(mView: View):RecyclerView.ViewHolder(mView) {
    var categoryName:TextView?=null
    private var mClickListener:ClickListener?=null

    init {
        categoryName=itemView.findViewById(R.id.categoryName)
        itemView.setOnClickListener { view->
            mClickListener!!.setOnItemClick(view,adapterPosition)

        }
    }
    interface ClickListener {
        fun setOnItemClick(view: View, position:Int)
    }
    fun setOnClickListener(clickListener: ClickListener){
        mClickListener=clickListener
    }
}