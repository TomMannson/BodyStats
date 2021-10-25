package com.tommannson.bodystats.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.tommannson.bodystats.databinding.LayoutSavedStatsItemBinding

class MyMessurmentsAdapter : RecyclerView.Adapter<MyMessurmentsAdapter.Holder>() {

    val list: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context);
//        val binding = LayoutSavedStatsItemBinding.inflate(inflater, parent, false)
        return Holder(parent);
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
      return list.size;
    }

    fun setData(data: List<String>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(text: String) {
//            binding.text.text = text
        }
    }
}
