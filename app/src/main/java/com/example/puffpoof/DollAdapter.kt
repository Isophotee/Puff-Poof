package com.example.puffpoof

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class DollAdapter(
    private val dolls: List<Doll>,
    private val onClick: (Doll) -> Unit
) : RecyclerView.Adapter<DollAdapter.DollViewHolder>() {

    inner class DollViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val textViewName: TextView = view.findViewById(R.id.textViewName)
        private val textViewPrice: TextView = view.findViewById(R.id.textViewPrice)

        fun bind(doll: Doll) {
            textViewName.text = doll.name
            textViewPrice.text = "${doll.price}"
            Glide.with(itemView.context).load(doll.image).into(imageView)

            itemView.setOnClickListener {
                onClick(doll)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DollViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doll, parent, false)
        return DollViewHolder(view)
    }

    override fun onBindViewHolder(holder: DollViewHolder, position: Int) {
        holder.bind(dolls[position])
    }

    override fun getItemCount(): Int = dolls.size
}
