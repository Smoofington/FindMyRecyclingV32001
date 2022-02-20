package com.findmyrecycling.dao


import com.findmyrecycling.dto.Product
import retrofit2.Call
import retrofit2.http.GET

class IProductDAO {
    @GET("/mMUesHYPkXjaFGfS/arcgis/rest/services/Recycling_Depot_Locations/FeatureServer/0/query?outFields=*&where=1%3D1&f=geojson")
    fun getAllProducts() : Call<ArrayList<Product>>

}