package com.findmyrecycling.dao


import com.findmyrecycling.dto.Product
import retrofit2.Call
import retrofit2.http.GET

class IProductDAO {
    @GET()
    fun getAllProducts() : Call<ArrayList<Product>>

}