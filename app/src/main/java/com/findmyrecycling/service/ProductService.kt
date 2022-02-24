package com.findmyrecycling.service

import com.findmyrecycling.RetrofitClientInstance
import com.findmyrecycling.dao.IProductDAO
import com.findmyrecycling.dto.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class ProductService{

    suspend fun fetchProducts() : List<Product>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IProductDAO::class.java)
            val products = async {service?.getAllProducts()}
            var result = products.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}