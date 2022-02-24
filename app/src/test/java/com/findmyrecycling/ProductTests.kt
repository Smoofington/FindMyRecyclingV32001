package com.findmyrecycling

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.findmyrecycling.dto.Product
import com.findmyrecycling.service.ProductService
import junit.framework.Assert.*
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ProductTests {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var productService : ProductService
    var allProducts : List<Product>? = ArrayList<Product>()

    @Test
    fun `Given product data are available when I search for cell phone then I should receive A facility to recycle cell phones` () = runTest {
        givenProductServiceIsInitalized()
        whenProductDataAreReadAndParsed()
        thenTheProductCollectionShouldContainFacilitiesToRecycleCellPhones()
    }

    private fun givenProductServiceIsInitalized() {
        productService = ProductService()
    }

    private suspend fun whenProductDataAreReadAndParsed() {
        allProducts = productService.fetchProducts()
    }

    private fun thenTheProductCollectionShouldContainFacilitiesToRecycleCellPhones() {
        assertNotNull(allProducts)
        assertTrue(allProducts!!.isNotEmpty())
        var containsCellPhones = false
        allProducts!!.forEach {
            if (it.product.equals(("Cell Phones"))){
                containsCellPhones = true
            }
        }
        assertTrue(containsCellPhones)
    }


}