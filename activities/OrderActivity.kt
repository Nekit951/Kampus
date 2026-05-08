package com.example.kampus2.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kampus2.R
import com.example.kampus2.adapters.OrderAdapter
import com.example.kampus2.databinding.ActivityOrderBinding
import com.example.kampus2.helpers.ManagmentCart
import com.example.kampus2.helpers.ManagmentOrder
import com.example.kampus2.model.ItemsModel


class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var managmentCart: ManagmentCart
    private lateinit var managmentOrder: ManagmentOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        managmentCart = ManagmentCart(this)
        managmentOrder = ManagmentOrder(this)

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        initOrder()
        bottom()
    }

    private fun initOrder() {
        val orderList = managmentOrder.getListOrder()

        if(orderList.isEmpty()){
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.recyclerViewOrder.visibility = View.GONE
        }
        else{
            binding.emptyStateLayout.visibility = View.GONE
            binding.recyclerViewOrder.visibility = View.VISIBLE

            binding.recyclerViewOrder.adapter = OrderAdapter(orderList){ selectedOrder, position ->
                val intent = Intent(this, OrderDetailActivity::class.java)
                intent.putExtra("ITEMS_LIST", selectedOrder.items)
                intent.putExtra("FINAL_PRICE", selectedOrder.summa)
                intent.putExtra("USER_ADDRESS", selectedOrder.address)
                intent.putExtra("PAY_METHOD", selectedOrder.payMethod)
                intent.putExtra("ORDER_POSITION", position)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initOrder()
    }


    private fun bottom(){
        binding.bottomNav.setOnItemSelectedListener{ menuItem ->
            when(menuItem.itemId){
                R.id.nav_order -> true

                R.id.nav_home -> {
                    if(this !is MainActivity){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_cart -> {
                    if(this !is CartActivity){
                        val intent = Intent(this, CartActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_profile -> {
                    if(this !is ProfileActivity){
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                else -> false
            }
        }
    }
}