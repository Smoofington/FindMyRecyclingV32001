package com.findmyrecycling

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.findmyrecycling.dto.Product
import com.findmyrecycling.service.ProductService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ProductTests {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var productService : ProductService
    private var allProducts : List<Product>? = ArrayList()

    lateinit var mvm : MainViewModel

    @MockK
    lateinit var mockProductService : ProductService

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @Before
    fun initMocksAndMainThread(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
    @Test
    fun `Given product data are available when I search for cell phones then I should receive a location of recycling center` () = runTest {
        givenProductServiceIsInitialized()
        whenProductDataAreReadAndParsed()
        thenTheProductCollectionShouldContainCellPhones()
    }
    @Test
    fun `Given product data are available when I search for batteries then I should receive a location of recycling center having lead acid batteries or rechargeable batteries` () = runTest {
        givenProductServiceIsInitialized()
        whenProductDataAreReadAndParsed()
        thenTheProductCollectionShouldContainBatteriesTypes()
    }

    @Test
    fun `Given product data are available when I search for gibberish then I should receive nothing` () = runTest {
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

    private fun thenTheProductCollectionShouldContainCellPhones() {
        assertNotNull(allProducts)
        assertTrue(allProducts!!.isNotEmpty())
        var containsCellPhones = false
        allProducts!!.forEach {
            if (it.product == ("Cell Phones")) {
                containsCellPhones = true
            }
        }
        assertTrue(containsCellPhones)
    }

    private fun thenTheProductCollectionShouldContainBatteriesTypes() {
        assertNotNull(allProducts)
        assertTrue(allProducts!!.isNotEmpty())
        var containsBatteries = false
        allProducts!!.forEach {
            if (it.product == ("Rechargeable Batteries") || it.product == ("Lead Acid Batteries")) {
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
            if (it.product != ("Human Remains")) {
                containsNothing = true
            }
        }
        assertTrue(containsNothing)
    }

    @Test
    fun `Given a view model with live data when populated with Cell Phones then results show Cell Phone Recycling Location` () = runTest {
        givenViewModelIsInitializedWithMockData()
        whenProductServiceFetchProductsInvoked()
        thenResultsShouldContainCellPhoneRecycling()
    }

    private fun givenViewModelIsInitializedWithMockData() {
        val products = ArrayList<Product>()
        products.add(Product("Cell Phone", 1 ))

        coEvery {(mockProductService.fetchProducts())} returns products

        mvm = MainViewModel(productService = mockProductService)

    }

    private fun whenProductServiceFetchProductsInvoked() {
        mvm.fetchProducts()
    }

    private fun thenResultsShouldContainCellPhoneRecycling() {

        var allProducts : List<Product>? = ArrayList()

        val latch = CountDownLatch(1)
        //creating a var observer assigning a static object
        val observer = object : Observer<List<Product>>{
            override fun onChanged(recievedProducts: List<Product>?) {
                allProducts =  recievedProducts
                latch.countDown()
                mvm.products.removeObserver(this)
            }
        }
        mvm.products.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)
        assertNotNull(allProducts)
        assertTrue(allProducts!!.isNotEmpty())
        var containsCellPhone = false
        allProducts!!.forEach {
            if (it.product == ("Cell Phone")) {
                containsCellPhone = true
            }
        }
        assertTrue(containsCellPhone)
    }


}