package com.example.shoppinglist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent

import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.Item


import com.example.shoppinglist.databinding.ShoppingItemDetailsBinding

class DetailsActivity: AppCompatActivity()
{

    private lateinit var binding: ShoppingItemDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ShoppingItemDetailsBinding.inflate(layoutInflater)
        binding.tvName.text =intent.getStringExtra("Name")
        binding.tvDesc.text = intent.getStringExtra("Desc")
        binding.tvPrice.text =intent.getFloatExtra("Price",1.0f).toString()
        binding.tvINRPrice.text = intent.getFloatExtra("INR",1.0f).toString()
        binding.tvUSDPrice.text = intent.getFloatExtra("USD",1.0f).toString()
        binding.tvRUBPrice.text = intent.getFloatExtra("RUB",1.0f).toString()
        if(intent.getBooleanExtra("Status",false))
        {
            binding.tvStatus.text = "Bought"
        }
        else
        {
            binding.tvStatus.text = "To Buy"
        }
        if(intent.getIntExtra("Image",0) == 0)
        {
            binding.icCategory.setImageResource(R.mipmap.ic_foodicon)
        }
        else if(intent.getIntExtra("Image",0) == 1)
        {
            binding.icCategory.setImageResource(R.mipmap.ic_homeicon)
        }
        else if(intent.getIntExtra("Image",0) == 2)
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