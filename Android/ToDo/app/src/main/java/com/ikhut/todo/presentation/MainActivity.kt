package com.ikhut.todo.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ikhut.todo.R
import com.ikhut.todo.data.ToDoDatabase
import com.ikhut.todo.data.ToDoEntity
import com.ikhut.todo.databinding.ActivityMainBinding
import com.ikhut.todo.domain.ToDoRepository

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var pinnedAdapter: ToDoListAdapter
    private lateinit var adapter: ToDoListAdapter

    private val addItemLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra("item", ToDoEntity::class.java)
            } else {
                @Suppress("DEPRECATION") result.data?.getParcelableExtra("item")
            }

            newItem?.let {
                viewModel.insertTodo(it)
            }
        }
    }

    private val changeItemLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra("item", ToDoEntity::class.java)
            } else {
                @Suppress("DEPRECATION") result.data?.getParcelableExtra("item")
            }

            newItem?.let {
                viewModel.updateTodo(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = ToDoDatabase.getInstance(applicationContext)
        val dao = database.toDoDao()
        val repository = ToDoRepository(dao)

        viewModel = ViewModelProvider(
            this, MainViewModel.create(repository)
        )[MainViewModel::class.java]

        setupAdapters()
        addListeners()
        addObserver()
    }

    private fun setupAdapters() {
        pinnedAdapter = ToDoListAdapter { item ->
            val intent = Intent(this, ToDoItemActivity::class.java)
            intent.putExtra("item", item)
            changeItemLauncher.launch(intent)
        }

        adapter = ToDoListAdapter { item ->
            val intent = Intent(this, ToDoItemActivity::class.java)
            intent.putExtra("item", item)
            changeItemLauncher.launch(intent)
        }

        binding.pinnedRecyclerView.layoutManager = StaggeredGridLayoutManager(
            2, StaggeredGridLayoutManager.VERTICAL
        )
        binding.pinnedRecyclerView.adapter = pinnedAdapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(
            2, StaggeredGridLayoutManager.VERTICAL
        )
        binding.recyclerView.adapter = adapter

        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.view_margin)
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.vertical_spacing)

        binding.pinnedRecyclerView.addItemDecoration(
            SpacingItemDecoration(horizontalSpacing, verticalSpacing)
        )

        binding.recyclerView.addItemDecoration(
            SpacingItemDecoration(horizontalSpacing, verticalSpacing)
        )
    }

    private fun addListeners() {
        binding.editText.addTextChangedListener { text ->
            val query = text.toString().lowercase()

            val allItems = viewModel.todoList.value.orEmpty()

            val pinnedItems = allItems.filter { it.pinned && it.name.lowercase().contains(query) }
            val unpinnedItems =
                allItems.filter { !it.pinned && it.name.lowercase().contains(query) }

            pinnedAdapter.setItems(pinnedItems)
            adapter.setItems(unpinnedItems)

            changeVisibilityTo(if (pinnedItems.isNotEmpty()) View.VISIBLE else View.GONE)
        }

        binding.addTodoButton.setOnClickListener {
            val newItem = ToDoEntity(0, "", false, listOf(), 1)
            val intent = Intent(this, ToDoItemActivity::class.java)
            intent.putExtra("item", newItem)
            addItemLauncher.launch(intent)
        }
    }

    private fun addObserver() {
        viewModel.todoList.observe(this) { todoList ->
            val pinnedItems = todoList.filter { it.pinned }
            val unpinnedItems = todoList.filter { !it.pinned }

            pinnedAdapter.setItems(pinnedItems)
            adapter.setItems(unpinnedItems)
            changeVisibilityTo(if (pinnedItems.isNotEmpty()) View.VISIBLE else View.GONE)
        }
    }

    private fun changeVisibilityTo(visibility: Int) {
        binding.pinnedRecyclerView.visibility = visibility
        binding.otherTextView.visibility = visibility
        binding.pinnedTextView.visibility = visibility
    }
}