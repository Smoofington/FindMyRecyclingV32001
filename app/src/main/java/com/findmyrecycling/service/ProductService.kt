package com.findmyrecycling.service

import android.content.ClipData
import com.findmyrecycling.RetrofitClientInstance
import com.findmyrecycling.dao.IProductDAO
import com.findmyrecycling.dto.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


interface IProductService {
    suspend fun fetchProduct() : List<Product>?
}

class ProductService : IProductService {
    override suspend fun fetchProduct() : List <Product>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IProductDAO::class.java)
            val items = async { service?.getAllProducts()}
            var result = items.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}