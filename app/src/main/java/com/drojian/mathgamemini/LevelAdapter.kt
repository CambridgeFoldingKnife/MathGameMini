package com.drojian.mathgamemini

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

        //通关后的皇冠图案。
        val corwnImage: ImageView = itemView.findViewById(R.id.iv_level_crown)
        val numOnCorwn: TextView = itemView.findViewById(R.id.tv_num_on_crown)
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

        //小于说明这一关已经通过，可以加上皇冠。
        if (level.id<openLevel){
            holder.corwnImage.visibility = View.VISIBLE
            holder.numOnCorwn.visibility = View.VISIBLE
        }else{
            holder.corwnImage.visibility = View.INVISIBLE
            holder.numOnCorwn.visibility = View.INVISIBLE
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