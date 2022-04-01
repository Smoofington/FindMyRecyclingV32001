package com.findmyrecycling.dto

import com.google.gson.annotations.SerializedName

data class Product(
    internal var product : String,
    internal var id : Int = 0,
) {
    override fun toString(): String {
        return product
    }
}