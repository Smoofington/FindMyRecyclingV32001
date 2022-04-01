package com.findmyrecycling.dto

data class Facility(
    var locationId : Int = 0,
    var facilityName : String = "",
    var facilityId : String = "",
    var location : String = "",
    var description : String = "",
    var recyclableProducts : String = ""
) {
    override fun toString(): String {
        return "$facilityName $description $location $recyclableProducts"
    }
}