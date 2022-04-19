package com.findmyrecycling.dto

data class Product(
    var product : String = "",
    var productId : Int = 0,
    var facilityName : String = "",

) {
    override fun toString(): String
    {
        return product
    }
}