package com.findmyrecycling


import com.findmyrecycling.service.IProductService
import com.findmyrecycling.service.ProductService
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

@JvmField
val appModule = module {
    viewModel { ProductViewModel(get()) }
    single <IProductService>{ ProductService() }
}