package com.example.kampus2.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kampus2.viewModel.MainViewModel
import com.example.kampus2.adapters.CategoryAdapter
import com.example.kampus2.adapters.ItemsAdapter
import com.example.kampus2.model.ItemsModel
import com.example.kampus2.R
import com.example.kampus2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy{
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    private var itemsAdapter = ItemsAdapter(mutableListOf())
    private var itemList = mutableListOf<ItemsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settings.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        initUI()
        search()
        bottom()
    }

    private fun initUI() {
        initCategory()
        initItems()
    }

    private fun initCategory() {
        binding.apply{
            val categoryAdapter = CategoryAdapter(mutableListOf()){
                    selectedCategory -> filterItems(selectedCategory)
            }

            recyclerViewCategory.layoutManager= LinearLayoutManager(this@MainActivity,
                LinearLayoutManager.HORIZONTAL, false)

            recyclerViewCategory.adapter = categoryAdapter

            viewModel.categories.observe(this@MainActivity) { data ->
                categoryAdapter.updateData(data)
            }
            viewModel.loadCategories()

        }
    }

    private fun initItems() {
        binding.apply {
            recyclerViewItems.layoutManager = GridLayoutManager(
                this@MainActivity,
                2,
                GridLayoutManager.VERTICAL,
                false
            )

            recyclerViewItems.adapter = itemsAdapter

            viewModel.items.observe(this@MainActivity) { data ->
                itemList = data.toMutableList()
                itemsAdapter.updateDate(ArrayList(data))
            }
            viewModel.loadItem()
        }
    }

    private fun filterItems(category: String){
        val listFilter = itemList
        if(category == "Все"){
            itemsAdapter.updateDate(listFilter)
        }
        else{
            val filter = listFilter.filter {
                it.category == category
            }
            itemsAdapter.updateDate(filter)
        }
    }

    private fun search(){
        binding.searchBar.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun performSearch(query: String?){
        if(query.isNullOrEmpty()){
            itemsAdapter.updateDate(itemList)
        }
        else{
            val filterList = itemList.filter {
                it.title.contains(query, ignoreCase = true)
            }
            itemsAdapter.updateDate(filterList)
        }
    }

    private fun bottom() {
        val sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false)

        binding.bottomNav.setOnItemSelectedListener{ menuItem ->
            when(menuItem.itemId){
                R.id.nav_home -> true

                R.id.nav_cart -> {
                    if(this !is CartActivity){
                        val intent = Intent(this, CartActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_order -> {
                    if(this !is OrderActivity){
                        val intent = Intent(this, OrderActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_profile -> {
                    if(isLoggedIn){
                        if(this !is ProfileActivity){
                            val intent = Intent(this, ProfileActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    else{
                        if(this !is RegisterActivity){
                            val intent = Intent(this, RegisterActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    true
                }

                else -> false
            }
        }
    }

}