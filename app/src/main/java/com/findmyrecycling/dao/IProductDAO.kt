package com.findmyrecycling.dao

import com.findmyrecycling.dto.Product
import retrofit2.Call
import retrofit2.http.GET

interface IProductDAO {
    @GET("Smoofington/FindMyRecyclingV32001/master/findmyrecyclingJSON.md")
    fun getAllProducts() : Call<ArrayList<Product>>
}