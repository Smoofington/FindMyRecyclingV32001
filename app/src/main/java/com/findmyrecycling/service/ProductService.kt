package com.findmyrecycling.service

import app.findmyrecycling.RetrofitClientInstance
import com.findmyrecycling.dao.IProductDAO
import com.findmyrecycling.dto.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

interface ProductService {
    suspend fun fetchProduct() : List<Product>?
}

class ProductService : IProductService {

    override suspend fun fetchPlants() : List<Product>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IProductDAO::class.java)
            val products = async {service?.getAllPlants()}
            var result = products.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}