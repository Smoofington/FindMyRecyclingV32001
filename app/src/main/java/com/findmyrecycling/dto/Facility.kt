package com.findmyrecycling.dto

data class Facility (
    var facilityId: int = 0,
    var productId: int = 0,
    var facilityName: String = "",
    var facilityDescription: String = "",
    var longitude: String = "",
    var latitude: String = ""){
}