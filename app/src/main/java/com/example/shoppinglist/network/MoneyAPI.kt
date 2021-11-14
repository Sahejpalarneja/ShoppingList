package com.example.shoppinglist.network

import com.example.shoppinglist.data.MoneyResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query



interface MoneyAPI {
    @GET("/latest")
    fun getMoney(@Query("from") base: String) : Call<MoneyResult>
}