package com.ikhut.todo.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikhut.todo.R
import com.ikhut.todo.data.ToDoEntity
import com.ikhut.todo.data.ToDoItem
import com.ikhut.todo.databinding.ToDoItemActivityBinding

class ToDoItemActivity : AppCompatActivity() {
    private var _binding: ToDoItemActivityBinding? = null
    private val binding get() = _binding!!
    private var _toDoItemEntity: ToDoEntity? = null
    private val toDoItemEntity get() = _toDoItemEntity!!

    private lateinit var uncheckedAdapter: ToDoItemAdapter
    private lateinit var checkedAdapter: ToDoItemAdapter

    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ToDoItemActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getItem()

        title = toDoItemEntity.name
        binding.titleTextView.setText(title)
        binding.pinButton.isSelected = toDoItemEntity.pinned
        updatePinButtonIcon()

        setupAdapters()
        addListeners()
    }

    private fun getItem() {
        _toDoItemEntity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("item", ToDoEntity::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getParcelableExtra("item")
        }
    }

    private fun updatePinButtonIcon() {
        if (binding.pinButton.isSelected) {
            binding.pinButton.setImageResource(R.drawable.ic_pinned)
        } else {
            binding.pinButton.setImageResource(R.drawable.ic_pin)
        }
    }

    private fun setupAdapters() {
        val uncheckedItems = toDoItemEntity.items.filter { !it.isChecked }.toMutableList()
        val checkedItems = toDoItemEntity.items.filter { it.isChecked }.toMutableList()

        uncheckedAdapter =
            ToDoItemAdapter(items = uncheckedItems, onCheckChanged = { index, item, isChecked ->
                binding.root.post {
                    if (isChecked) {
                        moveItemToChecked(index, item)
                    }
                }
            })

        checkedAdapter =
            ToDoItemAdapter(items = checkedItems, onCheckChanged = { index, item, isChecked ->
                item.isChecked = isChecked
                binding.root.post {
                    if (!isChecked) {
                        moveItemToUnchecked(index, item)
                    }
                }
            })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = uncheckedAdapter

        binding.checkedRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.checkedRecyclerView.adapter = checkedAdapter

        val spacing = resources.getDimensionPixelSize(R.dimen.to_do_item_spacing)
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(0, spacing))
        binding.checkedRecyclerView.addItemDecoration(SpacingItemDecoration(0, spacing))

        updateDividerVisibility()
    }

    private fun updateDividerVisibility() {
        binding.dividerLine.visibility = if (checkedAdapter.itemCount > 0) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun addListeners() {
        binding.backButton.setOnClickListener {
            saveAndFinish()
        }

        binding.pinButton.setOnClickListener {
            binding.pinButton.isSelected = !binding.pinButton.isSelected
            updatePinButtonIcon()
        }

        binding.addItemLayout.setOnClickListener {
            val newItem = ToDoItem("", false)
            uncheckedAdapter.addItem(newItem)
            binding.root.post {
                val viewHolder =
                    binding.recyclerView.findViewHolderForAdapterPosition(uncheckedAdapter.itemCount - 1) as? ToDoItemAdapter.ViewHolder
                viewHolder?.binding?.todoText?.requestFocus()
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            saveAndFinish()
        }

        binding.titleTextView.addTextChangedListener { text ->
            title = text.toString()
        }
    }

    private fun moveItemToChecked(index: Int, item: ToDoItem) {
        uncheckedAdapter.removeItem(index)
        checkedAdapter.addItem(item)
        updateDividerVisibility()
    }

    private fun moveItemToUnchecked(index: Int, item: ToDoItem) {
        checkedAdapter.removeItem(index)
        uncheckedAdapter.addItem(item)
        updateDividerVisibility()
    }

    private fun saveAndFinish() {
        val items = mutableListOf<ToDoItem>()
        items.addAll(uncheckedAdapter.getItems())
        items.addAll(checkedAdapter.getItems())

        val updatedItem = toDoItemEntity.copy(
            name = title,
            pinned = binding.pinButton.isSelected,
            items = items,
            time = System.currentTimeMillis()
        )

        val resultIntent = Intent()
        resultIntent.putExtra("item", updatedItem)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}