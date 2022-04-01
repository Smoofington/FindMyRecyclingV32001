package com.findmyrecycling.dto

data class Product(
    var product : String,
    var id : Int = 0,) {
    override fun toString(): String {
        return product
    }
}