package com.findmyrecycling.dto

import com.google.gson.annotations.SerializedName

data class Product(
    var product : String = "",
    var productId : Int = 0,
) {
    override fun toString(): String
    {
        return product
    }
}