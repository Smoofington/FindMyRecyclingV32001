package com.findmyrecycling.dao

import retrofit2.Call
import retrofit2.http.GET

class IProductDAO {
    @GET("/perl/mobile/viewplantsjsonarray.pl")
    fun getAllProducts() : Call<ArrayList<Products>>

}