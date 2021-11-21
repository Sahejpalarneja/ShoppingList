package com.example.shoppinglist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.widget.Toast


import com.example.shoppinglist.data.MoneyResult

import com.example.shoppinglist.databinding.ShoppingItemDetailsBinding
import com.example.shoppinglist.network.MoneyAPI
import com.romainpiel.shimmer.Shimmer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DetailsActivity: AppCompatActivity()
{

    private lateinit var binding: ShoppingItemDetailsBinding
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.frankfurter.app")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val currencyAPI = retrofit.create(MoneyAPI::class.java)


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val price = intent.getFloatExtra("Price",1.0f)
        getCurrencies(price)
        binding = ShoppingItemDetailsBinding.inflate(layoutInflater)
        binding.tvName.text =intent.getStringExtra("Name")
        binding.tvDesc.text = String.format(getString(R.string.Description),intent.getStringExtra("Desc"))
        binding.tvPrice.text = String.format(getString(R.string.PriceH),price)

        if(intent.getBooleanExtra("Status",false))
        {
            binding.tvStatus.text = getString(R.string.StatusTrue)
        }
        else
        {
            binding.tvStatus.text = getString(R.string.StatusFalse)
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

        shimmer.start(binding.tvName)

        setContentView(binding.root)
        binding.btnOK.setOnClickListener {
             val mainIntent = Intent()
            mainIntent.setClass(this,MainActivity::class.java)
            startActivity(mainIntent)
        }
    }


    private fun getCurrencies(price:Float)
    {

        val moneyCall = currencyAPI.getMoney("HUF")
        moneyCall.enqueue(object : Callback<MoneyResult> {
            override fun onFailure(call: Call<MoneyResult>, t: Throwable) {
                Toast.makeText(this@DetailsActivity,"HERE",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MoneyResult>, response: Response<MoneyResult>) {
                val moneyResult = response.body()
                binding.tvINRPrice.text = String.format(getString(R.string.PriceI),moneyResult?.rates?.INR?.toFloat()?.times(price))
                binding.tvUSDPrice.text = String.format(getString(R.string.PriceU),moneyResult?.rates?.USD?.toFloat()?.times(price))
                binding.tvRUBPrice.text = String.format(getString(R.string.PriceR),moneyResult?.rates?.RUB?.toFloat()?.times(price))


            }
        })

    }

}


