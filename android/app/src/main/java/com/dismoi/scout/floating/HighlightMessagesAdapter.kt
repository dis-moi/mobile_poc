package com.dismoi.scout.floating

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dismoi.scout.R

class HighlightMessagesAdapter(
  private val highlightMessagesList: List<HighlightMessage>,
  private val listener: onItemClickListener

) : RecyclerView.Adapter<HighlightMessagesAdapter.HighlightMessageViewHolder>() {

  inner class HighlightMessageViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val scoutNameTextView: TextView = itemView.findViewById(R.id.scout_name)
    val scoutSliceContentTextView: TextView = itemView.findViewById(R.id.scout_slice_content)

    init {
      itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
      val position: Int = bindingAdapterPosition
      if (position != RecyclerView.NO_POSITION) {
        listener.onItemClick(position)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightMessageViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(
      R.layout.highlight_message, parent, false
    )

    return HighlightMessageViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: HighlightMessageViewHolder, position: Int) {
    val currentItem = highlightMessagesList[position]

    holder.scoutNameTextView.text = currentItem.scoutName
    holder.scoutSliceContentTextView.text = currentItem.scoutHighlightMessage
  }

  override fun getItemCount() = highlightMessagesList.size

  interface onItemClickListener {
    fun onItemClick(position: Int)
  }
}
