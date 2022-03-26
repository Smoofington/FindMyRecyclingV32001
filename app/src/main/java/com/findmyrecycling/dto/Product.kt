package com.findmyrecycling.dto

import com.google.gson.annotations.SerializedName

data class Product(
    var product : String,
    var id : Int = 0,
    @SerializedName("name")var common : String,
    ) {
    override fun toString(): String {
        return name
    }
}