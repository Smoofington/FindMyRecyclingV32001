package com.findmyrecycling.dto

import com.google.gson.annotations.SerializedName

data class Product(@SerializedName("product") var product: String, var id : Int = 0, var common : String ) {
    override fun toString(): String {
        return common
    }
}