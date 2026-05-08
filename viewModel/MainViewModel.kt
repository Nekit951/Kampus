package com.example.kampus2.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.kampus2.model.Category
import com.example.kampus2.model.ItemsModel
import com.example.kampus2.repository.MainRepository

class MainViewModel: ViewModel() {
    private val repository = MainRepository()

    val categories: LiveData<MutableList<Category>> = repository.categories
    val items: LiveData<MutableList<ItemsModel>> = repository.items

    fun loadCategories() = repository.loadCategory()
    fun loadItem() = repository.loadItem()
}