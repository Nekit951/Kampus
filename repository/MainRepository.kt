package com.example.kampus2.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kampus2.model.Category
import com.example.kampus2.model.ItemsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainRepository {
    private val firebaseDatabase= FirebaseDatabase.getInstance()

    private val _categories= MutableLiveData< MutableList<Category>>()
    private val _items = MutableLiveData<MutableList<ItemsModel>>()

    val categories: LiveData<MutableList<Category>> get() = _categories
    val items: LiveData<MutableList<ItemsModel>> get() = _items

    fun loadCategory(){
        val ref = firebaseDatabase.getReference("Category")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Category>()
                for(child in snapshot.children){
                    child.getValue(Category::class.java)?.let{
                        list.add(it)
                    }
                }
                _categories.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                _categories.value = mutableListOf()
            }
        })
    }

    fun loadItem(){
        val ref = firebaseDatabase.getReference("Items")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("FIREBASE_DEBUG", "Данные обновились, пришло элементов: ${snapshot.childrenCount}")
                val list = mutableListOf<ItemsModel>()
                for(child in snapshot.children){
                    val item = child.getValue(ItemsModel::class.java)
                    item?.let{
                        it.id = child.key?.toInt() ?: 1
                        list.add(it)
                    }
                }
                _items.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FIREBASE_DEBUG", "Ошибка: ${error.message}")
                _items.value = mutableListOf()
            }
        })
    }

}