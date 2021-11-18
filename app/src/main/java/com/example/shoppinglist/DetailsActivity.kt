package com.example.shoppinglist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent

import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.Item


import com.example.shoppinglist.databinding.ShoppingItemDetailsBinding
import com.romainpiel.shimmer.Shimmer

class DetailsActivity: AppCompatActivity()
{

    private lateinit var binding: ShoppingItemDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ShoppingItemDetailsBinding.inflate(layoutInflater)
        binding.tvName.text =intent.getStringExtra("Name")
        binding.tvDesc.text = "Description:\n\n"+intent.getStringExtra("Desc")
        binding.tvPrice.text ="Price in HUF: : "+intent.getFloatExtra("Price",1.0f).toString()
        binding.tvINRPrice.text = "Price in INR: "+intent.getFloatExtra("INR",1.0f).toString()
        binding.tvUSDPrice.text = "Price in USD: "+intent.getFloatExtra("USD",1.0f).toString()
        binding.tvRUBPrice.text = "Price in RUB: "+intent.getFloatExtra("RUB",1.0f).toString()
        if(intent.getBooleanExtra("Status",false))
        {
            binding.tvStatus.text = "Status: Bought"
        }
        else
        {
            binding.tvStatus.text = "Status: To Buy"
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
        val shimmer = Shimmer()
        shimmer.start(binding.tvDesc)
        shimmer.start(binding.tvName)
        shimmer.start(binding.tvPrice)
        shimmer.start(binding.tvStatus)
        shimmer.start(binding.tvINRPrice)
        shimmer.start(binding.tvUSDPrice)
        shimmer.start(binding.tvRUBPrice)

        setContentView(binding.root)
        binding.btnOK.setOnClickListener {
             val mainIntent = Intent()
            mainIntent.setClass(this,MainActivity::class.java)
            startActivity(mainIntent)
        }
    }


}