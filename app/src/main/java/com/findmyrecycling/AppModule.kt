package com.findmyrecycling

import com.findmyrecycling.service.IProductService
import com.findmyrecycling.service.ProductService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
    single<IProductService> { ProductService() }
}