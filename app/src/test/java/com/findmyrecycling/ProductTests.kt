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
    fun `Given product data are available when I search for cell phone then I should receive a location of recycling center` () = runTest {
        givenProductServiceIsInitialized()
        whenProductDataAreReadAndParsed()
        thenTheProductCollectionShouldContainCellPhone()
    }
    @Test
    fun `Given product data are available when I search for batteries then I should receive a location of recycling center having lead acid batteries or rechargable batteries` () = runTest {
        givenProductServiceIsInitialized()
        whenProductDataAreReadAndParsed()
        thenTheProductCollectionShouldContainBatteriesTypes()
    }

    @Test
    fun `Given product data are available when I search for giberish then I should receive nothing` () = runTest {
        givenProductServiceIsInitialized()
        whenProductDataAreReadAndParsed()
        thenTheProductCollectionShouldContainNothing()
    }

    private fun givenProductServiceIsInitialized() {
        productService = ProductService()
    }

    private suspend fun whenProductDataAreReadAndParsed() {
        allProducts = productService.fetchProducts()
    }

    private fun thenTheProductCollectionShouldContainCellPhone() {
        assertNotNull(allProducts)
        assertTrue(allProducts!!.isNotEmpty())
        var containsCellPhone = false
        allProducts!!.forEach {
            if (it.product.equals("Cell Phones")) {
                containsCellPhone = true
            }
        }
        assertTrue(containsCellPhone)
    }

    private fun thenTheProductCollectionShouldContainBatteriesTypes() {
        assertNotNull(allProducts)
        assertTrue(allProducts!!.isNotEmpty())
        var containsBatteries = false
        allProducts!!.forEach {
            if (it.product.equals("Rechargable Batteries") || it.product.equals("Lead Acid Batteries")) {
                containsBatteries = true
            }
        }
        assertTrue(containsBatteries)
    }

    private fun thenTheProductCollectionShouldContainNothing() {
        assertNotNull(allProducts)
        assertTrue(allProducts!!.isNotEmpty())
        var containsNothing = false
        allProducts!!.forEach {
            if (!it.product.equals("Human Remains")) {
                containsNothing = true
            }
        }
        assertTrue(containsNothing)
    }



}