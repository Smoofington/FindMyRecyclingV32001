/*package com.findmyrecycling

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.findmyrecycling.dto.Product
import com.findmyrecycling.service.ProductService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ProductUnitTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var mvm : MainViewModel

    @MockK
    lateinit var mockProductService : ProductService

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @Before
    fun initMocksAndMainThread() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `given a view model with live data when populated with products then results show location of recycling center` () {
        givenViewModelIsInitializedWithMockData()
        whenProductServiceFetchLocationsInvoked()
        thenResultsShouldContainLocation()
    }

    private fun givenViewModelIsInitializedWithMockData() {
        val products = ArrayList<Product>()
        products.add(Product(1, "Generic Recycling Center1", "Cell Phones"))
        val paint = Product(2, "Generic Recycling Center2", "Paint")
        products.add(paint)
        products.add(Product(3, "Generic Recycling Center3", "Tires"))

        coEvery { mockProductService.fetchProducts() } returns products

        mvm = MainViewModel(productService = mockProductService)
    }

    private fun whenProductServiceFetchLocationsInvoked() {
        mvm.fetchProducts()
    }

    private fun thenResultsShouldContainLocation() {
        var allProducts : List<Product>? = ArrayList<Product>()
        val latch = CountDownLatch(1)
        val observer = object : Observer<List<Product>> {
            override fun onChanged(receivedProducts: List<Product>?) {
                allProducts = receivedProducts
                latch.countDown()
                mvm.products.removeObserver(this)
            }
        }
        mvm.products.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)
        Assert.assertNotNull(allProducts)
        Assert.assertTrue(allProducts!!.isNotEmpty())
        var containsCellPhones = false
        allProducts!!.forEach {
            if (it.product.equals(("Cell Phones"))) {
                containsCellPhones = true
            }
        }
        Assert.assertTrue(containsCellPhones)
    }
}*/