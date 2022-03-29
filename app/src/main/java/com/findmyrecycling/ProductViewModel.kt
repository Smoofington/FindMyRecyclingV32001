package com.findmyrecycling

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.findmyrecycling.dto.Product
import com.findmyrecycling.service.IProductService
import com.findmyrecycling.service.ProductService
import kotlinx.coroutines.launch


class ProductViewModel (var productService: IProductService = ProductService()): ViewModel() {

    var products : MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
    fun fetchProduct(){
        viewModelScope.launch {
            products.postValue(productService.fetchProduct())
        }
    }
}