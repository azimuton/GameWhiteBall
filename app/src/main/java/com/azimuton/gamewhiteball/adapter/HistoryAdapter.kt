package com.azimuton.gamewhiteball.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.azimuton.gamewhiteball.R
import com.azimuton.gamewhiteball.database.History
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryAdapter(private val listHistory: List<History>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView : View)  : RecyclerView.ViewHolder(itemView) {

        var id : TextView? = null
        var time : TextView? = null
        var count : TextView? = null
        var image : ImageView? = null

        init{
            id = itemView.findViewById(R.id.tvNumber)
            time = itemView.findViewById(R.id.tvTime)
            count = itemView.findViewById(R.id.tvCount)
            image  = itemView.findViewById(R.id.ivColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id?.text = listHistory[position].id.toString()
        holder.time?.text = listHistory[position].time
        holder.count?.text = " Очки : ${listHistory[position].count}"
        val color = listHistory[position].color
        holder.image?.setBackgroundColor(color.toInt())
    }

}