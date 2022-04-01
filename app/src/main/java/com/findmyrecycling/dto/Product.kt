package com.findmyrecycling.dto

import com.google.gson.annotations.SerializedName

data class Product(
    var productId : Int = 0,
    var product : String = ""
) {
    override fun toString(): String
    {
        return product
    }
}