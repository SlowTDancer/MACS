package com.ikhut.todo.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.ikhut.todo.data.ToDoItem
import com.ikhut.todo.databinding.ToDoItemBinding

class ToDoItemAdapter(
    private var items: MutableList<ToDoItem>,
    private val onCheckChanged: (Int, ToDoItem, Boolean) -> Unit
) : RecyclerView.Adapter<ToDoItemAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ToDoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ToDoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.todoCheckBox.setOnCheckedChangeListener(null)
        holder.binding.todoCheckBox.isChecked = item.isChecked

        holder.binding.todoCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val pos = holder.adapterPosition
            items[pos].isChecked = isChecked
            onCheckChanged(pos, items[pos], isChecked)
        }

        holder.binding.todoText.addTextChangedListener { text ->
            val pos = holder.adapterPosition
            items[pos].name = text.toString()
        }

        holder.binding.deleteButton.setOnClickListener {
            val pos = holder.adapterPosition
            holder.itemView.rootView.findFocus()?.clearFocus()
            removeItem(pos)
        }

        holder.binding.todoText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.binding.deleteButton.visibility = View.VISIBLE
            } else {
                holder.binding.deleteButton.visibility = View.GONE
            }
        }

        holder.binding.todoText.setText(item.name)
        val isEditable = !item.isChecked
        holder.binding.todoText.isEnabled = isEditable
        holder.binding.todoText.isFocusable = isEditable
        holder.binding.todoText.isFocusableInTouchMode = isEditable

        if (!isEditable) {
            holder.binding.todoText.alpha = 0.6f
        } else {
            holder.binding.todoText.alpha = 1.0f
        }
    }

    fun getItems(): MutableList<ToDoItem> {
        return items
    }

    fun addItem(item: ToDoItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}