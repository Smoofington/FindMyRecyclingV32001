package com.findmyrecycling.dto

data class Facility (var id : Int = 0, var name : String, var product : String){
    override fun toString() : String {
        return name
    }
}
// Hello