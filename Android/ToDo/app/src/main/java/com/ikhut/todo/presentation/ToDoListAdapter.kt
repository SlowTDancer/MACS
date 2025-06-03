package com.ikhut.todo.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikhut.todo.data.ToDoEntity
import com.ikhut.todo.databinding.ToDoListItemBinding

class ToDoListAdapter(
    private val onItemClick: (ToDoEntity) -> Unit
) : RecyclerView.Adapter<ToDoListAdapter.ToDoViewHolder>() {
    private val items: MutableList<ToDoEntity> = mutableListOf()

    inner class ToDoViewHolder(val binding: ToDoListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = ToDoListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val item = items[position]
        holder.binding.toDoTitle.text = item.name
        clearPreviousData(holder)
        setUpCheckboxes(holder, item)
        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            onItemClick(items[pos])
        }
    }

    private fun clearPreviousData(holder: ToDoViewHolder) {
        holder.binding.toDoCheckbox1.text = ""
        holder.binding.toDoCheckbox2.text = ""
        holder.binding.toDoCheckbox3.text = ""
        holder.binding.itemsInfo.text = ""

        holder.binding.toDoCheckbox1.visibility = View.GONE
        holder.binding.toDoCheckbox2.visibility = View.GONE
        holder.binding.toDoCheckbox3.visibility = View.GONE
        holder.binding.etc.visibility = View.GONE
        holder.binding.itemsInfo.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun setUpCheckboxes(holder: ToDoViewHolder, item: ToDoEntity) {
        val uncheckedItems = item.items.filter { !it.isChecked }
        val checkedItems = item.items.filter { it.isChecked }

        if (uncheckedItems.isNotEmpty()) {
            holder.binding.toDoCheckbox1.text = uncheckedItems[0].name
            holder.binding.toDoCheckbox1.isChecked = uncheckedItems[0].isChecked
            holder.binding.toDoCheckbox1.visibility = View.VISIBLE
        }

        if (uncheckedItems.size >= 2) {
            holder.binding.toDoCheckbox2.text = uncheckedItems[1].name
            holder.binding.toDoCheckbox2.isChecked = uncheckedItems[1].isChecked
            holder.binding.toDoCheckbox2.visibility = View.VISIBLE
        }

        if (uncheckedItems.size >= 3) {
            holder.binding.toDoCheckbox3.text = uncheckedItems[2].name
            holder.binding.toDoCheckbox3.isChecked = uncheckedItems[2].isChecked
            holder.binding.toDoCheckbox3.visibility = View.VISIBLE
        }

        if (uncheckedItems.size > 3) {
            holder.binding.etc.visibility = View.VISIBLE
        }

        if (checkedItems.isNotEmpty()) {
            holder.binding.itemsInfo.visibility = View.VISIBLE
            holder.binding.itemsInfo.text = "+ ${checkedItems.size} checked items"
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<ToDoEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
