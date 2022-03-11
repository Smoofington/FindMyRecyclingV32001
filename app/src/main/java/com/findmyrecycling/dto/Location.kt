package com.findmyrecycling.dto

class Location (var id : Int = 0, var name : String, var product : String, var common: String){
    override fun toString() : String {
        return common
    }
}