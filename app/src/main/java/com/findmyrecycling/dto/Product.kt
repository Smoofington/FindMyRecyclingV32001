package com.findmyrecycling.dto


data class Product(var id : Int = 0, var name : String, var product : String) {
    override fun toString(): String {
        return product
    }
}
