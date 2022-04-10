package com.findmyrecycling.dto

data class Facility(
    var facilityId : String = "",
    var facilityName : String = "",
    var location : String = "",
    var description : String = "",
    var recyclableProducts : String = "",
) {
}