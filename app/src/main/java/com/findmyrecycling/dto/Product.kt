package com.findmyrecycling.dto

data class Product(
    var product : String = "",
    var facilityName : String = "",
    var productId : Int = 0,

) {
    override fun toString(): String
    {
        return product
    }
}