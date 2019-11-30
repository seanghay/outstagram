package com.seanghay.outstagram.fragment.choose

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seanghay.outstagram.R
import com.seanghay.outstagram.model.ChooseImageItem
import kotlinx.android.synthetic.main.item_choose.view.*

class ChooseAdapter(var items: List<ChooseImageItem> = emptyList()) :
    RecyclerView.Adapter<ChooseAdapter.ChooseViewHolder>() {

    var itemClickDelegate: ((ChooseImageItem) -> Unit)? = null

    fun updateItems(newItems: List<ChooseImageItem>) {
        val callback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition].id == newItems[newItemPosition].id
            }

            override fun getOldListSize(): Int {
                return items.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newItems[newItemPosition]
            }
        }

        DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        items = newItems
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_choose
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ChooseViewHolder(inflater.inflate(viewType, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ChooseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: AppCompatImageView = itemView.imageView

        init {
            itemView.setOnClickListener {
                itemClickDelegate?.invoke(items[adapterPosition])
            }
        }

        fun bind(item: ChooseImageItem) {
            Glide.with(itemView)
                .load(item.path)
                .thumbnail(.2f)
                .centerCrop()
                .into(imageView)
        }
    }

}