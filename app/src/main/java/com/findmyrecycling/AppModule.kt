package com.findmyrecycling

import com.findmyrecycling.service.ProductService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val appModule = module {
    viewModel { MainViewModel(get()) }
    single <ProductService>{ ProductService() }
}