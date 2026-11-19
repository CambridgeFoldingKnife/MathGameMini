package com.drojian.mathgamemini

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LevelAdapter (
    private val levelList: ArrayList<Level>,
    private val openLevel: Int,
    private val listener: OnLevelClick)
    : RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {

    class LevelViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val numberText: TextView = itemView.findViewById(R.id.tv_level_num)
        val nameText : TextView = itemView.findViewById(R.id.tv_level_name)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LevelViewHolder {
       val  view = LayoutInflater.from(parent.context)
           .inflate(R.layout.item_level,parent,false)
        return LevelViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: LevelViewHolder,
        position: Int
    ) {
       val context = holder.itemView.context
       val  level = levelList[position]
        holder.numberText.text = level.id.toString()
        holder.nameText.text = level.name
        if(level.id <= openLevel){
            holder.numberText.setBackgroundResource(R.drawable.level_bg_open)
            holder.nameText.setTextColor(context.getColor(R.color.text_dark) )
        }else{
            holder.numberText.setBackgroundResource(R.drawable.level_bg_off)
            holder.nameText.setTextColor(context.getColor(R.color.text_gray))
        }
        holder.itemView.setOnClickListener {
            listener.onLevelClick(level)
        }
    }

    override fun getItemCount(): Int {
        return levelList.size
    }
    interface OnLevelClick{
        fun onLevelClick(level: Level)
    }



}