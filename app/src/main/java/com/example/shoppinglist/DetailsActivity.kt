package com.example.shoppinglist

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext

import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.Item
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.example.shoppinglist.databinding.ShoppingItemDetailsBinding

class DetailsActivity(val position:Int): AppCompatActivity()
{

    private lateinit var binding: ShoppingItemDetailsBinding
    private val items = AppDatabase.getInstance(context = MainActivity()).itemDao().getAllItems()
    private val selectedItem = items[position]
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ShoppingItemDetailsBinding.inflate(layoutInflater)
        binding.tvName.text = selectedItem.name
        binding.tvDesc.text = selectedItem.desc
        binding.tvPrice.text = selectedItem.price.toString()
        binding.tvINRPrice.text = selectedItem.currencies[1].toString()
        binding.tvUSDPrice.text = selectedItem.currencies[0].toString()
        binding.tvRUBPrice.text = selectedItem.currencies[2].toString()
        if(selectedItem.done)
        {
            binding.tvStatus.text = "Bought"
        }
        else
        {
            binding.tvStatus.text = "To Buy"
        }
        if(selectedItem.category == 0)
        {
            binding.icCategory.setImageResource(R.mipmap.ic_foodicon)
        }
        else if(selectedItem.category == 1)
        {
            binding.icCategory.setImageResource(R.mipmap.ic_homeicon)
        }
        else if(selectedItem.category == 2)
        {
            binding.icCategory.setImageResource(R.mipmap.ic_personalicon)
        }
        setContentView(binding.root)
        binding.btnOK.setOnClickListener {
             var mainIntent = Intent()
            mainIntent.setClass(this,MainActivity::class.java)
            startActivity(mainIntent)
        }
    }

}