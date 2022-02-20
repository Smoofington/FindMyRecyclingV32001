package com.findmyrecycling

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.findmyrecycling.dto.Product
import com.findmyrecycling.service.ProductService
import junit.framework.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ProductTests {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var productService : ProductService
    var allProducts : List<Product>? = ArrayList<Product>()

    @Test
    suspend fun `Given product data are available when I search for motor oil then I should receive A facility to recycle motor oil` () = runTest {
        givenProductServiceIsInitalized()
        whenProductDataAreReadAndParsed()
        thenTheProductCollectionShouldContainFacilitiesToRecycleMotorOil()
    }

    private fun givenProductServiceIsInitalized() {
        productService = ProductService()
    }

    private suspend fun whenProductDataAreReadAndParsed() {
        allProducts = productService.fetchProducts()
    }

    private fun thenTheProductCollectionShouldContainFacilitiesToRecycleMotorOil() {
        Assert.assertNotNull(allProducts)
        Assert.assertTrue(allProducts!!.isNotEmpty())
        var containsMotorOil = false
        allProducts!!.forEach {
            if (it.product.equal(("motor oil"))){
                containsMotorOil = true
            }
        }
    }
    assertTrue(containsMotorOil)

}