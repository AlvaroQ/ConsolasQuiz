package com.quiz.videoconsolas.ui.info

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quiz.domain.Console
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.common.inflate
import com.quiz.videoconsolas.utils.glideLoadURL
import java.text.NumberFormat
import java.util.*

class InfoListAdapter(
    val context: Context,
    var infoList: MutableList<Console>) : RecyclerView.Adapter<InfoListAdapter.InfoListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoListViewHolder {
        val view = parent.inflate(R.layout.item_info, false)
        return InfoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoListViewHolder, position: Int) {
        val console = infoList[position]

        glideLoadURL(context,  console.image, holder.flagImage)
        holder.nameText.text = console.name

        val descriptionLocalize = when {
            context.getString(R.string.locale) == "es" -> console.description?.ES
            context.getString(R.string.locale) == "fr" -> console.description?.FR
            context.getString(R.string.locale) == "pt" -> console.description?.PT
            context.getString(R.string.locale) == "de" -> console.description?.DE
            context.getString(R.string.locale) == "it" -> console.description?.IT
            else -> console.description?.EN
        }
        holder.descriptionText.text = descriptionLocalize

        holder.genText.text = context.getString(R.string.generation, console.generation)
        holder.yearText.text = context.getString(R.string.year, NumberFormat.getNumberInstance(Locale.US).format(console.year))

        if(console.sold != 0) {
            holder.soldText.visibility = View.VISIBLE
            holder.soldText.text = context.getString(R.string.sold, NumberFormat.getNumberInstance(Locale.US).format(console.sold))
        }
    }

    override fun getItemCount(): Int {
        return infoList.size
    }

    fun getItem(position: Int): Console {
        return infoList[position]
    }

    fun update(modelList: MutableList<Console>){
        infoList = modelList
    }

    class InfoListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameText: TextView = view.findViewById(R.id.nameText)
        var flagImage: ImageView = view.findViewById(R.id.flagImage)
        var descriptionText: TextView = view.findViewById(R.id.descriptionText)

        var genText: TextView = view.findViewById(R.id.genText)
        var yearText: TextView = view.findViewById(R.id.yearText)
        var soldText: TextView = view.findViewById(R.id.soldText)
    }
}